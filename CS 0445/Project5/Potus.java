package Project5;
import java.util.*;
import java.io.*;

public class Potus
{
	public static void main( String[] args )  throws Exception
	{
		BufferedReader state2PresidentsFile = new BufferedReader( new FileReader("state2Presidents.txt") );
		TreeMap<String,TreeSet<String>> state2Presidents= new TreeMap<String,TreeSet<String>>();

		BufferedReader allPresidentsFile = new BufferedReader( new FileReader("allPresidents.txt") );
		TreeSet<String> allPresidents = new TreeSet<String>();

		BufferedReader allStatesFile = new BufferedReader( new FileReader("allStates.txt") );
		TreeSet<String> allStates = new TreeSet<String>();

		System.out.println( "THESE STATES HAD THESE POTUS BORN IN THEM:\n");
		
		printState2Presidents(state2Presidents, state2PresidentsFile);

		System.out.println( "\nLIST OF POTUS AND STATE THEY WERE BORN IN:\n");

		TreeSet<String> noStatePresidents = new TreeSet<String>();

		printAllPresidents(allPresidents, allPresidentsFile, state2Presidents, noStatePresidents);
	
		System.out.println( "\nTHESE POTUS BORN BEFORE STATES WERE FORMED:\n");

		for (String president : noStatePresidents)
		{
			System.out.println(president);
		}

		System.out.println( "\nTHESE STATES HAD NO POTUS BORN IN THEM:\n");	

		printAllStates(allStates, allStatesFile, state2Presidents);
	} // END MAIN

public static void printState2Presidents( TreeMap<String,TreeSet<String>> state2Presidents, BufferedReader state2PresidentsFile) throws IOException
{
	while (state2PresidentsFile.ready())
	{
		TreeSet <String> presidents = new TreeSet<String>();
		String[] line = state2PresidentsFile.readLine().split(" ");

		String state = line[0];

		for (int x = 1; x < line.length; x++)
		{
			presidents.add(line[x]);
		}
		state2Presidents.put(state, presidents);
	}
	for (String state : state2Presidents.keySet())
		{
			System.out.print(state + " ");
			for (String president : state2Presidents.get(state))
			{
				System.out.print(president + " ");
			}
			System.out.println();
		}
}
	
public static void printAllPresidents( TreeSet<String> allPresidents, BufferedReader allPresidentsFile, TreeMap<String, TreeSet<String>> state2Presidents, TreeSet<String> noStatePresidents) throws IOException
{
	while (allPresidentsFile.ready())
	{
		String president = allPresidentsFile.readLine();
		allPresidents.add(president);
	}
	for (String president : allPresidents)
	{
		boolean x = false;

		for (String state : state2Presidents.keySet())
		{
			 
			if (state2Presidents.get(state).contains(president))
			{
				System.out.print(president + " ");
				System.out.println(state);
				x = true;
			}
		}
		if (x == false) noStatePresidents.add(president);
	}
	
}

public static void printAllStates( TreeSet<String> allStates, BufferedReader allStatesFile, TreeMap<String, TreeSet<String>> state2Presidents) throws IOException
{
	while (allStatesFile.ready())
	{
		String state = allStatesFile.readLine();
		allStates.add(state);
	}
	for (String state : allStates)
	{
		if (!state2Presidents.containsKey(state))
		{
			System.out.println(state);
		}
	}
}
}// END CLASS

//A starter file was provided for this project. I only wrote the code outside of the main method and the method calls in the main method.