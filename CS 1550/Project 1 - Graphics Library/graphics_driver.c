#include "graphics.h"
#include <unistd.h>

int main() {
    init_graphics();
    printf("Graphics initialized\n");
    //wait
    sleep(10);
    exit_graphics();
    printf("Graphics exited\n");
    return 0;
}