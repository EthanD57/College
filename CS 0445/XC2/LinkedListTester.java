package XC2;
import java.io.*;
import java.util.*;

public class LinkedListTester
{
	public static void main( String args[] ) throws Exception
	{
		if ( args.length < 2 )
			die( "FATAL ERROR: must enter two filenames on command line.\n");
		
		//  READ IN BOTH SETS INTO LINKED LISTS 1 & 2
		
		LinkedList<String> list_1 = new LinkedList<String>();
		Scanner infile_1 = new Scanner( new File( args[0] ) );
		while ( infile_1.hasNext() )
				list_1.insertAtTail( infile_1.next() );
		infile_1.close();
		LinkedList<String> list_2 = new LinkedList<String>();
		Scanner infile_2 = new Scanner( new File( args[1] ) );
		while ( infile_2.hasNext() )
				list_2.insertAtTail( infile_2.next() );
		infile_2.close();

	//  ECHO OUT BOTH LISTS - EACH LIST ON ITS OWN LINE 
	
	System.out.println( "list_1: " + list_1 ); // INVOKE THE LINKEDLIST CLASS's .toString
	System.out.println( "list_2: " + list_2 ); // INVOKE THE LINKEDLIST CLASS's .toString
	
	// CREATE AND PRINT A THIRD LIST: THE SORTED MERGE OF FIRST TWO LISTS
	LinkedList<String> theMerge = list_1.merge( list_2 );
	System.out.println( "merged: " + theMerge ); // PRINT THE SORTED MERGER OF THE TWO LISTS
	
	//  ECHO OUT BOTH LISTS AGAIN JUST TO PROVE YOU DID NOT CORRUPT THEM IN THE MERGE CODE
	
	System.out.println( "list_1: " + list_1 ); // INVOKE THE LINKEDLIST CLASS's .toString
	System.out.println( "list_2: " + list_2 ); // INVOKE THE LINKEDLIST CLASS's .toString	
	
	} // END MAIN

	static void die( String errMsg )
	{
		System.out.println( errMsg );
		System.exit(0); // kills program
	}
} // EOF

//I did not write this file. It was provided by the instructor.