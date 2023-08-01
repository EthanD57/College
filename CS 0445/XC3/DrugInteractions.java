package XC3;
import java.util.*;
import java.io.*;

public class DrugInteractions
{
	public static void main( String[] args ) throws Exception
	{
		BufferedReader foodDrug2CategoryFile = new BufferedReader( new FileReader( "foodDrug2Category.txt" ) );
		BufferedReader patient2FoodDrugFile = new BufferedReader( new FileReader( "patient2FoodDrug.txt") );
		BufferedReader dontMixFile = new BufferedReader( new FileReader( "dontMix.txt" ) );
	

		Map<String, TreeSet<String>> food = new HashMap<String, TreeSet<String>>();

		while ( foodDrug2CategoryFile.ready() )
		{
			String line = foodDrug2CategoryFile.readLine();
			String[] tokens = line.split( "," );
			food.put( tokens[0], new TreeSet<String>() );
			for ( int i = 1; i < tokens.length; i++ )
			{
				food.get( tokens[0] ).add( tokens[i] );
			}
			
		}
		//print keys in order
		ArrayList<String> keys = new ArrayList<String>(food.keySet());
		Collections.sort(keys);
		for (String key : keys)
		{
			System.out.print(key + " ");
			String temp = food.get(key).toString();
			temp = temp.replace("[", "").replace("]", "").replace(",", "");
			System.out.println(temp);
		}

		Map<String, TreeSet<String>> patient = new HashMap<String, TreeSet<String>>();

		while ( patient2FoodDrugFile.ready() )
		{
			String line = patient2FoodDrugFile.readLine();
			String[] tokens = line.split( "," );
			patient.put( tokens[0], new TreeSet<String>() );
			for ( int i = 1; i < tokens.length; i++ )
			{
				patient.get( tokens[0] ).add( tokens[i] );
			}
			
		}
		//print keys in order
		ArrayList<String> names = new ArrayList<String>(patient.keySet());
		Collections.sort(names);
		System.out.print("\n");
		for (String name : names)
		{
			System.out.print(name + " ");
			String temp = patient.get(name).toString();
			temp = temp.replace("[", "").replace("]", "").replace(",", "");
			System.out.println(temp);
		}

		ArrayList<ArrayList<String>> dontMix = new ArrayList<ArrayList<String>>();
		while(dontMixFile.ready())
		{
			String line = dontMixFile.readLine();
			String[] tokens = line.split( "," );
			ArrayList<String> temp = new ArrayList<String>();
			for ( int i = 0; i < tokens.length; i++ )
			{
				temp.add(tokens[i]);
			}
			dontMix.add(temp);
		}
	
		Map<String, TreeSet<String>> DrugsAndPatients = new HashMap<String, TreeSet<String>>();
		//A lot of loops but it works
		for (String name : names) //For each person 
		{
			DrugsAndPatients.put(name, new TreeSet<String>()); //Add them to the map

			for (String drug : patient.get(name)) 	//Get thier list of drugs
			{
				for (String category : food.keySet())   //Get the list of categories for drugs and checks to see which cat the drug is i
				{
					if (food.get(category).contains(drug))
						DrugsAndPatients.get(name).add(category); //Add the category to the map
				}
			}
		}

		System.out.println("");
		for (String name : DrugsAndPatients.keySet())
		{
			for (ArrayList<String> bad : dontMix)
			{
				if (DrugsAndPatients.get(name).contains(bad.get(0)) && DrugsAndPatients.get(name).contains(bad.get(1)))
				{
					System.out.println(name);
				}
			}
		}
		
		
	} // END MAIN

} // END CLASS