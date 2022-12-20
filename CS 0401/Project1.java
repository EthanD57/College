
import java.util.*;

public class Project1 {
    public static void main(String args[]) {
        Scanner kbd = new Scanner(System.in);

        final int LOW_RATE = 30; 
        final int HIGH_RATE = 50; 
        final int HIGHRATE_OVERLIMIT = 20; 
        
        final int UNDERAGE_SPEEDER_FINE = 300; 
        String firstName, lastName; 
        int age; 
        int speedLimit = 0, driverSpeed = 0, mphOver = 0; 
        int rate; 
        int baseFine = 0, zoneFine = 0, underAgeFine = 0; 
        boolean inZone = false;

        System.out.print("Driver's first name? ");
        firstName = kbd.next();

        System.out.print("Driver's last name? ");
        lastName = kbd.next();

        System.out.print("Driver's speed in mph? ");
        driverSpeed = kbd.nextInt();

        System.out.print("Speed Limit? ");
        speedLimit = kbd.nextInt();

        System.out.print("Driver's age in yrs? ");
        age = kbd.nextInt();

        System.out.print("Did violation occur in construction zone? (true/false) ");
        inZone = kbd.nextBoolean(); 

        kbd.close();

        mphOver = driverSpeed - speedLimit;
        int mph_multi = mphOver / 5;

        if ((age > HIGHRATE_OVERLIMIT || age == HIGHRATE_OVERLIMIT)) {
            if (driverSpeed - speedLimit > 20) {
                rate = HIGH_RATE;
            } else {
                rate = LOW_RATE;
            }
        } else {
            underAgeFine = UNDERAGE_SPEEDER_FINE;

            if (driverSpeed - speedLimit > 20) {
                rate = HIGH_RATE;
            } else {
                rate = LOW_RATE;
            }
        }

        baseFine = mph_multi * rate;
        if (inZone) {
            zoneFine = baseFine;
        }

        // END OF MY CODE --------------------------------

        System.out.println("\n" +
                "Driver Name: " + lastName + ", " + firstName + "\n" +
                "Driver Age: " + age + "\n" +
                "Speed Limit: " + speedLimit + "\n" +
                "Actual Speed: " + driverSpeed + "\n" +
                "Mph over limit: " + mphOver + "\n" +
                "Base Fine: $" + baseFine + "\n" +
                "Construction Zone Fine: $" + zoneFine + "\n" +
                "Underage Fine: $" + underAgeFine + "\n" +
                "Total Fine: $" + (baseFine + zoneFine + underAgeFine));

    } 
}
// A STARTER FILE WAS GIVEN FOR THIS CODE. THE CODE SECTION INDICATED WAS MADE
// BY ME.