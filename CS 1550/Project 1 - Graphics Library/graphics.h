typedef unsigned short color_t;

#define rgbConverter(r, g, b) ((color_t) (((r & 0x1F) << 11) | ((g & 0x3F) << 5) | (b & 0x1F))) //Converts RGB values to a color_t value

void init_graphics();
void exit_graphics();
char getkey();
void sleep_ms(long ms);
void clear_screen();
void draw_pixel(int x, int y, color_t color);
void draw_rect(int x1, int y1, int width, int height, color_t color);
void draw_text(int x, int y, const char *text, color_t c);
void draw_circle(int x_center, int y_center, int radius, color_t color);
int abs(int x);
void draw_line(int x1, int y1, int x2, int y2, color_t c);

