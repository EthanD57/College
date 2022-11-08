import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class Project4 {
    public static void main(String[] args) throws Exception
    {
        if (args.length < 2 )
		{
			System.out.println("\nusage: C:\\> java Lab2 <input Dictionary> <input Jumbles>\n\n");
			System.exit(0);
		}
        ArrayList<String>Jumbles = new ArrayList<String>();
        ArrayList<String>Dictionary = new ArrayList<String>();
        ArrayList<String>CanonDictionary = new ArrayList<String>();

//This is the buffered readers for the dictionary and jumbles files -------------------------

        try (BufferedReader input = new BufferedReader (new FileReader( args[1] )))
        {
            while (input.ready())
            {
                Jumbles.add(input.readLine());
            }
        }
        try (BufferedReader input2 = new BufferedReader (new FileReader( args[0] )))
        {
            while (input2.ready())
            {
                String curWord = input2.readLine();
                Dictionary.add(curWord);
                CanonDictionary.add(canon(curWord));
            }
        }
//---------------------------------------------------------------------------------------------
       Collections.sort(Jumbles);
       
        for (int i=0; i<Jumbles.size(); i++)
        {
            System.out.println(Jumbles.get(i) + " " + findMatches(canon(Jumbles.get(i)), CanonDictionary, Dictionary));
        }

    }

//END OF MAIN METHOD --------------------------------------------------------------------------------



//This canonizes the word and returns it as a string -------------------------------------------------
    public static String canon (String words)
    {
        char[] chars = words.toCharArray();
        Arrays.sort(chars);
        return new String(chars);
    }
 
//This method finds the matches for the jumbles and returns them as a string ----------------------------
    public static String findMatches (String word, ArrayList<String>Canondict,ArrayList<String>Dictionary)
    {
        ArrayList<String>Matches = new ArrayList<String>();
        for (int i = 0; i < Canondict.size(); i++)
            {
                if (word.equals(Canondict.get(i)))
                {
                    Matches.add(Dictionary.get(i));
                }
            }

        Collections.sort(Matches);

        String matchString = "";
        for (String s: Matches)
        {
            matchString += s + " ";
        }
        return matchString;
    
    }
}
//NO STARTER FILE GIVEN.