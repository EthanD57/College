#define _POSIX_C_SOURCE 200809L
#include <sys/types.h>
#include <sys/socket.h>
#include <sys/stat.h>
#include <netinet/in.h>
#include <arpa/inet.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <time.h>
#include <signal.h>
#include <sys/wait.h>
#include <pthread.h>
#include <string.h>

#define CACHE_LIMIT 5

typedef struct {
    char filename[1024];
    char* content;
    size_t content_length;
} CachedFile;

CachedFile cache[CACHE_LIMIT];
int cache_start = 0;
int cache_count = 0;
pthread_mutex_t cache_lock = PTHREAD_MUTEX_INITIALIZER;
pthread_mutex_t lock = PTHREAD_MUTEX_INITIALIZER;
FILE *stats_file; 
int cache_index = 0;

CachedFile* ammendCache(char *filename){

    FILE *recentFile = fopen(filename, "rb");
    if (recentFile == NULL){
        return NULL;
    }

    struct stat file_stats;
    fstat(fileno(recentFile), &file_stats);

    char *new_content = malloc(file_stats.st_size);
    size_t bytes_read = fread(new_content, 1, file_stats.st_size, recentFile);
    fclose(recentFile);

    cache_index++;
    cache_index =  cache_index > 4 ? 0 : cache_index;  //If greater than 5, we reset to 0 to get oldest item 

    if (cache_count == CACHE_LIMIT){
        free(cache[cache_index].content);
    } else {
        cache_count++;
    }
    cache[cache_index].content = new_content;
    cache[cache_index].content_length = file_stats.st_size;
    strncpy(cache[cache_index].filename, filename, sizeof(cache[cache_index].filename) - 1);

    return &cache[cache_index];
}

CachedFile* fetchCached(const char *filename) {
    
    for (int i = 0; i < cache_count; i++) {
        if (strcmp(cache[i].filename, filename) == 0) {
            return &cache[i];
        }
    }
    return NULL;  // File not found in cache
}


