#include "graphics.h"

int main() {
    init_graphics();
    printf("Graphics initialized\n");
    clear_screen();

    color_t color = rgbConverter(31, 00, 31);

    draw_rect(0, 0, 20, 20, color);

    exit_graphics();
    printf("Graphics exited\n");
    return 0;
}