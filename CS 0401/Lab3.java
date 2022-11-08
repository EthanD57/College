// STARTER FILE LAB #3 

import java.util.*;

import java.io.*;

public class Lab3
{
    static final int MAX_CAPACITY = 30;  // HARDOCED PLENTY BIG. WE'LL DO TRIM AFTER
    
    public static void main( String args[] ) throws Exception
    {
        if (args.length < 1 )
        {
            System.out.println("usage: $ java Lab3 <inupt file of random numbers>\n");
            System.exit(0);
        }
        
        int[] arr = new int[ MAX_CAPACITY ];
        int count=0;
        
        Scanner infile = new Scanner( new File( args[0] ) );
        while ( infile.hasNextInt() )
        {
            insertInOrder( arr, count, infile.nextInt() );
            ++count;
        }
        
        arr = trimArray( arr, count );
        printArray( arr ); // NOTE:  NO COUNT PASSED IN
        
    }// END MAIN
    
    // ############################################################################################################
    
    // MUST USE ENHANCED FOR LOOP IN THIS METHOD
    // (YOUR TRIM BETTER HAVE BEEN WRITEN CORRECTLY)
    static void printArray( int[] array )
    {
        // PRINT EACH NUMBER WITH A SPACE AFTER IT
        for (int number: array) 
        {
        System.out.print(number + " ");
        }
    }
    
    static int[] trimArray( int[] array, int count  )
    {
        int[] temp = new int[count];
        for (int i = 0; i < count; i++)
        {
           temp[i] = array[i];
        }
        return temp;
    }
    
   
  //ALL OF MY CODE IS IN THE METHOD BELOW
    static void insertInOrder( int[] arr, int count, int newVal   )
    {
        for(int i = 0; i <= count+1; i++)
        {
            if(arr[i] == 0)
            {
                arr[i] = newVal;
                break;
            }

            if(newVal < arr[i])
            {
                for(int j = count; j > i; j--)
                {
                   arr[j] = arr[j-1];
                   
                }
                arr[i] = newVal;
                break;
            }
        }
    }
}// END LAB #3
//A STARTER FILE WAS GIVEN FOR THIS CODE. THE CODING SECTION INDICATED WAS MADE BY ME. 