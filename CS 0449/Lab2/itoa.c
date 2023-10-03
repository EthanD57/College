#include <stdlib.h>
/**
    Integer TO Array
    Converts an int, to a string (aka an array of chars terminated by character '\0')
        If base is 10, negative numbers are prefixed with a - sign.
        All other bases are assumed to be unsigned.
        str WILL be enough to hold the number! Including the nul terminator `\0`
*/

void my_itoa( int value, char *str, int base ) {
    // Your implementation
        int isNegative = 0;
        int holder;
        int length = 0;
        int index = 0;

//BASE 10 CASE
        if (base == 10)
        {
            if (value < 0)
            {
                str[index] = '-';
                index++;
                value = -value;
            }
        

        holder = value;
        while(holder != 0)
        {
            holder/=10;
            length++;
        }
        length--;

        for (int i = (length+index); i >= index; i--)
        {   
            str[i] = (value % 10) + '0';
            value /= 10;
        }
        str[length+index+1] = '\0';
        }

//BASE 2 CASE

    if (base == 2) {

        if (value < 0) {
            unsigned u_val = value;
            unsigned holder = value;

            while (holder != 0)
            {
                holder /= 2;
                length++;
            }
            length--;

            for (int i = (length); i >= 0; i--)
            {
                str[i] = (u_val % 2) + '0';
                u_val /= 2;
            }
            str[length + 1] = '\0';
        }

        else{
            holder = value;
            while (holder != 0)
            {
                holder /= 2;
                length++;
            }
            length--;

            for (int i = (length); i >= 0; i--)
            {
                str[i] = (value % 2) + '0';
                value /= 2;
            }
            str[length + 1] = '\0';

        }
    }

//BASE 16 CASE   
    if (base == 16)
        {
            if (value < 0) {
            unsigned u_val = value;
            unsigned holder = value;

            while (holder != 0)
            {
                holder /= 16;
                length++;
            }
            length--;

            for (int i = (length); i >= 0; i--)
            {
                if ((u_val % 16) < 10)
                {
                    str[i] = (u_val % 16) + '0';
                }
                else
                {
                    str[i] = (u_val % 16) + 'A' - 10;
                }
                u_val /= 16;
            }
            str[length + 1] = '\0';
        }

        else{
            holder = value;
            while (holder != 0)
            {
                holder /= 16;
                length++;
            }
            length--;

            for (int i = (length); i >= 0; i--)
            {
                if ((value % 16) < 10)
                {
                    str[i] = (value % 16) + '0';
                }
                else
                {
                str[i] = (value % 16) + 'A' - 10;
                }
                value /= 16;
            }
            str[length + 1] = '\0';

        }
        }

//BASE 8 CASE
    if (base == 8)
        {
            if (value < 0) {
            unsigned u_val = value;
            unsigned holder = value;

            while (holder != 0)
            {
                holder /= 8;
                length++;
            }
            length--;

            for (int i = (length); i >= 0; i--)
            {
                str[i] = (u_val % 8) + '0';
                u_val /= 8;
            }
            str[length + 1] = '\0';
        }

        else{
            holder = value;
            while (holder != 0)
            {
                holder /= 8;
                length++;
            }
            length--;

            for (int i = (length); i >= 0; i--)
            {
                str[i] = (value % 8) + '0';
                value /= 8;
            }
            str[length + 1] = '\0';

        }
        }
    }
    // No main function