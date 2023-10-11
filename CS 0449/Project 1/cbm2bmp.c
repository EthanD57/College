#include <stdio.h>
#include <stdint.h>
#include <stdlib.h>
#include <string.h>
#include "metadata.h"
#include "headers.h"


int main (int argc, char *argv[]){

    if (argc < 2){          //Checks to see if the user entered at least 1 file name
        printf("Error: No file name provided\n");
        return 1;
    }
    
    if (strcmp(argv[1], "--info") == 0){           //If the user request info 
        FILE *cmb;                          //This is the file pointer for the CMB file
        cmb = fopen(argv[2], "rb");         //Opens the second command line arg in read binary mode
        
        
        
        CBMheaderWrite(cmb, &CBM_header); //Function calls to make file look neat
        CBMheaderPrint(&CBM_header);

    }
    else if (strcmp(argv[1], "--bmp-info") == 0){      //If the user request bmp info
        FILE *cmb;                              //Creates a file pointer for the CMB file
        cmb = fopen(argv[2], "rb");             //Opens the second command line arg in read binary mode

        CBMheaderWrite(cmb, &CBM_header);       //Tons of function calls in order to declutter the main function
        bmpDIBWrite(&DIB_header, &CBM_header);
        bmpHeaderWrite(&BMP_header, &DIB_header);
        bmpHeaderPrint(&BMP_header);
        dibHeaderPrint(&DIB_header);
    }
    else if (strcmp(argv[1], "--convert") == 0){        //If the user request to convert to bmp
        FILE *cmb;                              //Creates a file pointer for the CMB file
        cmb = fopen(argv[2], "rb");  

        FILE *bmp;                              //This is making a file pointer for the bmp file
        bmp = fopen(argv[3], "wb");             //And we are naming it the third argument in the command line

        CBMheaderWrite(cmb, &CBM_header);           //We call our functions to fill the structs and convert the file
        bmpDIBWrite(&DIB_header, &CBM_header);
        bmpHeaderWrite(&BMP_header, &DIB_header);
        convert(cmb, &CBM_header, &BMP_header, &DIB_header, bmp);
    }
    else{   
        printf("Error: Invalid command\n");     //If the user enters an invalid command, we print an error
        return 0;
    }

    
    return 0;
}



