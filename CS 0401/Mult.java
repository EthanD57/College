
public class Mult
{
	public static void main(String [] args)
	{	// CHANGE NOTHING IN MAIN
		System.out.println(  mult( Integer.parseInt(args[0]), Integer.parseInt(args[1]) )  );
	}
//ALL OF MY CODE IS IN THIS METHOD
	static int mult( int a, int b )
	{
		if (b == 0) return 0;

		if (a < 0 && b < 0) return mult(Math.abs(a), Math.abs(b));

		if (b < 0) return (-a + mult(a, ++b));
		
		return (a + mult(a, --b));
		
	}
}
//A STARTER FILE WAS GIVEN FOR THIS CODE. THE CODE SECTION INDICATED WAS MADE BY ME. 