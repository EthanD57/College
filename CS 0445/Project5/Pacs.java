package Project5;
import java.io.*;
import java.util.*;

public class Pacs
{	public static void main( String args[] ) throws Exception
	{	try (BufferedReader memberToPacsFile = new BufferedReader(new FileReader( "member2Pacs.txt"))) {
		try (BufferedReader AllPacsFile = new BufferedReader(new FileReader("allPacs.txt"))) {
			TreeSet<String> allPacs= new TreeSet<String>();
			while( AllPacsFile.ready())
				allPacs.add(AllPacsFile.readLine());			
			
			TreeMap<String, TreeSet<String>> pacToMembers = new TreeMap<String, TreeSet<String>>(); // THE MAP THAT GETS PRINTED	
			

//MY CODE STARTS HERE

			while (memberToPacsFile.ready())
			{
				String[] words = memberToPacsFile.readLine().split(" ");

				String member = words[0];

				TreeSet <String> pacs = new TreeSet<String>();

				for (int i = 1; i < words.length; i++)
				{
					 pacs.add(words[i]);
				}

				pacToMembers.put(member, pacs);
			}
			for (String pac : allPacs)
			{
				System.out.print(pac + " ");
				for (String member : pacToMembers.keySet())
				{
					if (pacToMembers.get(member).contains(pac))
					{
						System.out.print(member + " ");
					}
				}
				System.out.println();
			}
		}
	}
	} // END MAIN
} // CLASS

//A starter file was provided for this project. I only wrote the code below the indicated line.
