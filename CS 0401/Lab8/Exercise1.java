package Lab8;
/*
	Exercise1.java
	
	- WRITE A SINGLE DO LOOP THAT DOES THE FOLLOWING UNTIL THE USER ENTERS A VALID INPUT FILENAME
	- PROMPT THE USER FOR A FILENAME AND USE TRY CATCH TO RE-PROMPT
	- IF INPUT IS NOT AN INT YOU MUST CLERA THE BUFFER BECUASE THAT BOGUS STRING IS STILL IN THERE 
	- ONCE OUT OF THE LOOP JUST DO THE CODE THAT PRINTS THE TOKENS FROM THE FILE.

*/	
import java.io.*;
import java.util.*;
public class Exercise1
{
	public static void main( String args[] ) 
	{
		if (args.length < 1)
		{
			System.out.println("\nYou must enter an input filename on cmd line!\n");
			System.exit(0);
		}
    boolean x = true;

    do
    {
		try (Scanner input = new Scanner(new File(args[0])))
        {
            while (input.hasNext())
            {
                String token = input.next();
                System.out.println(token);
            }
            x = false;
            break;
        }
        catch (FileNotFoundException e)
        {
            System.out.println("File not found. Please enter a valid filename.");
            Scanner input = new Scanner(System.in);
            String filename = input.next();
            args[0] = filename;
        }
		
    } while (x = true);
		
		
	}
} //A STARTER FILE WAS GIVEN. ONLY THE CODE INSIDE THE DO LOOP WAS WRITTEN BY ME.