#include "graphics.h"

int main() {
    init_graphics();
    clear_screen();

    color_t color1 = rgbConverter(31, 00, 31);
    color_t color2 = rgbConverter(00, 63, 00);
    color_t color3 = rgbConverter(31, 31, 00);

    draw_circle(100, 100, 50, color1);

    draw_text(200, 200, "CLEARING SCREEN", color2);
    sleep_ms(5000000);
    clear_screen();

    draw_text(200, 200, "SLEEPING FOR TEN SECONDS", color2);
    sleep_ms(10000000);
    clear_screen();

    draw_text(200, 200, "DRAWING LINES AND STUFF", color3);
    sleep_ms(5000000);
    clear_screen();

    draw_line(0, 0, 639, 479, color2);
    draw_line(0, 479, 639, 0, color1);
    draw_rect(320, 240, 100, 100, color3);
    
    char str[2];
    char key; 
    draw_text(100, 100, "Press any key to exit", color3);
    while(1) {
        key = getkey();
        if (key != '\0') {
            str[0] = key;
            str[1] = '\0';
            draw_text(200, 200, "KEY PRESSED", color2);
            draw_text(200, 250, str, color1);
            sleep_ms(10000000);
            clear_screen();
            break;
        }
    }
    exit_graphics();
    return 0;
}