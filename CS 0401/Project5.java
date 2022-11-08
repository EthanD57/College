

public class Project5
{
	public static void main( String[] args )
	{
		// T R I S T A R S 
		// assume you are just given a single number that says how many rows of stars are in your triangle
		// an input of 1 means a singe row - the top row
		//      *     <= the answer is 1
		// an input of 2 means the top tow and the second row 
		//      *	
		//     * *    <= the answer is 3 since now you have 3 stars
		// an input of 3 means the top tow and the next two rows
		//      *	
		//     * *	
		//    * * *   <= the answer is 6 since now you have 6 stars
		// do you see the pattern?  code it up recursively!
		
		
		int rows = 5;
		System.out.format("%d row triangle tree contains %d stars\n", rows, triStars(rows) );
		
		// S U M D I G I T S
		int number = 12345;
		System.out.format("sum of digits in %d = %d\n", number, sumDigits( number ) );
		
		// C O U N T 7 S
		number = 713274772;
		System.out.format("%d occurances of digit 7 in %d\n", count7s(number), number );
		
		// C O U N T 8 S -but- there is a twist! Any 8 with an 8 to its left counts as TWO 8s
		number = 82338828;
		System.out.format("%d occurances** of digit 8 in %d\n", count8s(number), number );

		// P O W E R N
		int base=2,exponent=8;
		System.out.format("%d to the power %d = %d\n", base, exponent, powerN(base,exponent) );	
	

		// I S S O R T E D 
		// perturb values as needed to test for your own benefit on an unsorted array (we will test on an unserted too)
		int[] array = { 7, 8, 12, 20, 21, 22, 37, 41, 55, 60, 65, 74, 83, 84, 87 };
		int startingAt=0;
		boolean isSorted = isSorted( array, startingAt, array.length );
		System.out.print( "array: ");
		for ( int i=0 ; i<array.length ; ++i ) System.out.print( array[i] + " " );
		if (isSorted)
			System.out.println(" is SORTED" );	
		else
			System.out.println(" is NOT SORTED" );	


		// P A L I N D R O M E
		String s = "stanleyyelnats"; // try with several differnt values that are or not palindromes	
		if ( isPalindrome( s, 0, s.length()-1 ) )
			System.out.format("%s IS a palindrome\n", s );	
		else
			System.out.format("\n%s NOT a palindrome\n", s ); 	
		
	} 

	//ALL OF THE CODE IN THE BELOW METHODS IS MY CODE -----------------------------------------------

	// count stars in a triangle using # of rows as input
	static int triStars(int rows)  
	{	
		if (rows != 0) return rows + triStars(rows-1);
		return rows;
	}
	// given a number return the sum of the digits
	static int sumDigits(int n) 
	{	
		if (n != 0) return n%10 + sumDigits(n/10);
		return n;
	}

	// given a number compute the number of 7's in that number 
	static int count7s(int n) 
	{	
		if (n%10 == 7 && n != 0)
			return 1 + count7s(n/10);
		else if (n == 0)
			return 0;
		else
			return count7s(n/10);
	}
	// given a number count the number of 8 but if an 8 has another 8 to its immdiate left count it as 2 
	// the number 8802388 will return a count of 5  even tho only 4 literal 8 are there
	static int count8s(int n) 
	{	
		
		if (n == 0)
		return n;

		if (n%10 == 8)
		{
			if(n%100 ==8 && n%10 == 8)
			{
				return 2 + count8s(n/10);
			}
			else
			{
				return 1 + count8s(n/10);
			}
		}
			return count8s(n/10);
	}
	
	//compute base to the power n
	static int powerN(int base, int n) 
	{	
		if (n != 0) return base * powerN(base, n-1);
		return 1;
	}
	// return true only if the array is sorted 
	static boolean isSorted(int array[], int i, int count ) 
	{	
		if (array[i] < array[i+1] && i == count-2) return true;

		if (array[i] < array[i+1] ) return (true && isSorted(array, i+1, count));
		
		return false;
	}

	// return true if string is palindrome
	static boolean isPalindrome(String s, int lo, int hi ) 
	{	
		if (lo == hi) return true;
		if (s.charAt(lo) != s.charAt(hi)) return false;
		if (lo == hi) return isPalindrome(s, lo+1, hi-1);
		return true;
	}
}
//A STARTER FILE WAS GIVEN FOR THIS CODE. THE CODE SECTION INDICATED WAS MADE BY ME. 
