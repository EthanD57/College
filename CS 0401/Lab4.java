import java.io.*;
import java.util.*;

public class Lab4 {
    public static void main (String args[]) throws Exception
    {
        if (args.length < 1 )
		{
			System.out.println("\nusage: C:\\> java Lab2 <input filename>\n\n");
			System.exit(0);
		}
        ArrayList<String>BIGWORDS = new ArrayList<String>();
        try (BufferedReader input = new BufferedReader (new FileReader( args[0] )))
        {
            while (input.ready())
            {
                BIGWORDS.add(input.readLine());
            }
        }
       Collections.sort(BIGWORDS);

        for (String element : BIGWORDS)
        {
            System.out.println(element + " " + canon(element));
            
        }
    }
    public static String canon (String words)
    {
        char[] chars = words.toCharArray();
        Arrays.sort(chars);
        return new String(chars);
    }
}  
//NO STARTING CODE FOR THIS LAB