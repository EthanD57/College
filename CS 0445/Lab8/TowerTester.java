package Lab8;
import java.io.*;
import java.util.*;

public class TowerTester
{
	public static void main( String args[] ) throws Exception
	{
		Tower<Integer> myTower = new Tower<Integer>(); 
		Scanner infile = new Scanner( new FileReader( args[0] ) ); // CONTAINS 1 LINE SEQUENCE LIKE 10 9 8 7 6 5 4 3 2 1 
		while ( infile.hasNextInt() )
			myTower.push( infile.nextInt() );
		infile.close();
		
		System.out.format( "myTower loaded from %s: %s\n", args[0], myTower );
		
		while ( ! myTower.empty() )
		{	myTower.pop();
			System.out.format( "myTower after pop: %s\n", myTower );
		}
	} // END MAIN
} // END OF TOWERTESTER CLASS

//This file is meant to be run with Tower.java. I did not write this file. It was provided by the professor.