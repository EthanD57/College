package Lab1;
public class Deck
{
	private int[] deck;
	private final int MAX_DECK_SIZE = 20;
	public Deck( int numCards )
	{	if ( numCards%2 != 0 || numCards > MAX_DECK_SIZE ) 
		{
			System.out.format("\nINVALID DECK SIZE: (" + numCards + "). Must be an small even number <= %d\n", MAX_DECK_SIZE);
			System.exit(0);
		}
		deck = new int[numCards];
		for (int i=0 ; i < numCards; i++ )
			deck[i] = i;

	}
	
	public String toString()
	{
		String deckStr = "";
		for ( int i=0 ; i < deck.length ; ++i )
			deckStr += deck[i] + " ";
		return deckStr;
	}

	public void inShuffle()
	{
		int[] temp = new int[deck.length];
		int increment = 0;
		for (int i = 0; i < deck.length-1; i+=2)
		{
			temp[i] = deck[(deck.length/2)+(increment)];
			temp[i+1] = deck[increment++];
		}

		for (int i = 0; i < deck.length; i++)
		{
			deck[i] = temp[i];
		}
	}
	
	public void outShuffle()
	{
		int[] temp = new int[deck.length];
		int increment = 0;
		for (int i = 0; i < deck.length-1; i+=2)
		{
			temp[i] = deck[increment];
			temp[i+1] = deck[(deck.length/2)+(increment++)];
		}

		for (int i = 0; i < deck.length; i++)
		{
			deck[i] = temp[i];
		}
	}
	
	public boolean inSortedOrder()
	{
		for (int i = 0; i < deck.length-1; i++)
		{
			if (deck[i] > deck[i+1])
				return false;
		}
		return true;
	}
}	

/*This is from a starter file given as part of Lab 1 for CS 0445. I wrote all of the code in the InSortedOrder() method, 
the OutShuffle() method, and the InShuffle() method. I did not write the rest of the code. Credit for all other code 
goes to Timothy Hoffman.
Please remove the package statement before running*/