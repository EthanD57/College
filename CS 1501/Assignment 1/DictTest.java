import java.io.*;
import java.util.*;
/** Simple test program to demonstrate DictInterface and its implementations.  
 * Adapted from Dr. John Ramirez's CS 1501 Assignment 1
 */
public class DictTest
{
	public static void main(String [] args) throws IOException
	{
		Scanner fileScan = new Scanner(new FileInputStream("dict8.txt"));
		
		String st;
		StringBuilder sb;
		DictInterface D;
		D = new MyDictionary();
		
		while (fileScan.hasNext())
		{
			st = fileScan.nextLine();
			D.add(st);
		}

		String [] tests = {"abc", "abe", "abet", "abx", "ace", "acid", "hives",
						   "iodin", "inval", "zoo", "zool", "zurich"};
		for (int i = 0; i < tests.length; i++)
		{
			sb = new StringBuilder(tests[i]);
			int ans = D.searchPrefix(sb);
			System.out.print(sb + " is ");
			switch (ans)
			{
				case 0: System.out.println("not found");
					break;
				case 1: System.out.println("a prefix");
					break;
				case 2: System.out.println("a word");
					break;
				case 3: System.out.println("a word and prefix");
			}
		}
	}
}