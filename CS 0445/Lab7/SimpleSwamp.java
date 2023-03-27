package Lab7;
import java.io.*;
import java.util.*;

public class SimpleSwamp
{
	public static void main(String[] args) throws Exception
	{	
		if ( args.length < 1 ) { System.out.println("usage: java SimpleSwamp swamp0.txt (you forgot the filename)"); System.exit(0); }

		int[] origin = new int[2]; // the coords where you are dropped into swamp
		int[][] swamp = loadSwamp( args[0], origin );
		int row=origin[0], col = origin[1];
		escapeSwamp( swamp, row, col);
	}
	static boolean onEdge( int[][] swamp, int r, int c ) // RET TRUE IF ON EDGE OF SWAMP
	{
		return r==0 || r==swamp.length-1 || c ==0 || c==swamp.length-1;
	}
	static void  escapeSwamp(int[][] swamp, int r, int c)
	{
		while ( !onEdge( swamp, r,c ) )
		{
			System.out.format("[%d,%d]",r,c );
			if (swamp[r-1][c] == 1) 	// TRY NORTH
			{
				swamp[r][c] = -1; // mark the cell im ON RIGHT NOW as "been here already dont wanna come back"
				--r; 		  // MOVE to the N
			}
			else if (swamp[r-1][c+1]==1)//test NE cell
			{
				swamp[r][c] = -1; 
				--r; 		  
				++c; 		  
			}
			else if (swamp[r][c+1]==1) //test E cell
			{
				swamp[r][c] = -1;
				++c;
			}
			else if (swamp[r+1][c+1]==1) //test SE cell
			{
				swamp[r][c] = -1;
				++r;
				++c;
			}
			else if (swamp[r+1][c]==1) //test S cell
			{
				swamp[r][c] = -1;
				++r;
			}
			else if (swamp[r+1][c-1]==1) //test SW cell
			{
				swamp[r][c] = -1;
				++r;
				--c;
			}
			else if (swamp[r][c-1]==1)//test W cell
			{
				swamp[r][c] = -1;
				--c;

			}
			else if (swamp[r-1][c-1]==1) //test NW cell
			{
				swamp[r][c] = -1;
				--r;
				--c;
			}
		}
		System.out.format("[%d,%d]\n",r,c ); // YOU ARE OUT OF THE SWAMP!
	}

	static int[][] loadSwamp( String infileName, int[] origin  ) throws Exception
	{
		int[][] theSwamp;
		Scanner infile = new Scanner(new File(infileName));
		int dimension = infile.nextInt();
		theSwamp = new int[dimension][dimension];
		origin[0] = infile.nextInt(); 
		origin[1] = infile.nextInt();
		for (int row = 0; row < dimension; row++)
		{
			for (int col = 0; col < dimension; col++)
			{
				theSwamp[row][col] = infile.nextInt();
			}
		}
		
		return theSwamp;
	}

//I only wrote the code inside the else if statements and the loadswamp method. The rest of the code was provided by the professor.
}
