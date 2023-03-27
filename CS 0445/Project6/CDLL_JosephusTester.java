package Project6;
public class CDLL_JosephusTester
{
	@SuppressWarnings("unchecked")
	public static void main( String args[] ) throws Exception
	{
		if ( args.length < 3 )
			die( "FATAL ERROR. PROGRAM ABORTING. Must enter 3 cmd args, something like this:\n" +
				 "$ java JosephusTester names.txt      HoffmanT            -3\n" + 
				 "                      inputFile   firstNameToDelete   skipCount\n");
		
		String infileName = args[0];
		String first2Bdeleted = args[1];
		int skipCount = Integer.parseInt( args[2] );  // 5 or +3 or -4 etc. + sign optional
		
		@SuppressWarnings("rawtypes")
		CDLL_JosephusList<String> jCircle = new CDLL_JosephusList( infileName );
		System.out.format( "jCircle pre ritual:  %s\n",jCircle );
		jCircle.executeRitual( first2Bdeleted, skipCount );
		System.out.format( "jCircle post ritual: %s <= last rebel standing", jCircle );
	
	} // END MAIN
	
	static void die( String errMsg )
	{	System.out.println( errMsg );
		System.exit( 0 );
	}
} // END CDLL_ListTester CLASS

//I did not write this file. The professor provided it for use with the CDLL_JosephusList.java file.