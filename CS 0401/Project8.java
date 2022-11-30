import java.util.*;

public class Project8
{
	public static void main( String args[] )
	{
		do{
			System.out.print("Enter int in range 1..100 inclusive: ");
			Scanner input = new Scanner(System.in);

			try{

				String token = input.next();
				int y = Integer.parseInt(token);

				if (y < 1 || y > 100)
				{
					NumberOutOfRangeException e = new NumberOutOfRangeException();
				}
				else
				{
					System.out.format("Thank you. You entered %d\n", y);
					break;
				}
			}
			catch (Exception e)
			{
				System.out.println("Input was not an integer. ");
			}
		} while (true);
	}
}

class NumberOutOfRangeException extends Exception
{
	public NumberOutOfRangeException()
	{
		System.out.println("Number out of range. Must be in 1...100 inclusive. ");
	}	
}
//A STARTER FILE WAS GIVEN. ONLY THE CODE INSIDE THE DO LOOP AND CLASS "NumberOutOfRangeException" WAS WRITTEN BY ME.