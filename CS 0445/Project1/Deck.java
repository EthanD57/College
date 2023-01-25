package Project1;
public class Deck
{
	private int[] deck;
	private final int MAX_DECK_SIZE = 30;
	public Deck( int numCards )
	{	if ( numCards%2 != 0 || numCards > MAX_DECK_SIZE ) 
		{
			System.out.format("\nINVALID DECK SIZE: (" + numCards + "). Must be an small even number <= %d\n", MAX_DECK_SIZE);
			System.exit(0);
		}
		deck = new int[numCards];
		for ( int i=0 ; i<numCards ; i++ ) deck[i] = i;
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
	
	public String toBitString( int n ) 
	{
		if (n == 0) return "";

		int Length = (int)(Math.log(n)/Math.log(2) + 1);
		int[] bitArray = new int[Length];
		for (double i = 0; i < Length; i++)
		{
			double temp = (Math.pow(2.0, (Length - 1) - i));
			if(n - temp == 0)
			{
				bitArray[(int)i] = 1;
				n = 0;
				break;
			}
			else if(n - temp > 0)
			{
				bitArray[(int)i] = 1;
				n -= temp;
			}
			else bitArray[(int)i] = 0;
		}
		String bitString = "";
		for (int x : bitArray)
		{
			bitString += x;
		}
		return bitString;
		
	}
	
}
//This is from a starter file writen by Tim Hoffman for CS 0445. I wrote the code inside of "toBitString" and "inShuffle" and "outShuffle".
//Please remove the package statement before attempting to run the code.