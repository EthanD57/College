#include<stdio.h>
int initialize() {
    printf("Initializing plugin2\n");
    return 0;
}

int run(int arg1, int arg2) {
    if (arg1 != '\0' && arg2 != '\0') {
        printf("%d", arg1);
        printf("%d", arg2);
    }
    else{
        printf("No arguments passed!\n");
    }
    printf("Running plugin2\n");
    return 0;
}

int cleanup() {
    printf("Cleaning plugin\n");
    return 0;
}