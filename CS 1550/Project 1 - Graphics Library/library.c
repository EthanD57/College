#include <sys/mman.h>
#include <unistd.h>
#include <sys/syscall.h>
#include <sys/ioctl.h>
#include <fcntl.h>
#include <linux/fb.h>
#include <termios.h>
#include <sys/select.h>
#include <time.h>
#include "graphics.h"
#include "iso_font.h"

#define SYS_EXIT 1

// Globals
int frameBuffer;
void* mappedBuffer;
int bufferSize;
struct fb_var_screeninfo vinfo;
struct fb_fix_screeninfo finfo;     


void init_graphics(){
	frameBuffer = open("/dev/fb0", O_RDWR);	//Opens the framebuffer in read/write mode or exits if it fails
	if (frameBuffer < 0){
		syscall(SYS_EXIT, 1);
	}

	ioctl(frameBuffer, FBIOGET_VSCREENINFO, &vinfo);	//Gets the variable screen info and stores it in vinfo (vertical resolution, horizontal resolution, etc)
	ioctl(frameBuffer, FBIOGET_FSCREENINFO, &finfo);	//Gets the fixed screen info and stores it in finfo (line length, length of memory mapped buffer, etc) 

	bufferSize = vinfo.yres_virtual * finfo.line_length;	//Calculates the size of the buffer 
 
	mappedBuffer = (void*) mmap(NULL, bufferSize, PROT_READ | PROT_WRITE, MAP_SHARED, frameBuffer, 0);	//Maps the framebuffer into memory with read and write perms
	if (mappedBuffer == NULL){	//If the mapping failed, exit
		syscall(SYS_EXIT, 1);
	}


	// Disable keyboard echo and buffering keypresses
	struct termios term;  //termios is a struct from termios.h. Conventionally named term
	ioctl(0, TCGETS, &term);    
	term.c_lflag &= ~ICANON;    //unset canonical mode
	term.c_lflag &= ~ECHO;      //unset echo
	ioctl(0, TCSETS, &term);
}

void exit_graphics(){
	struct termios term;       
	ioctl(0, TCGETS, &term);    
	term.c_lflag |= ICANON;    //set canonical mode
	term.c_lflag |= ECHO;      //set echo
	ioctl(0, TCSETS, &term);

	munmap(mappedBuffer, bufferSize);
	close(frameBuffer);
}

char getkey(){
	fd_set readfds;			//File descriptor set
	FD_ZERO(&readfds);		//Clears the file descriptor set. MACRO from sys/select.h
	FD_SET(0, &readfds);	//Sets the file descriptor 0 (stdin) in the file descriptor set

	struct timeval time;	//Initializes the timeval struct from time.h
	time.tv_sec = 0;			
	time.tv_usec = 0 ;	//Don't sleep at all for non-blocking. We are sleeping manually with nanosleep

	int keyPressed = select(1, &readfds, NULL, NULL, &time);	//Checks if a key was pressed in the file descriptor set within the time limit 

	if (keyPressed > 0){	//If a key was pressed, read it. Otherwise return -1
		char c;
		if (read(0, &c, 1) == 1) return c;
	}
	return '\0';
}

void sleep_ms(long ms){
	struct timespec reqSleep;	 //Initializes the timespec struct from time.h

	reqSleep.tv_sec = 0;				//We aren't interested in seconds, so it is set to 0 and we convernt the ms to ns
	reqSleep.tv_nsec = ms * 1000000;

	nanosleep(&reqSleep, NULL);		//Sleep for the specified time and don't handle errors
}

void clear_screen(){
	write(1, "\033[2J", 4);		//Writes the escape sequence to clear the screen
}

void draw_pixel(int x, int y, color_t color){
	if (x < 0 || x >= vinfo.xres || y < 0 || y >= vinfo.yres){	//If the pixel is out of bounds, return
		return;
	}
	color_t* location = (color_t*) mappedBuffer;
	int pixelOffset = (y * finfo.line_length) / 2 + x;
	location[pixelOffset] = color;	//Set the pixel at the given location to the given color
}

void draw_rect(int x1, int y1, int width, int height, color_t color) {
	int x, y;

	for (x = x1; x < x1 + width; x++) {    // Draw the bottom edge
		draw_pixel(x, y1, color);
	}

	for (y = y1; y < y1 + height; y++) {    // Draw the right edge
		draw_pixel(x1 + width, y, color);
	}

	for (x = x1; x < x1 + width; x++) {     // Draw the top edge
		draw_pixel(x, y1 + height, color);
	}

	for (y = y1; y < y1 + height; y++) {    // Draw the left edge
		draw_pixel(x1, y, color);
	} 
}

void draw_text(int x, int y, const char *text, color_t c) {
    int charIndex, row, col;
    unsigned char *frontChar_ptr;
    unsigned char fontRow;
    int len = 0;

    while (text[len] != '\0') {
        len++;
    }

    for (charIndex = 0; charIndex < len; charIndex++) {
        frontChar_ptr = &iso_font[(text[charIndex] - ISO_CHAR_MIN) * ISO_CHAR_HEIGHT];  // Get the character from the font

        for (row = 0; row < ISO_CHAR_HEIGHT; row++) {  // Iterates through the rows of the character top to bottom
            fontRow = *frontChar_ptr++;

            for (col = 0; col < 8; col++) {  // Iterates through the columns of the character left to right       
                if (fontRow & (1 << col)) {  // If the bit is set, draw a pixel
                    draw_pixel(x + col + (charIndex * 8), y + row, c);
                }
            }
        }
    }
}