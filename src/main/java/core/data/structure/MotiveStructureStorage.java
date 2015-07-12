package core.data.structure;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;

public class MotiveStructureStorage {

	public static LinkedList<MotiveStructure> MOTIVES_STRUCTURES;
	
	public static void init()
	{
		MOTIVES_STRUCTURES = new LinkedList<MotiveStructure>();
		parseFileIntoList("database/MotivesStructures.txt", MOTIVES_STRUCTURES);
	}
	
	private static void parseFileIntoList(String filename, LinkedList<MotiveStructure> list)
	{
		File f = new File(filename);
		BufferedReader in;
		try {
			in = new BufferedReader(new FileReader(f));
			String inputLine;
			while ((inputLine = in.readLine()) != null) {
				if(inputLine.startsWith("//")) //Skip comments
					continue; 
				if(inputLine.startsWith("Name~")) //Start of new pattern; Parse till end
				{
					String name = inputLine.substring(inputLine.indexOf("~") + 1); //Get name
					String data = "";
					while(!(inputLine = in.readLine()).startsWith("End"))
					{
						data = data + inputLine;
					}
					MotiveStructure p = new MotiveStructure(data);
					p.setName(name);
					list.add(p);
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
