import java.util.*;
import java.io.*;

public class Project3
{
    static final int INITIAL_CAPACITY = 5;
    
    public static void main( String args[] ) throws Exception
    {
        if (args.length < 1 )
        {
            System.out.println("ERROR: Must put input filename on cmd line\n");
            System.exit(0);
        }
        
        Scanner infile = new Scanner( new File( args[0] ) );        
               
        int[] arr = new int[INITIAL_CAPACITY];
        int count= 0;

        while ( infile.hasNextInt() )
        {
            if ( count==arr.length ) arr = upSizeArr(arr);
            if (insertInOrder( arr, count, infile.nextInt() ) )
		++count;
        }
        
        arr=trimArr(arr,count); // Now count == .length
        printArray( arr );  // we trimmed it thus count == length so we don't bother to pass in count
        
    }
    
    // ############################################################################################################
    
    static void printArray( int[] arr  )
    {
        for( int i=0 ; i<arr.length ;++i )
			System.out.print(arr[i] + " " );
        System.out.println();
    }
    
    static int[] upSizeArr( int[] fullArr )
    {
        int[] upSizedArr = new int[ fullArr.length * 2 ];
        for ( int i=0; i<fullArr.length ; ++i )
			upSizedArr[i] = fullArr[i];
        return upSizedArr;
    }
    
    static int[] trimArr( int[] oldArr, int count )
    {
        int[] trimmedArr = new int[ count ];
        for ( int i=0; i<count ; ++i )
			trimmedArr[i] = oldArr[i];
        return trimmedArr;
    }
    
    // ALL OF MY CODE IS BELOW IN THESE METHODS
    static boolean insertInOrder( int[] arr, int count, int newVal )
    {
         int index = bSearch(arr, count, newVal);
        
	if ( index >= 0 ) return false; 

        index = -(index + 1);

        if (index > arr.length-1)
            arr = upSizeArr(arr);

            for(int i = count; i > index; i--)
                {
                   arr[i] = arr[i-1];
                }

        arr[index] = newVal;
	return true;
    }
    
    static int bSearch(int[] a, int count, int key)
    {
        int low = 0;
        int high = count-1;

        while (low <= high)
        {
            int mid = low + (high - low)/2;
            if(a[mid] == key)
                return mid;
            else if (a[mid] > key)
                high = mid - 1;
            else if (a[mid] < key)
                low = mid + 1;  
        }
        return -(low + 1);

    }
}

//A STARTER FILE WAS GIVEN FOR THIS CODE. THE CODE SECTION INDICATED WAS MADE BY ME. 