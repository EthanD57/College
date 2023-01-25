package Project1;
public class TopCardPlacer
{
	public static void main(String[] args) 
	{	
		if ( args.length < 2 ) 
		{
			System.out.println("Must enter two cmd args: deck size (even number) and postion to place top card.\n");
			System.exit(0);
		}
		Deck deck = new Deck( Integer.parseInt( args[0] ) ); // starts out in sorted order 1 2 3 4 5 . . .
		int placementIndex = Integer.parseInt( args[1] );
		
		String bitString = deck.toBitString( placementIndex ); 
		System.out.format( "placement index %d is %s in binary\n", placementIndex, bitString );
		
		for ( int i=0 ; i<bitString.length() ; ++i )
		{
			if (bitString.charAt(i) == '1')
			{	deck.inShuffle();
				System.out.println( "1: " + deck );
			}
			else
			{	deck.outShuffle();
				System.out.println( "0: " + deck );
			}
		} 		
	} // END MAIN
}
//This is a starter file intended to run with Deck.java as part of Project 1 for CS 0445. I did NOT write this code.
//Please remove the package statement before attempting to run the code. 