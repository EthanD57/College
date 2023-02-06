import java.io.*;


public class LinkedListTester
{
	public static void main( String args[] ) throws Exception
	{
		if ( args.length < 2 )
			die( "FATAL ERROR: must enter two filenames on command line.\n   java LinkedListTester set1.txt  set2.txt\n" );
		LinkedList<String> myList = new LinkedList<String>();
		// READ IN THE FILE NAMED BY args[0]
		BufferedReader infile0 = new BufferedReader( new FileReader( args[0] ) );
		while ( infile0.ready() )
				myList.insertAtTail( infile0.readLine() );
		infile0.close();

		System.out.println( "myList loaded from " + args[0] );
		System.out.println("myList: (contains " + myList.size() + " elements) " +  myList ); // invokes toString

		// NOW READ IN THE FILE NAMED BY args[1]
		BufferedReader infile1 = new BufferedReader( new FileReader( args[1] ) );
		System.out.println( "Searching myList for the following words from " + args[1] );
		while (infile1.ready() )
		{
			String word = infile1.readLine();
			if ( myList.contains( word ) )
				System.out.println( word + "\tfound" );
			else
				System.out.println( word + "\tNOT found" );
		}
		infile1.close();
	} // END MAIN

	static void die( String errMsg )
	{
		System.out.println( errMsg );
		System.exit(0); // kills program
	}
} 

//This is a starter file for Lab 3.  You will need this file to run LinkedList.java
//This file was written by Timothy Hoffman for CS 0445 at the University of Pittsburgh. I did not write this file.