package core.data.structure;

import java.util.HashMap;

import core.data.Motive;

public class PhraseStructure {
	
	public static final int MOTIVE_EXISTS = 0;
	public static final int NEW_MOTIVE = 1;
	public static final int SIMILAR_MOTIVE = 2;
	public static final int LAST_BAR = 3;
	
	private HashMap<String, Motive> motives;
	private MotiveStructure motiveStructure;
	private String currMotiveType;
	private int iterator;
	
	public PhraseStructure(MotiveStructure struct)
	{
		motives 			= new HashMap<String, Motive>();
		motiveStructure 	= struct;
		iterator 			= 0;
		currMotiveType 		= motiveStructure.getMotiveTypes().get(iterator);
	}
	
	/** Returns null if current motiveType doesn't exit
	 * @return
	 */
	public int getRequiredGenerationType()
	{
		//if it's almost end - return last bar type
		if(iterator == motiveStructure.getMotiveTypes().size() - 1)
		{
			return LAST_BAR;
		}
		
		//if hashmap has exact same
		if(motives.containsKey(currMotiveType))
		{
			//return element
			return MOTIVE_EXISTS;
		}
		else
		{
			String withoutQuotes = (currMotiveType.contains("'")) ? currMotiveType.substring(0, currMotiveType.indexOf("'")) : currMotiveType;

			//if has same without commas
			if (motives.containsKey(withoutQuotes))
			{
				//call generateSimilarMotive based on elementwithout commas
				return SIMILAR_MOTIVE;
			}
			else
			{
				//call generateMotive
				return NEW_MOTIVE;
			}
		}
	}
	
	public Motive getMotiveForSimilar()
	{
		String withoutQuotes = (currMotiveType.contains("'")) ? currMotiveType.substring(0, currMotiveType.indexOf("'")) : currMotiveType;
		return motives.get(withoutQuotes);
	}
	
	public Motive getExistingMotive()
	{
		return motives.get(currMotiveType);
	}
	
	public void addMotive(Motive m)
	{
		motives.put(currMotiveType, m);
	}
	
	
	/** 
	 * @return
	 */
	public void iterate()
	{
		iterator ++;
		if(iterator != motiveStructure.getMotiveTypes().size())
			currMotiveType 	= motiveStructure.getMotiveTypes().get(iterator);
	}
	
	public boolean hasAllMotives()
	{
		return (iterator == motiveStructure.getMotiveTypes().size());
	}
	
	@Override
	public String toString()
	{
		return "[" + "Structure:" + motiveStructure.toString() + " Hashmap:" + motives.toString() + "]";
	}
}
