#include "graphics.h"

int main(int argc, char** argv)
{
	int i;
	clear_screen();
	init_graphics();

	char key;
	int x = (640-20)/2;
	int y = (480-20)/2;

	// Draw some initial text
	draw_text(10, 10, "Move the square with WASD", rgbConverter(00, 63, 00)); // Green text
	draw_text(10, 30, "Press Q to quit", rgbConverter(31, 0, 0)); // Red text

	do
	{
		//draw a black rectangle to erase the old one
		clear_screen();
		draw_rect(x, y, 20, 20, 0);
		key = getkey();
		if(key == 'w') y-=10;
		else if(key == 's') y+=10;
		else if(key == 'a') x-=10;
		else if(key == 'd') x+=10;
		//draw a blue rectangle
		draw_rect(x, y, 20, 20, rgbConverter(0, 0, 31));

		// Draw the current position of the square
		char position[30];
		snprintf(position, sizeof(position), "Square position: (%d, %d)", x, y);
		draw_text(10, 50, position, rgbConverter(31, 31, 31)); // White text

		sleep_ms(20);
	} while(key != 'q');

	exit_graphics();

	return 0;
}