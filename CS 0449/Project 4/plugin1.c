#include<stdio.h>
int initialize() {
    printf("Initializing plugin1\n");
    return 0;
}

int run(const char **args) {
    if (args[1] != NULL && args[2] != NULL) {
        int intArg1 = atoi(args[1]);
        int intArg2 = atoi(args[2]);

        printf("Integer value of arg1: %d\n", intArg1);
        printf("Integer value of arg2: %d\n", intArg2);
    } else {
        printf("No arguments passed!\n");
    }

    printf("Running plugin1\n");
    return 0;
}

int cleanup() {
    printf("Cleaning plugin\n");
    return 0;
}