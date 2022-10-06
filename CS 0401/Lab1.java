import java.io.*; // I/O
import java.util.*; // Scanner class
 
public class Lab1
{
    public static void main( String args[] ) 
    {
        final double MILES_PER_MARATHON = 26.21875; // i.e 26 miles 285 yards
 
        Scanner input = new Scanner (System.in);
 
        // THE FOLLOWING THREE VARIABLES ARE PRINTED OUT AT THE END OF THE PROGRAM
        double aveMPH=0, aveMinsPerMile=0, aveSecsPerMile=0;
 
        // YOUR JOB IS TO DO WHAT YOU HAVE TO TO TO PUT THE CORRECT VALUES INTO THEM
        // BEFORE THEY GET PRINTED OUT AT THE BOTTOM
 
        System.out.print("Enter marathon time in hrs minutes seconds: "); // i.e. 3 49 37
        // DO NOT WRITE OR MODIFY ANYTHING ABOVE THIS LINE
 
         
        //  - - - - - - - - - - - A L L   Y O U R   C O D E   H E R E - - - - - - - - - - - - - - - 
        int h = input.nextInt();
        int m = input.nextInt();
        int s = input.nextInt();
 
        double total_seconds = 0.0;
         
        total_seconds += ((double)h*3600) + ((double)m*60) + ((double)s);
 
        double secondsPerMiles = total_seconds / MILES_PER_MARATHON;
 
        aveSecsPerMile = secondsPerMiles % 60;

        aveMinsPerMile = (int)(secondsPerMiles / 60);
 
 
        total_seconds /= 3600;
 
        aveMPH = MILES_PER_MARATHON / total_seconds;
 
 
        // DO NOT WRITE OR MODIFY ANYTHING BELOW THIS LINE. JUST LET MY CODE PRINT THE VALUES YOU PUT INTO THE 3 VARS
        System.out.println();
        System.out.format("Average MPH was: %.2f mph\n", aveMPH  );
        System.out.format("Average mile split was %.0f mins %.1f seconds per mile", aveMinsPerMile, aveSecsPerMile );
        System.out.println();
 
    } // END MAIN METHOD
} // EOF