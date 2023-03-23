import java.io.*;
import java.util.*;

public class ComplexSwamp
{
	static int[][] swamp;  

 	public static void main(String[] args) throws Exception
	{
		int[] dropInPt = new int[2]; 
		swamp = loadSwamp( args[0], dropInPt );
		int row=dropInPt[0], col = dropInPt[1];
		String path = ""; 
		dfs( row, col, path );
	} 

 	
	private static int[][] loadSwamp( String infileName, int[] dropInPt  ) throws Exception
	{
		int[][] theSwamp;
		Scanner infile = new Scanner(new File(infileName));
		int dimension = infile.nextInt();
		theSwamp = new int[dimension][dimension];
		dropInPt[0] = infile.nextInt(); 
		dropInPt[1] = infile.nextInt();
		for (int row = 0; row < dimension; row++)
		{
			for (int col = 0; col < dimension; col++)
			{
				theSwamp[row][col] = infile.nextInt();
			}
		}
		
		return theSwamp;
	}

	static void dfs( int row, int col, String path ) 
	{
		path += String.format("[%d,%d]", row, col);

		if (onEdge(swamp, row, col))
		{
			System.out.println(path);
			return;
		}
		if (swamp[row-1][col] == 1) 	
		{
			swamp[row][col] = -1; 
			dfs( row-1, col, path);
			swamp[row][col] = 1;	 
		}
		if (swamp[row-1][col+1]==1)
		{
			swamp[row][col] = -1; 
			dfs( row-1, col+1, path);
			swamp[row][col] = 1;
		}
		if (swamp[row][col+1]==1)
		{
			swamp[row][col] = -1;
			dfs(row, col+1, path);
			swamp[row][col] = 1;
		}
		if (swamp[row+1][col+1]==1)
		{
			swamp[row][col] = -1;
			dfs(row+1, col+1, path);
			swamp[row][col] = 1;
		}
		if (swamp[row+1][col]==1) 
		{
			swamp[row][col] = -1;
			dfs(row+1, col, path);
			swamp[row][col] = 1;
		}
		if (swamp[row+1][col-1]==1)
		{
			swamp[row][col] = -1;
			dfs(row+1, col-1, path);
			swamp[row][col] = 1;
		}
		if (swamp[row][col-1]==1)
		{
			swamp[row][col] = -1;
			dfs(row, col-1, path);
			swamp[row][col] = 1;
		}
		if (swamp[row-1][col-1]==1) 
		{
			swamp[row][col] = -1;
			dfs( row-1, col-1, path);
			swamp[row][col] = 1;
		}
		return;	
	}	

	static boolean onEdge( int[][] swamp, int r, int c ) 
	{
		return r==0 || r==swamp.length-1 || c ==0 || c==swamp.length-1;
	}
}
