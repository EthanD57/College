#include <stdint.h>
#include <stdio.h>
#include "metadata.h"
#include <stdlib.h>

void CBMheaderWrite(FILE *cmb, struct CBMheader *CBM_header) {

    uint32_t buffer;        //Just a variable to store the contents of fread

    fread(&buffer, 2, 1, cmb);      //We read in the magic number

    CBM_header->format_identifier = buffer;     //We store the magic number in the struct

    if (CBM_header->format_identifier != 29769) {   //If the magic number is not 29769, then the format is not supported
        printf("The format is not supported.\n");
        exit(-1);       //This exits the program instantly 
    }

    uint32_t *header_fields[] = {       //These are all of our struct fields that we need to fill
        &CBM_header->width,
        &CBM_header->height,
        &CBM_header->num_colors,
        &CBM_header->pallet_offset,
        &CBM_header->image_offset
    };

    for (int i = 0; i < 5; i++) {       //Iterates through all of the fields and stores the data from the file 
        fread(&buffer, sizeof(buffer), 1, cmb);
        *header_fields[i] = buffer;
    }

    // Now we read in the pallet data

    fseek(cmb, CBM_header->pallet_offset, SEEK_SET);    //Seek to the start of the pallet


    for (int i = 0; i < CBM_header->num_colors; i++) {  //Iterates through all of the colors and stores the data from the file
        fread(&buffer, 1, 1, cmb);
        CBM_header->red.intensity[i] = buffer;
        fread(&buffer, 1, 1, cmb);
        CBM_header->green.intensity[i] = buffer;
        fread(&buffer, 1, 1, cmb);
        CBM_header->blue.intensity[i] = buffer;
    }
}

void CBMheaderPrint(struct CBMheader *CBM_header) {     //Prints the CBM header data
    
    printf("=== CBM Header ===\n");

    printf("Magic: %#x\n", CBM_header->format_identifier);
    printf("Width: %d\n", CBM_header->width);
    printf("Height: %d\n", CBM_header->height);
    printf("Number of colors: %d\n", CBM_header->num_colors);
    printf("Color array offset: %d\n", CBM_header->pallet_offset);
    printf("Image array offset: %d\n", CBM_header->image_offset);

    printf("\n=== Palette (R, G, B) ===\n");

    for (int i = 0; i < CBM_header->num_colors; i++) {  //Iterates through all of the colors and prints the data
    printf("Color %d: (%d, %d, %d)\n", i, CBM_header->red.intensity[i], CBM_header->green.intensity[i], CBM_header->blue.intensity[i]);
    }
}

//NOW WE DO THE BMP INFO ---------------------------------------------
void bmpHeaderWrite(struct BMPheader *BMP_header, struct DIBheader *DIB_header) {   //Writes the BMP header data to the file
    BMP_header->format_identifier = 0x4D42; //This is equal to "BM" in ASCII
    BMP_header->size = 54 + (DIB_header->width * DIB_header->height * 3) + (DIB_header->height * (DIB_header->width % 4));  //This is the size of the file including padding
    BMP_header->reserved = 0;   //These are just all of the numbers he told us to initialize
    BMP_header->reserved2 = 0;  //These are just all of the numbers he told us to initialize
    BMP_header->offset = 54; //This is the offset of the image data
}

void bmpDIBWrite (struct DIBheader *DIB_header, struct CBMheader *CBM_header){
    DIB_header->size_DIP = 40;  //DIB header size, I spelled it wrong in the struct and I'm too scared to change it all
    DIB_header->width = CBM_header->width;  //This is equal to the width of the image for the CBM
    DIB_header->height = CBM_header->height;  //This is equal to the height of the image for the CBM
    DIB_header->num_planes = 1; //This is always 1 for us
    DIB_header->bits_per_pixel = 24;    //This is always 24 for us
    DIB_header->compression = 0;    //These are all going to always be 0 for this project
    DIB_header->image_size = 0;
    DIB_header->x_pixels_per_meter = 0;
    DIB_header->y_pixels_per_meter = 0;
    DIB_header->num_colors = 0;
    DIB_header->num_important_colors = 0;
}

