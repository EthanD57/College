#define SYS_OPEN 5
#define SYS_CLOSE 6
#define SYS_WRITE 4
#define SYS_READ 3

void init_graphics(){
    int frameBuffer = open("/dev/fb0", SYS_OPEN);
    if (frameBuffer < 0){
        printf("Error: Could not open framebuffer device.\n");
        exit(1);
    }
    printf("The framebuffer device was opened successfully.\n");
}