#include <sys/mman.h>
#include <unistd.h>
#include <sys/syscall.h>
#include <sys/ioctl.h>
#include <fcntl.h>
#include <linux/fb.h>
#include <termios.h>

#define SYS_OPEN 5
#define SYS_CLOSE 6
#define SYS_WRITE 4
#define SYS_READ 3
#define SYS_EXIT 1
#define SYS_MMAP 9


// Globals
int frameBuffer;
void* mappedBuffer;
typedef unsigned short color_t;
int bufferSize;


void init_graphics(){
    frameBuffer = open("/dev/fb0", O_RDWR);
    if (frameBuffer < 0){
        syscall(SYS_EXIT, 1);
    }

    struct fb_var_screeninfo vinfo;     //Note: fb_var_screeninfo is a struct from fb.h, you didn't define it
    ioctl(frameBuffer, FBIOGET_VSCREENINFO, &vinfo);

    struct fb_fix_screeninfo finfo;     //Note: fb_var_screeninfo is a struct from fb.h, you didn't define it
    ioctl(frameBuffer, FBIOGET_FSCREENINFO, &finfo);

    bufferSize = vinfo.yres_virtual * finfo.line_length;
 
    mappedBuffer = (void*) syscall(SYS_MMAP, NULL, bufferSize, PROT_READ | PROT_WRITE, MAP_SHARED, frameBuffer, 0);
    if (mappedBuffer == NULL){
        syscall(SYS_EXIT, 1);
    }
    

    // Disable keyboard echo and buffering keypresses
    struct termios term;  //Note: termios is a struct from termios.h, you didn't define it
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

	syscall(SYS_MMAP, mappedBuffer, 0, 0, 0, 0, 0);
	syscall(SYS_CLOSE, frameBuffer);
}

