#include <sys/syscall.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>

int send_msg(char *to, char *msg, char *from) {
    syscall(451, to, msg, from);
    return 0;
}

int get_msg(char *to, char *msg, char *from) {
    int returnValue = syscall(452, to, msg, from); //Set initial returnValue of the syscall

    if (returnValue == -1) {  //If the return value is -1, then there are no messages
        printf("No messages for %s\n", to);
    }
    else{  //If the return value is 0, then there no messages remaining after this one
        printf("%s said: \"%s\"\n", from, msg);
    }

    //If there are messages left, then keep printing them. 
    while (returnValue != -1 && returnValue != 0){  
        returnValue = syscall(452, to, msg, from);
        printf("%s said: \"%s\"\n", from, msg);
    }
    return 0;
}

int main(int argc, char *argv[]){  
    char *to = malloc(256);
    char *msg = malloc(1024);
    char *from = malloc(256);


    if (argc < 2) {     //Check if the number of arguments is less than 2. I.e. the user did not user -s or -r
        fprintf(stderr, "Usage: %s [-s recipient message] | [-r]\n", argv[0]);
        exit(1);
    }
    //If the command is send, then send the message
    if (strcmp(argv[1], "-s") == 0) {
        from = getenv("USER");  //Get the username of the current user
        //Check if 4 arguments are passed: -s/-r, to, message, and from
        if (argc != 4) {
            printf("Usage: osmsg -s <to> <msg>\n");
            exit(1);
        }
        strncpy(to, argv[2], 255);  
        to[255] = '\0';  // Ensure null-termination

        strncpy(msg, argv[3], 1023);
        msg[1023] = '\0';  // Ensure null-termination

        send_msg(to, msg, from);
    }
    if (strcmp(argv[1], "-r") == 0){
        to = getenv("USER");    //Get the username of the current user
        get_msg(to, msg, from);
    }
    return 0;


}