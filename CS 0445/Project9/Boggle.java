package Project9;
import java.io.*;
import java.util.*;


public class Boggle
{
	static String[][] board;
	static long startTime,endTime; // for timing
	static final long MILLISEC_PER_SEC = 1000;
	static TreeSet <String> matches = new TreeSet <String>();
	static TreeSet <String> dictionary = new TreeSet <String>();
	static TreeSet <String> correct = new TreeSet <String>();


	public static void main( String args[] ) throws Exception
	{
		startTime= System.currentTimeMillis();
		board = loadBoard( args[1] );	
	
		loadDictionary( dictionary, args[0] );


		for (int row = 0; row < board.length; row++)
			for (int col = 0; col < board[row].length; col++)
				dfs( row, col, ""  );

		for (String s : matches)
			System.out.println(s);

		endTime =  System.currentTimeMillis(); 
		System.out.println("GENERATION COMPLETED: runtime=" + (endTime-startTime)/MILLISEC_PER_SEC );

	} // END MAIN ----------------------------------------------------------------------------

	static void dfs( int r, int c, String word  )
	{
		word += board[r][c];

			if (dictionary.contains(word))
			{
				matches.add(word);
			}
			else if (dictionary.subSet(word, word + Character.MAX_VALUE).size() == 0)
			{
				return;
			}

		if (r-1 >= 0 && board[r-1][c] != null)  
		{	
			String unMarked = board[r][c];
			board[r][c] = null; 
			dfs(r-1, c, word ); 
			board[r][c] = unMarked; 
		}

		// NE
		if (r-1 >= 0 && c+1 < board[r].length && board[r-1][c+1] != null)
		{	
			String unMarked = board[r][c]; 
			board[r][c] = null; 
			dfs(r-1, c+1, word ); 
			board[r][c] = unMarked; 
		}
		// E 
		if (c+1 < board[r].length && board[r][c+1] != null )
		{	
			String unMarked = board[r][c]; 
			board[r][c] = null; 
			dfs(r, c+1, word ); 
			board[r][c] = unMarked;
		}
		// SE
		if (r+1 < board[r].length && c+1 < board[r].length && board[r+1][c+1] != null )
		{	
			String unMarked = board[r][c]; 
			board[r][c] = null; 
			dfs(r+1, c+1, word );
			board[r][c] = unMarked; 
		}

		// S 
		if (r+1 < board[r].length && board[r+1][c] != null )
		{	
			String unMarked = board[r][c]; 
			board[r][c] = null; 
			dfs(r+1, c, word );
			board[r][c] = unMarked; 
		}
		// SW 
		if (r+1 < board[r].length && c-1 >= 0 && board[r+1][c-1] != null )
		{	
			String unMarked = board[r][c]; 
			board[r][c] = null; 
			dfs(r+1, c-1, word ); 
			board[r][c] = unMarked; 
		}
		// W 
		if (c-1 >= 0 && board[r][c-1] != null )
		{	
			String unMarked = board[r][c]; 
			board[r][c] = null; 
			dfs(r, c-1, word ); 
			board[r][c] = unMarked;
		}
		// NW IS ...
		if (r-1 >= 0&& c-1 >= 0 && board[r-1][c-1] != null )
		{	
			String unMarked = board[r][c]; 
			board[r][c] = null; 
			dfs(r-1, c-1, word ); 
			board[r][c] = unMarked; 
		}
	} // END DFS ----------------------------------------------------------------------------

	//=======================================================================================
	static String[][] loadBoard( String fileName ) throws Exception
	{	Scanner infile = new Scanner( new File(fileName) );
		int rows = infile.nextInt();
		int cols = rows;
		String[][] board = new String[rows][cols];
		for (int r=0; r<rows; r++)
			for (int c=0; c<cols; c++)
				board[r][c] = infile.next();
		infile.close();
		return board;
	} //END LOADBOARD

	static void loadDictionary (TreeSet<String> dict, String fileName) throws Exception
	{	
		Scanner infile = new Scanner( new File(fileName) );
		while (infile.hasNext())
		{
			String temp = infile.next();
			if (temp.length() >= 3)
			dict.add(temp);
		}
		infile.close();
	} 

} // END BOGGLE CLASS

//A starter file was given. I wrote the dfs method, and the loadDictionary method. The rest was given by the professor.

/*This file is intended to be used with 2 command line arguments. 
The first should be a dictionary file, and the second should be a board file.
Dictionary file should be lines of correct words that are in the boggle board. 
The board file should be a square of letters. With a dimension on the first line.

Ex. 

3
A B C
D E F
G H I

*/
