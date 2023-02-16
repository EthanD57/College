package Project4;

import java.io.*;
import java.util.*;

public class Knapsack {

    public static void main( String args[] ) throws FileNotFoundException 
	{
		if (args.length < 1)
		{
			System.out.println("\nYou must enter an input filename on cmd line!\n");
			System.exit(0);
        }
        
        ArrayList<Integer> numbers = new ArrayList<Integer>();
		try 
        {
            Scanner input = new Scanner(new File(args[0]));
            while (input.hasNext())
            {
                int current = Integer.parseInt(input.next());
                numbers.add(current);
            }
        } 
        catch (FileNotFoundException e) 
        {
            System.out.println("File not found");
        }
        int sum = numbers.get(numbers.size()-1);

        numbers.remove(numbers.size()-1);

        ArrayList<ArrayList<Integer>> powerset = removeBadSubsets(powerSet(numbers),sum);
        for (ArrayList<Integer> subset : powerset)
        {
            for (int x : subset)
            {
                System.out.print(x + " ");
            }
            System.out.println();
        }
}

public static ArrayList<ArrayList<Integer>> powerSet( ArrayList<Integer> set)
{
    ArrayList<ArrayList<Integer>> powerset = new ArrayList<ArrayList<Integer>>();
    int length = set.size();

    for (int i = 0; i < Math.pow(2, length); i++) 
    {
        ArrayList<Integer> subset = new ArrayList<>();

        for (int j = 0; j < length; j++) {

            if ((i & (int) Math.pow(2, j)) != 0) 
            {
                subset.add(set.get(j));
            }
        }
        powerset.add(subset);
    }

    return powerset;
}

public static ArrayList<ArrayList<Integer>> removeBadSubsets( ArrayList<ArrayList<Integer>> powerset, int sum )
    {
        ArrayList<ArrayList<Integer>> goodSubsets = new ArrayList<ArrayList<Integer>>();
        for (ArrayList<Integer> subset : powerset)
        {
            int subsetSum = 0;
            for (int x : subset)
            {
                subsetSum += x;
            }
            if (subsetSum == sum)
            {
                goodSubsets.add(subset);
            }
        }
        return goodSubsets;
    }

}
//This file is for Project 4 of CS 0445. I wrote the entire file. The input is a file that contains a list of number
//separated by spaces. The last number is the sum that the subsets must equal. The output is a list of all subsets