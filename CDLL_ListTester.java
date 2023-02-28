import java.io.*;
import java.util.*;

public class CDLL_ListTester
{
	@SuppressWarnings("unchecked")
	public static void main( String args[] ) throws Exception
	{
		String[] keys = { "charlie", "golf", "bravo", "dragonfly" }; // WE WILL SEARCH THE LISTS FOR THESE
		
		CDLL_List<String> cdll_1 = new CDLL_List<String>( args[0], "atFront" );
		System.out.format( "cdll_1 loaded from %s (insertAtFront) size=%d %s\n",args[0],cdll_1.size(),cdll_1 );
		for ( String key : keys )
			System.out.format( "cdll_1.contains(%s) returns %b\n", key,cdll_1.contains(key) );
		
		CDLL_List<String> cdll_2 = new CDLL_List<String>( args[0], "atTail" );
		System.out.format( "cdll_2 loaded from %s (insertAtTail)  size=%d %s\n",args[0],cdll_2.size(),cdll_2 );
		for ( String key : keys )
			System.out.format( "cdll_2.contains(%s) returns %b\n", key,cdll_2.contains(key) );
		
	} // END MAIN
} // END CDLL_ListTester CLASS