void response(int connfd)
{
    struct timespec start, end;
    struct stat file_stats;
    double timeDiff;
    clock_gettime(CLOCK_REALTIME, &start);  // Start timing the request

    char buffer[1024];
    char filename[1024];
    FILE *f;

    memset(buffer, sizeof(buffer), 0);
    memset(filename, sizeof(buffer), 0);

    // In HTTP, the client speaks first. So we recv their message
    // into our buffer.
    int amt = recv(connfd, buffer, sizeof(buffer), 0);
    fprintf(stderr, "%s", buffer);

    // We only can handle HTTP GET requests for files served
    // from the current working directory, which becomes the website root
    if (sscanf(buffer, "GET /%s", filename) < 1)
    {
        fprintf(stderr, "Bad HTTP request\n");
        shutdown(connfd, SHUT_RDWR);
        close(connfd);

        pthread_mutex_lock(&lock); // ENTER CRITICAL SECTION

        clock_gettime(CLOCK_REALTIME, &end);  // Start timing the request
        timeDiff = (end.tv_sec - start.tv_sec) + (end.tv_nsec - start.tv_nsec) / 1e9;
        fprintf(stats_file, "%s 0 %.4f\n", filename, timeDiff);
        fflush(stats_file);

        pthread_mutex_unlock(&lock); // EXIT CRITICAL SECTION

        pthread_exit(NULL);
    }

    if (amt == sizeof(buffer))
    {
        while (recv(connfd, buffer, sizeof(buffer), 0) == sizeof(buffer))
            /* discard */;
    }

    pthread_mutex_lock(&cache_lock);
    CachedFile *cached = fetchCached(filename);
    CachedFile *localCopy;
    if (cached != NULL){
        localCopy = malloc(sizeof(CachedFile));
        *localCopy = *cached;
        if(cached->content != NULL){
            localCopy->content = strdup(cached->content);
        }
    }
    if (cached == NULL){ // File not found in cache
        cached = ammendCache(filename); // Add file to cache
        if (cached != NULL){ 
            localCopy = malloc(sizeof(CachedFile));
            *localCopy = *cached;
            if(cached->content != NULL){
                localCopy->content = strdup(cached->content);
            }
        }
    }
    pthread_mutex_unlock(&cache_lock); // EXIT CRITICAL SECTION

    if (localCopy == NULL)
    {
        strcpy(buffer, "HTTP/1.1 404 Not Found\n\n");
        send(connfd, buffer, strlen(buffer), 0);
        shutdown(connfd, SHUT_RDWR);
        close(connfd);

        pthread_mutex_lock(&lock); // ENTER CRITICAL SECTION

        clock_gettime(CLOCK_REALTIME, &end);  // Start timing the request
        timeDiff = (end.tv_sec - start.tv_sec) + (end.tv_nsec - start.tv_nsec) / 1e9;
        fprintf(stats_file, "%s 0 %.4f\n", filename, timeDiff);
        fflush(stats_file);

        pthread_mutex_unlock(&lock); // EXIT CRITICAL SECTION
        
        pthread_exit(NULL);
    }

    int size = localCopy->content_length;
    char response[1024];

    strcpy(response, "HTTP/1.1 200 OK\n");
    send(connfd, response, strlen(response), 0);

    time_t now;
    time(&now);
    // How convenient that the HTTP Date header field is exactly
    // in the format of the asctime() library function.
    //
    // asctime adds a newline for some dumb reason.
    sprintf(response, "Date: %s", asctime(gmtime(&now)));
    send(connfd, response, strlen(response), 0);

    // Get the file size via the stat system call
    sprintf(response, "Content-Length: %d\n", size);
    send(connfd, response, strlen(response), 0);

    // Tell the client we won't reuse this connection for other files
    strcpy(response, "Connection: close\n");
    send(connfd, response, strlen(response), 0);

    // Send our MIME type and a blank line
    strcpy(response, "Content-Type: text/html\n\n");
    send(connfd, response, strlen(response), 0);

    fprintf(stderr, "File: %s\n", filename);

    size_t remaining = localCopy->content_length;
    size_t offset = 0;

    while (remaining > 0) {
            // If the file is smaller than our response size, we just send the exact file size
            size_t send_size = (remaining < sizeof(response)) ? remaining : sizeof(response);
            
            // Send data from the cached content
            int sent = send(connfd, localCopy->content + offset, send_size, 0);
            if (sent <= 0) break;  // Handle error or connection closed
            
            remaining -= sent; // Decrease the remaining size by the amount sent
            offset += sent; // If we didn't send all the data, we need to move the offset to where we stopped
        }

    free(localCopy->content);
    free(localCopy);

    pthread_mutex_lock(&lock); // ENTER CRITICAL SECTION

    clock_gettime(CLOCK_REALTIME, &end);  // Start timing the request
    timeDiff = (end.tv_sec - start.tv_sec) + (end.tv_nsec - start.tv_nsec) / 1e9;
    fprintf(stats_file, "%s %ld %.4f\n", filename, size, timeDiff);
    fflush(stats_file);

    pthread_mutex_unlock(&lock); // ENTER CRITICAL SECTION

    shutdown(connfd, SHUT_RDWR);
    close(connfd);

    pthread_exit(NULL);
}

int main()
{
    stats_file = fopen("stats_cache.txt", "w");
    // Sockets represent potential connections
    // We make an internet socket
    int sfd = socket(PF_INET, SOCK_STREAM, 0);
    if (-1 == sfd)
    {
        perror("Cannot create socket\n");
        exit(EXIT_FAILURE);
    }

    // We will configure it to use this machine's IP, or
    // for us, localhost (127.0.0.1)
    struct sockaddr_in addr;
    memset(&addr, 0, sizeof(addr));

    addr.sin_family = AF_INET;
    // Web servers always listen on port 80
    addr.sin_port = htons(80);
    addr.sin_addr.s_addr = htonl(INADDR_ANY);

    // So we bind our socket to port 80
    if (-1 == bind(sfd, (struct sockaddr *)&addr, sizeof(addr)))
    {
        perror("Bind failed");
        exit(EXIT_FAILURE);
    }

    // And set it up as a listening socket with a backlog of 10 pending connections
    if (-1 == listen(sfd, 10))
    {
        perror("Listen failed");
        exit(EXIT_FAILURE);
    }

    // A server's gotta serve...
    for (;;)
    {
        // accept() blocks until a client connects. When it returns,
        // we have a client that we can do client stuff with.
        int connfd = accept(sfd, NULL, NULL);
        if (connfd < 0)
        {
            perror("Accept failed");
            exit(EXIT_FAILURE);
        }

        // At this point a client has connected. The remainder of the
        // loop is handling the client's GET request and producing
        // our response.

        // THREAD TIME

        pthread_t thread;
        pthread_create(&thread, NULL, response, (void *) connfd);
        pthread_detach(thread);
    }
    fclose(stats_file);
    close(sfd);
    pthread_mutex_destroy(&lock);
    pthread_mutex_destroy(&cache_lock);
    return 0;
}
