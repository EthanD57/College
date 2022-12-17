import java.io.*;
import java.util.*;

public class LinkedListTester
{
	public static void main( String args[] ) throws Exception
	{
		LinkedList myList = new LinkedList( args[0]);
		System.out.println( "myList loaded from " + args[0] );
		System.out.println("myList: (contains " + myList.size() + " elements) " +  myList ); // invokes toString
		BufferedReader infile = new BufferedReader( new FileReader( args[1] ) );
		System.out.println( "Searching myList for the following words from " + args[1] );
		while ( infile.ready() )
		{	
			String word = infile.readLine();
			if ( myList.contains( word ) )
				System.out.println( word + "\tfound" );
			else
				System.out.println( word + "\tNOT found" );
		}
	} // END MAIN
} // EOF