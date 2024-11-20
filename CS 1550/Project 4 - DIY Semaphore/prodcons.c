#include <stdio.h>
#include <stdlib.h>
#include <sys/mman.h>
#include <fcntl.h>
#include <unistd.h>
#include <asm-generic/mman-common.h>

struct cs1550_sem {
    void *kernel_sem_ptr;  // Opaque pointer to kernel structure. MUCH easier to manage the list and we never use any of the data from it either
};

void down(struct cs1550_sem *sem) {
    syscall(452, sem);
}

void up(struct cs1550_sem *sem) {
    syscall(453, sem);
}

void init(struct cs1550_sem *sem, int value) {
    syscall(451, sem, value);
}

struct cs1550_sem *semmutex;
struct cs1550_sem *semfull; 
struct cs1550_sem *semempty; 

int *buffer;
int *in;
int *out;
int *counter;
int *total;
int *N;

void producer(int name) { //Puts the next value into buffer and then increments the in index
    while (1) {
        down(semempty);
        down(semmutex);

        buffer[*in] = (*total)++;
        printf("Producer %d Produced: %d\n", name, buffer[*in]);
        *in = (*in + 1) % *N;
        (*counter)++;

        up(semmutex);
        up(semfull);
    }
}

void consumer(int name) { //Consumes the next value from buffer and then increments the out index
    while (1) {
        down(semfull);
        down(semmutex);

        printf("Consumer %d Consumed: %d\n", name, buffer[*out]);
        *out = (*out + 1) % *N;
        (*counter)--;

        up(semmutex);
        up(semempty);
    }
}

void init_shared_memory(int bufferSize) {   
    size_t size = sizeof(struct cs1550_sem) * 3 + sizeof(int) * 5 + sizeof(int) * bufferSize;   //Total size of shared memory that we need
    void *ptr = mmap(NULL, size, PROT_READ | PROT_WRITE, MAP_SHARED | MAP_ANONYMOUS, -1, 0);

    // Set up the struct pointers to different sections of the shared memory
    semmutex = (struct cs1550_sem *)ptr;    
    semfull = (struct cs1550_sem *)(ptr + sizeof(struct cs1550_sem));
    semempty = (struct cs1550_sem *)(ptr + (2 * sizeof(struct cs1550_sem)));

    //This is the base of the integer section of the shared memory
    void *int_base = ptr + (3 * sizeof(struct cs1550_sem));
    N = (int *)int_base;
    counter = (int *)(int_base + sizeof(int));
    in = (int *)(int_base + (2 * sizeof(int)));
    out = (int *)(int_base + (3 * sizeof(int)));
    total = (int *)(int_base + (4 * sizeof(int)));

    buffer = (int *)(int_base + (5 * sizeof(int)));

    //Initialize the shared memory
    *N = bufferSize;
    *counter = 0;
    *in = 0;
    *out = 0;
    *total = 0;
}

int main(int argc, char *argv[]) {
    //Check for the correct number of arguments
    if (argc != 4) {
        printf("Usage: %s <num_consumers> <num_producers> <buffer_size>\n", argv[0]);
        return 1;
    }

    //Check for valid arguments
    if (atoi(argv[1]) < 1 || atoi(argv[2]) < 1 || atoi(argv[3]) < 1) {
        printf("All arguments must be greater than 0\n");
        return 1;
    }
    int consumers = atoi(argv[1]);
    int producers = atoi(argv[2]);
    int buffersize = atoi(argv[3]);
    
    init_shared_memory(buffersize);

    init(semmutex, 1);
    init(semfull, 0);
    init(semempty, buffersize);

    //fork off the producers
    for (int i = 0; i < producers; i++) {
        pid_t pid = fork();
        if (pid == 0) {
            producer(i+1);
        }
    }

    //fork off the consumers
    for (int i = 0; i < consumers; i++) {
        pid_t pid = fork();
        if (pid == 0) {
            consumer(i+1);
        }
    }

    while(1){ //RUN FOREVER!

    };
    return 0;
}

