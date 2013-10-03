package core.data.tonality;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Random;

import util.Tools;
import core.data.chords.Chord;

public class Tonality {
	protected int pitch;
	protected int[] intervals; //Main intervals for this tonality. 0, 2, 4, etc..
	protected LinkedList<Integer> allTonalitySteps;
	//protected ArrayList<String> degreesNames = new ArrayList<String>();
	//public ArrayList<Chord> degrees;  //Список ступеней
	protected HashMap<String, Chord> degrees;
	protected String type;

	public String getType() {
		return type;
	}

	public int[] getIntervals() {
		return intervals;
	}

	public void setIntervals(int[] intervals) {
		this.intervals = intervals;
	}
	
	public Tonality(int pitch, int[] intervals) {
		super();
		this.pitch = pitch;
		this.intervals = intervals;
		this.type = "nontonal";
		
		degrees = new HashMap<String, Chord>();
		initAllTonalitySteps();
	}
	
	private void initAllTonalitySteps()
	{
		allTonalitySteps = new LinkedList<Integer>();
		int absolute_pitch = pitch % 12; //Get pitch value from 0 to 11 - to start from.
		for (int i = 0; i < 10; i ++)
		{
			for (int j = 0; j < intervals.length; j ++)
			{
				allTonalitySteps.add(intervals[j] + absolute_pitch + 12 * i);
			}
		}
	}
	
	
	/** Returns either I note or V note of tonality
	 * 
	 */
	public int getRandomMainNotePitched()
	{
		if(Math.random() > 0.7)
			return pitch;
		else
			return pitch + intervals[4];
	}
	
	/** Returns pitch of note which is at a distance of <code>step</code> from <code>noteValue</code>. E.g. [2, 60] for C-maj tonality will return 64. [2,61] => 64. [2,62] => 65.
	 * @param step Step - how many degrees we should leave before reaching required note
	 * @param noteValue Starting point - note pitch.
	 * @return
	 */
	public int getTonalityNoteByStep(int step, int noteValue)
	{
		int[] array = new int[allTonalitySteps.size()];
		for (int i = 0; i < allTonalitySteps.size(); i++) {
		    array[i] = allTonalitySteps.get(i);
		}
		return Tools.getElementInArrayByStep(step, array, noteValue); 
	}

	/** Returns chord as int[] by degree name 
	 * @param deg Degree Name
	 * @return Chord as int[]
	 */
	public Chord getChordByDegree(String deg) 
	{
		return (degrees.get(deg));
	}
	
	public Chord getRandomDegreeChord()
	{
		Random generator = new Random();
		Object[] values = degrees.values().toArray();
		Object randomValue = values[generator.nextInt(values.length)];
		return (Chord)randomValue;
	}

	public int getPichedScale(int scaleId) {
		return (intervals[scaleId] + pitch);
	}


	public void switchTonality(int id) {
		// TODO Implement

	}

	public int getPitch() {
		return pitch;
	}
}
