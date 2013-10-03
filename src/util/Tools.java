package util;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;


public class Tools {

	/** Returns pitch of note which is at a distance of <code>step</code> from <code>startNote</code>.
	 * @param step  Step - how many degrees we should leave before reaching required note
	 * @param array Array where search should be performed
	 * @param startNote Starting point - note pitch.
	 * @return
	 */
	public static int getElementInArrayByStep(int step, int[] array, int startNote)
	{
			int tonalityElementId = 0;
			for (int i = 0; i < array.length - 1; i++)
			{
				if(array[i] > startNote)
				{
					tonalityElementId = (Math.signum(step) > 0) ? i - 1 : i; //If we are going down the scale - we take note which is after.
					break;
				}
				else if (array[i] == startNote)
				{
					tonalityElementId = i;
					break;
				}
			}
			
			if((tonalityElementId + step) < 0 || (tonalityElementId + step) > array.length)
			{
				Log.severe("Tonality > getTonalityNoteByStep > Out of bounds error! Element ID = " + tonalityElementId + " Step = " + step);
				return 0;
			}
			
			return array[tonalityElementId + step]; 
	}
	
	public static Double nearestKey(HashMap<Double, ?> map, Double target) {
	    double minDiff = Double.MAX_VALUE;
	    Double nearest = null;
	    for (Double key : map.keySet()) {
	    	if(key <= target)
	    	{
		        double diff = Math.abs((double) target - (double) key);
		        if (diff < minDiff) {
		            nearest = key;
		            minDiff = diff;
		        }
	    	}
	    }
	    return nearest;
	}

	public static Integer pick(HashMap<Integer, Double> valuesHash)
	{
		// Compute the total weight of all items together
		double totalWeight = 0.0d;
		for (Double i : valuesHash.values())
		{
		    totalWeight += i;
		}
		// Now choose a random item
		double random = Math.random() * totalWeight;
		
		for (Integer hashKey : valuesHash.keySet())
		{
			random -= valuesHash.get(hashKey);
		    if (random <= 0.0d)
		    {
		        return hashKey;
		    }
		}
		Log.severe("Tools > pick > Cannot pick a random element in Map: " + valuesHash.toString());
		return 0; //Shouldn't happen
	}
	
	/** Переводит индекс сдвига по октаве в реальный сдвиг в нотах. Индексы бывают [0-6], сдвиги соответственно [-36, -24, -12, 0, 12, 24, 36]
	 * @param octaveIndex Индекс сдвига (от 0 до 6 включительно)
	 * @return Реальный сдвиг по октаве в нотах (от -36 до 36 включительно)
	 */
	public static int octaveIndexToShift(int octaveIndex)
	{
		return (octaveIndex * 12) - 36;
	}
	
}
