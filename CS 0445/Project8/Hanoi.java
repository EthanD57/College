package Project8;
public class Hanoi
{
	static int numOfMoves=0;
	public static void main( String args[] ) throws Exception
	{
		if ( args.length < 1 ) die("USAGE: must enter a small postive int on cmd line\n");
		int numOfDisks = Integer.parseInt( args[0] );
		Tower<Integer> src = new Tower<Integer>(); 
		Tower<Integer> dest = new Tower<Integer>(); 
		Tower<Integer> spare = new Tower<Integer>(); 
		for ( int i=numOfDisks; i > 0; --i )
			src.push( i ) ; // auto boxing will convert int 1 to Integer 1
		 
		System.out.format( "%3d\t %-20s\t%-20s\t%-20s\n",numOfMoves,src,dest,spare);
		moveTower( numOfDisks, src, dest, spare );
	
	} // END MAIN
	
	static void  moveTower( int disk, Tower<Integer> src, Tower<Integer> dest, Tower<Integer> spare ) throws Exception
	{   // Use this print statement immediately after a line of code that moves a disk i.e. after every pop+push operation
		// System.out.format( "%3d:\t %-20s\t%-20s\t%-20s\n",++numOfMoves,src,dest,spare);
		if (disk == 1) 
		{
			dest.push(src.pop().label);
			System.out.format( "%3d:\t %-20s\t%-20s\t%-20s\n",++numOfMoves,src,dest,spare);
		}
		else
		{
			moveTower(disk-1, src, spare, dest);
			dest.push(src.pop().label);
			System.out.format( "%3d:\t %-20s\t%-20s\t%-20s\n",++numOfMoves,src,dest,spare);
			moveTower(disk-1, spare, dest, src);
		}

	}
	static void die( String errMsg )
	{
		System.out.println( errMsg );
		System.exit(0);
	}
} // END OF TOWERTESTER CLASS

//A starter file was given. I only wrote the moveTower method. The rest was provided by the professor.