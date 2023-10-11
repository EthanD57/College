#ifndef METADATA_H
#define METADATA_H

#include <stdint.h>
#pragma pack(1)

/// CBM HEADER --------------------------------
struct CBMheader{
    //These are just all of the numbers he told us to initialize
    uint16_t format_identifier;
    uint32_t width;
    uint32_t height;
    uint32_t num_colors;
    uint32_t pallet_offset;
    uint32_t image_offset;

    //These are the arrays for the colors
    struct red{
        uint8_t intensity [256];
    } red;
    struct green{
        uint8_t intensity [256];
    } green;
    struct blue{
        uint8_t intensity [256];
    } blue;
} CBM_header;


/// BMP INFO HEADER --------------------------------
struct BMPheader{
    //These are just all of the numbers he told us to initialize
    uint16_t format_identifier;
    uint32_t size;
    uint16_t reserved;
    uint16_t reserved2;
    uint32_t offset;
} BMP_header;

struct DIBheader{
    //These are just all of the numbers he told us to initialize
    uint32_t size_DIP;
    uint32_t width;
    uint32_t height;
    uint16_t num_planes;
    uint16_t bits_per_pixel;
    uint32_t compression;
    uint32_t image_size;
    uint32_t x_pixels_per_meter;
    uint32_t y_pixels_per_meter;
    uint32_t num_colors;
    uint32_t num_important_colors;
} DIB_header;
//This supposedly packs the structs so that they are the correct size
#pragma pack(4)
#endif