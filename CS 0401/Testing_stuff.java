import java.util.Arrays;

//This code is purely for me to test things that interest me in java
public class Testing_stuff
{
    public static void main (String args[])
    {
        int counter = 1;
        int[] arr = new int[10];
        for( int x = 0; x < arr.length - 1; x++)
        {
            arr[x] = counter;
            counter++;
        }
        System.out.print(Arrays.binarySearch(arr, 6));
    }  
}