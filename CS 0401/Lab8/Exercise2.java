import java.util.*;

public class Exercise2
{
	public static void main( String args[] )
	{
		System.out.print("Enter int in range 1..100 inclusive: ");
		do{
			Scanner input = new Scanner(System.in);

			try{

				String token = input.next();
				int y = Integer.parseInt(token);

				if (y < 1 || y > 100)
				{
					throw new ArithmeticException("Number out of range");
				}
				else
				{
					System.out.format("Thank you. You entered %d\n", y);
					break;
				}
			}
			catch (Exception e)
			{
				System.out.print("Invalid input. Please enter an integer in the range 1..100 inclusive. ");
			}
		} while (true);
	}
		
		
}
//A STARTER FILE WAS GIVEN. ONLY THE CODE INSIDE THE DO LOOP WAS WRITTEN BY ME.