void bmpHeaderPrint(struct BMPheader *BMP_header){      //Now we print all of the data in the header
    printf("=== BMP Header ===\n");

    printf("Type: %c%c\n", BMP_header->format_identifier, BMP_header->format_identifier >> 8);
    printf("Size: %d\n", BMP_header->size);
    printf("Reserved 1: %d\n", BMP_header->reserved);
    printf("Reserved 2: %d\n", BMP_header->reserved2);
    printf("Image offset: %d\n", BMP_header->offset);
}

void dibHeaderPrint(struct DIBheader *DIB_header){  //Now we print all of the data in the DIB header
    printf("\n=== DIB Header ===\n");

    printf("Size: %d\n", DIB_header->size_DIP);
    printf("Width: %d\n", DIB_header->width);
    printf("Height: %d\n", DIB_header->height);
    printf("# color planes: %d\n", DIB_header->num_planes);
    printf("# bits per pixel: %d\n", DIB_header->bits_per_pixel);
    printf("Compression scheme: %d\n", DIB_header->compression);
    printf("Image size: %d\n", DIB_header->image_size);
    printf("Horizontal resolution: %d\n", DIB_header->x_pixels_per_meter);
    printf("Vertical resolution: %d\n", DIB_header->y_pixels_per_meter);
    printf("# colors in palette: %d\n", DIB_header->num_colors);
    printf("# important colors: %d\n", DIB_header->num_important_colors);
}

//NOW WE DO THE CONVERT ---------------------------------------------

void convert (FILE *cmb, struct CBMheader *CBM_header, struct BMPheader *BMP_header, struct DIBheader *DIB_header, FILE *bmp){
    
    //Write the BMP header data to the file
    fwrite(&BMP_header->format_identifier, 2, 1, bmp);
    fwrite(&BMP_header->size, 4, 1, bmp);
    fwrite(&BMP_header->reserved, 2, 1, bmp);
    fwrite(&BMP_header->reserved2, 2, 1, bmp);
    fwrite(&BMP_header->offset, 4, 1, bmp);

    //Write the DIB header data to the file
    fwrite(&DIB_header->size_DIP, 4, 1, bmp);
    fwrite(&DIB_header->width, 4, 1, bmp);
    fwrite(&DIB_header->height, 4, 1, bmp);
    fwrite(&DIB_header->num_planes, 2, 1, bmp);
    fwrite(&DIB_header->bits_per_pixel, 2, 1, bmp);
    fwrite(&DIB_header->compression, 4, 1, bmp);
    fwrite(&DIB_header->image_size, 4, 1, bmp);
    fwrite(&DIB_header->x_pixels_per_meter, 4, 1, bmp);
    fwrite(&DIB_header->y_pixels_per_meter, 4, 1, bmp);
    fwrite(&DIB_header->num_colors, 4, 1, bmp);
    fwrite(&DIB_header->num_important_colors, 4, 1, bmp);
    
    
    fseek(cmb, CBM_header->image_offset, SEEK_SET); //Seek to the start of the image

    uint8_t buffer;     //Just a variable to store the contents of fread

    //An array to store all of the color pallet index starting at the top left of the image going to bottom right aka the pixels
    uint8_t CBM_pixels[CBM_header->height][CBM_header->width];
    for (int i = 0; i < CBM_header->height; i++) {
        for (int j = 0; j < CBM_header->width; j++) {
            fread(&buffer, 1, 1, cmb);
            CBM_pixels[i][j] = buffer;
        }
    }

    //Stores the colors in the color pallet in order in BGR format in an array
    uint8_t BMP_colors[CBM_header->num_colors][3];
    for (int i = 0; i < CBM_header->num_colors; i++) {
        BMP_colors[i][0] = CBM_header->blue.intensity[i];
        BMP_colors[i][1] = CBM_header->green.intensity[i];
        BMP_colors[i][2] = CBM_header->red.intensity[i];
    }
    
    int padding = (4 - (CBM_header->width % 4)) % 4; //Calculates the padding needed for the BMP file
    
    for (int i = CBM_header->height; i >= 0; i--) {  //Iterates through all of the pixels and writes the color data to the file
        for (int j = 0; j < CBM_header->width; j++) {
            fwrite(&BMP_colors[CBM_pixels[i][j]], 3, 1, bmp);   //This writes the entire row of bgr values to the file at once
        }
        // Write any necessary padding bytes
        for (int k = 0; k < padding; k++) { //Finally, we pad the rows with 0x00 until the padding is complete
            fputc(0x00, bmp);
        }
    }
//Then we are done. And it works mostly :)


}
    


    

