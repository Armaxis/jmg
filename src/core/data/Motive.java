package core.data;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import jm.gui.cpn.Notate;
import jm.music.data.Phrase;
import core.data.chords.Chord;
import core.data.patterns.CPattern;
import core.data.patterns.Pattern;
import core.data.patterns.SNPattern;
import core.data.tonality.MajorTonality;
import core.data.tonality.Tonality;
import core.generators.Harmonizer;

public class Motive {
	
	//PatternArraysMarkers
	public static final int FIRST_RUN = 0;
	public static final int FIRST_RUN_STABLE = 1;
	public static final int SECOND_RUN = 2;
	public static final int SECOND_RUN_STABLE = 3;
	public static final int THIRD_RUN = 4;
	public static final int THIRD_RUN_STABLE = 5;
		
	
	public LinkedList<Pattern> firstRunPatterns;
	public LinkedList<SNPattern> firstRunStables;
	public LinkedList<CPattern> secondRunPatterns;
	public LinkedList<Pattern> secondRunStables;
	public LinkedList<Pattern> thirdRunPatterns;
	
	public Motive ()
	{
		firstRunPatterns 	= new LinkedList<Pattern>();
		firstRunStables		= new LinkedList<SNPattern>();
		secondRunPatterns 	= new LinkedList<CPattern>();
		secondRunStables	= new LinkedList<Pattern>();
		thirdRunPatterns 	= new LinkedList<Pattern>();
	}
	
	public Phrase getFirstPhrase(HashMap<Double, Chord> harmony, Tonality currentTonality)
	{
		return DataStorage.patternsToPhrase(firstRunPatterns, harmony, currentTonality);
	}
	
	public Phrase getSecondPhrase(HashMap<Double, Chord> harmony, Tonality currentTonality)
	{
		return DataStorage.patternsToPhrase(secondRunStables, harmony, currentTonality);
	}
	
	public Phrase getPhrase(HashMap<Double, Chord> harmony, Tonality currentTonality)
	{
		return DataStorage.patternsToPhrase(thirdRunPatterns, harmony, currentTonality);
	}
	
	public void testNotate(LinkedList<Pattern> list)
	{
		new Notate(DataStorage.patternsToPhrase(list, new Harmonizer().getHarmonyForMotive(this, new MajorTonality(60)), new MajorTonality(60)));
	}
	
	/** Returns first PNote from firstRunPattern
	 * @return
	 */
	public PNote getFirstNote()
	{
		return firstRunPatterns.getFirst().getFirstNote();
	}
	
	public void applyRhythm(List<Double> rhythm, int listType)
	{
		LinkedList<?> list = getListByType(listType);
		//Tweaks notes lengths to make them look sound in lengths like in rhythm
		//Короче, накладывает ритмический рисунок на паттерны
	
		//Applied only if sizes are equal! And if there are more than 1 note
		if(list.size() != rhythm.size() || rhythm.size() == 1)
		{
			return;
		}
		
		double lastNoteAbsoluteLength = ((Pattern)list.getFirst()).getFirstNote().getAbsolute_length();
		for (int i = 1; i < rhythm.size(); i ++)
		{
			Pattern curPattern = ((Pattern)list.get(i)); 
			//TODO: implement calculation for insides of cpattern
			curPattern.getFirstNote().setLength(rhythm.get(i) / lastNoteAbsoluteLength);
			lastNoteAbsoluteLength = rhythm.get(i);
		}
	}
	
	private LinkedList<?> getListByType(int listType)
	{
		switch(listType)
		{
			case Motive.FIRST_RUN:
				return firstRunPatterns;
				
			case Motive.FIRST_RUN_STABLE:
				return firstRunStables;
				
			case Motive.SECOND_RUN:
				return secondRunPatterns;
				
			case Motive.SECOND_RUN_STABLE:
				return secondRunStables;
				
			case Motive.THIRD_RUN:
				return thirdRunPatterns;
		}
		return firstRunPatterns;
	}
	
	public void setOverallVolume (int volume)
	{
		//TODO: Replace with third run patterns
		for (int i = 0; i < thirdRunPatterns.size(); i++)
		{
			thirdRunPatterns.get(i).getFirstNote().setVolumeChange(volume);
		}
	}
	
}
