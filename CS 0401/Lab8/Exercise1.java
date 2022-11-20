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