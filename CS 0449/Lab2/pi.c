#include <stdlib.h>

double estimate_pi(int number_of_samples) {
  double isInsideCircle = 0;

    for(int i = 0; i<number_of_samples; i++) {
        double x = (double)rand();
        double y = (double)rand();


        x = x / RAND_MAX;
        y = y / RAND_MAX;

        x = x-0.5;
        y = y-0.5;

        x *= 4;
        y *= 4;

        if(x*x + y*y <= 4) {
            isInsideCircle++;
        }

            
        }
        double pi = 4 * (isInsideCircle / (double)number_of_samples);
        return pi;
}
