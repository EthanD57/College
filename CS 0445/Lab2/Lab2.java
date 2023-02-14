package Lab2;
import java.io.*;
import java.util.*;

public class Lab2
{
	public static void main( String args[] ) throws Exception
	{
		BufferedReader infile1 = new BufferedReader( new FileReader( args[0] ) );
		BufferedReader infile2 = new BufferedReader( new FileReader( args[1] ) );

		String[] set1 = loadSet( infile1 );
		Arrays.sort( set1 );
		String[] set2 = loadSet( infile2 );
		Arrays.sort( set2 );
		printSet( "set1: ",set1 );
		printSet( "set2: ",set2 );

		String[] union = union( set1, set2 );
		Arrays.sort( union );
		printSet( "\nunion: ", union );


		String[] intersection = intersection( set1, set2 );
		Arrays.sort( intersection );
		printSet( "\nintersection: ",intersection );

		String[] difference = difference( set1, set2 );
		Arrays.sort( difference );
		printSet( "\ndifference: ",difference );

		String[] xor = xor( set1, set2 );
		Arrays.sort( xor );
		printSet("\nxor: ", xor );

		System.out.println( "\nSets Echoed after operations.");

		printSet( "set1: ", set1 );
		printSet( "set2: ", set2 );

	}

	static String[] loadSet( BufferedReader infile ) throws Exception
	{
		final int INITIAL_LENGTH = 5;
		int count=0;
		String[] set = new String[INITIAL_LENGTH];
		while( infile.ready() )
		{
				if (count >= set.length)
					set = doubleLength( set );
				set[ count++ ] = infile.readLine();
		}
		infile.close();
		return trimArray( set, count );
	}

	static void printSet( String caption, String [] set )
	{
		System.out.print( caption );
		for ( String s : set )
			System.out.print( s + " " );
		System.out.println();
	}

//MY CODE IS BELOW THIS LINE ------------------------------------------------------------

	static String[] union( String[] set1, String[] set2 )
	{
		String[] union = new String[set1.length + set2.length];
		
		int count = 0;

		for (String x : set1) 
		{
			union[count++] = x;
		}
		for (String x : set2) 
		{
			union[count++] = x;
		}
		Arrays.sort(union);
		for (int i = 0; i < union.length; i++) 
		{
			for (int j = i + 1; j < union.length; j++)
			{
				if (union[i].equals(union[j])) 
				{
					union[i] = null;
					count--;
					break;
				}
			}
		}

		return trimArray(union, count);
	}

	static String[] intersection( String[] set1, String[] set2 )
	{
		String[] intersection = null;
		int count = 0;
		if (set1.length > set2.length) 
		{
			intersection = new String[set2.length];
		}

		else 
		{
			intersection = new String[set1.length];
		}
		

		for (int x = 0; x < set2.length; x++) 
			{
				for (String y : set1) 
				{
					if ((set2[x].equals(y))) 
					{
						intersection[count++] = set2[x];
						break;
					}
				}
			}
		
		return trimArray(intersection,count);
		
	}

	static String[] difference( String[] set1, String[] set2 )
	{
		String difference[] = new String[set1.length];
		int count = 0;
		for (int i = 0; i < set1.length; i++)
		{
			for (int j = 0; j < set2.length; j++)
			{
				if (set1[i].equals(set2[j]))
				{
					break;
				}
				else if (j == set2.length - 1)
				{
					difference[count++] = set1[i];
				}
			}
		}
		
			
		return trimArray(difference, count); 
	}

	static String[] xor( String[] set1, String[] set2 )
	{
		return difference(union(set1, set2), intersection(set1, set2));
	}

	static String[] doubleLength( String[] old )
	{
		int x = old.length * 2;
		String[] temp = new String[x];
		for (int i = 0; i < old.length; i++) 
		{
			temp[i] = old[i];
		}
		return temp;
	}


	static String[] trimArray( String[] old, int count )
	{
		String[] temp = new String[count];
		int increment = 0;
		for (int i = 0; i < old.length; i++) 
		{
			if(old[i] != null)
			{
				temp[increment++] = old[i];
			}
		}
		return temp;
	}

} 

//A starter file was given for this lab. All the code above the indicated line was written by Tim Hoffman for CS 0445
//The code below the indicated line was written by me.