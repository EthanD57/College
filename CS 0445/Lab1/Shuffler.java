package Lab1;
public class Shuffler
{
	public static void main(String[] args) 
	{	
		if ( args.length < 1 ) 
		{
			System.out.println("Must enter deck size (even number) on cmd line\n");
			System.exit(0);
		}
		Deck deck = new Deck( Integer.parseInt( args[0] ) ); // starts out in sorted order 1 2 3 4 5 . . .
		int numShuffles=0;
		System.out.println("IN-SHUFFLING:");
		System.out.format( "%2d shuffles: %s\n", numShuffles, deck );
		do // IN-SHUFFLE UNTIL DECK IS BACK IN ORIGINAL (SORTED) ORDER
		{
			deck.inShuffle();
			++numShuffles;
			System.out.format( "%2d shuffles: %s\n", numShuffles, deck ); 
		} while ( !deck.inSortedOrder() );	

		System.out.println("OUT-SHUFFLING:");
		deck = new Deck( Integer.parseInt( args[0] ) ); // starts out in sorted order 1 2 3 4 5 . . .
		numShuffles=0;
		System.out.format( "%2d shuffles: %s\n", numShuffles, deck );
		do // OUT-SHUFFLE UNTIL DECK IS BACK IN ORIGINAL (SORTED) ORDER
		{
			deck.outShuffle();
			++numShuffles;
			System.out.format( "%2d shuffles: %s\n", numShuffles, deck ); 
		} while ( !deck.inSortedOrder() );	
		
	}
}
//I DID NOT WRITE THIS CODE. ALL CREDIT GOES TO TIMOTHY HOFFMAN.
//You must run this code to use Deck.java
//Please remove the package statement before running
