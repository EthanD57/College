#define _POSIX_C_SOURCE 199309L
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
#include <semaphore.h>

sem_t sem;

int main()
{
    sem_init(&sem, 0, 1); // Initialize the semaphore
	//Sockets represent potential connections
	//We make an internet socket
	int sfd = socket(PF_INET, SOCK_STREAM, 0);
	if(-1 == sfd)
	{
		perror("Cannot create socket\n");
		exit(EXIT_FAILURE);
	}

	//We will configure it to use this machine's IP, or
	//for us, localhost (127.0.0.1)
	struct sockaddr_in addr;
	memset(&addr, 0, sizeof(addr));

	addr.sin_family = AF_INET;
	//Web servers always listen on port 80
	addr.sin_port = htons(80);
	addr.sin_addr.s_addr = htonl(INADDR_ANY);

	//So we bind our socket to port 80
	if(-1 == bind(sfd, (struct sockaddr *)&addr, sizeof(addr)))
	{
		perror("Bind failed");
		exit(EXIT_FAILURE);
	}

	//And set it up as a listening socket with a backlog of 10 pending connections
	if(-1 == listen(sfd, 10))
	{
		perror("Listen failed");
		exit(EXIT_FAILURE);
	}
    FILE *stats_file = fopen("stats_proc.txt", "w"); 

	//A server's gotta serve...
	for(;;){
		//accept() blocks until a client connects. When it returns,
		//we have a client that we can do client stuff with.
		int connfd = accept(sfd, NULL, NULL);
		if(connfd < 0)
		{
			perror("Accept failed");
			exit(EXIT_FAILURE);
		}

		//At this point a client has connected. The remainder of the
		//loop is handling the client's GET request and producing
		//our response.

        //FORK TIME

        pid_t pid = fork();
        if(pid < 0){  // Fork failed ----------------
            perror("Fork failed");
            exit(EXIT_FAILURE);
        }
        else if(pid == 0){ // Child process ----------------
            struct stat file_stats;
            struct timespec start, end;
            double timeDiff;
            clock_gettime(CLOCK_REALTIME, &start);  // Start timing the request


            char buffer[1024];
            char filename[1024];
            FILE *f;
            
            
            memset(buffer, sizeof(buffer), 0);
            memset(filename, sizeof(buffer), 0);

            //In HTTP, the client speaks first. So we recv their message
            //into our buffer.
            int amt = recv(connfd, buffer, sizeof(buffer), 0);
            fprintf(stderr, "%s", buffer);

            //We only can handle HTTP GET requests for files served
            //from the current working directory, which becomes the website root
            if(sscanf(buffer, "GET /%s", filename)<1) {

                fprintf(stderr, "Bad HTTP request\n");
                shutdown(connfd, SHUT_RDWR);
                close(connfd);

                sem_wait(&sem); //ENTER CRITICAL SECTION

                clock_gettime(CLOCK_REALTIME, &end);
                timeDiff = (end.tv_sec - start.tv_sec) + (end.tv_nsec - start.tv_nsec) / 1e9;
                fprintf(stats_file, "%s 0 %.4f\n", filename, timeDiff); //Write filename, 

                sem_post(&sem); //EXIT CRITICAL SECTION

                fclose(stats_file);
                exit(EXIT_FAILURE);
            }


            if(amt == sizeof(buffer)){
                while(recv(connfd, buffer, sizeof(buffer), 0) == sizeof(buffer))
                    /* discard */;
            }   
            f = fopen(filename, "rb");
            if(f == NULL){
                strcpy(buffer, "HTTP/1.1 404 Not Found\n\n");
                send(connfd, buffer, strlen(buffer), 0);
                shutdown(connfd, SHUT_RDWR);
                close(connfd);

                sem_wait(&sem); //ENTER CRITICAL SECTION

                clock_gettime(CLOCK_REALTIME, &end);
                timeDiff = (end.tv_sec - start.tv_sec) + (end.tv_nsec - start.tv_nsec) / 1e9;
                fprintf(stats_file, "%s 0 %.4f\n", filename, timeDiff);

                sem_post(&sem); //EXIT CRITICAL SECTION

                fclose(stats_file);
                exit(EXIT_FAILURE);
            }
            else{
                int size;
                char response[1024];

                strcpy(response, "HTTP/1.1 200 OK\n");
                send(connfd, response, strlen(response), 0);

                time_t now;
                time(&now);
                //How convenient that the HTTP Date header field is exactly
                //in the format of the asctime() library function.
                //
                //asctime adds a newline for some dumb reason.
                sprintf(response, "Date: %s", asctime(gmtime(&now)));
                send(connfd, response, strlen(response), 0);

                //Get the file size via the stat system call
                fstat(fileno(f), &file_stats);
                size = file_stats.st_size;
                sprintf(response, "Content-Length: %d\n", file_stats.st_size);
                send(connfd, response, strlen(response), 0);

                //Tell the client we won't reuse this connection for other files
                strcpy(response, "Connection: close\n");
                send(connfd, response, strlen(response), 0);

                //Send our MIME type and a blank line
                strcpy(response, "Content-Type: text/html\n\n");
                send(connfd, response, strlen(response), 0);

                fprintf(stderr, "File: %s\n", filename);

                int bytes_read = 0;
                do{
                    //read response amount of data at a time.
                    //Note that sizeof() in C can only tell us the number of
                    //elements in an array that is declared in scope. If you
                    //move the declaration elsewhere, it will degrade into
                    //the sizeof a pointer instead.
                    bytes_read = fread(response, 1, sizeof(response), f);

                    //if we read anything, send it
                    if(bytes_read > 0){
                        int sent = send(connfd, response, bytes_read, 0);
                        //It's possible that send wasn't able to send all of
                        //our response in one call. It will return how much it
                        //actually sent. Keep calling send until all of it is
                        //sent.
                        while(sent < bytes_read){
                            sent += send(connfd, response+sent, bytes_read-sent, 0);
                        }
                    }
                } while(bytes_read > 0 && bytes_read == sizeof(response));

                fclose(f);
                
                sem_wait(&sem); //ENTER CRITICAL SECTION

                clock_gettime(CLOCK_REALTIME, &end);
                timeDiff = (end.tv_sec - start.tv_sec) + (end.tv_nsec - start.tv_nsec) / 1e9;
                fprintf(stats_file, "%s %d %.4f\n", filename, size, timeDiff);

                sem_post(&sem); //EXIT CRITICAL SECTION
                fclose(stats_file);
            }
            shutdown(connfd, SHUT_RDWR);
            close(connfd);

            exit(EXIT_SUCCESS);
        }
        else{
            // Parent process ----------------
            close(connfd);
            waitpid(pid, NULL, WNOHANG);
            shutdown(connfd, SHUT_RDWR);
        }

	}
    fclose(stats_file);
	close(sfd);
	return 0;
}

