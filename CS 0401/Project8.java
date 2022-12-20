import java.util.*;

public class Project8
{
	public static void main( String args[] )
	{
		do{
			System.out.print("Enter int in range 1..100 inclusive: ");
			Scanner input = new Scanner(System.in);

			try{

				int token = input.nextInt();

				if (token < 1 || token > 100)
				{
					throw new NumberOutOfRangeException();
				}
				else
				{
					System.out.format("Thank you. You entered %d\n", token);
					break;
				}
			}
			catch (NumberOutOfRangeException e)
			{
				
			}
			catch (InputMismatchException e)
			{ 
				System.out.println("Input was not an integer");
			}
			catch (Exception e)
			{
				System.out.println(e);
				System.exit(0);
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