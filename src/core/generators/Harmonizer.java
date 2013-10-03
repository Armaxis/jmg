package core.generators;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import util.Log;
import core.data.Motive;
import core.data.chords.Chord;
import core.data.patterns.SNPattern;
import core.data.tonality.Tonality;

public class Harmonizer {

	private HashMap<String, LinkedList<String>> chordAntecendents; //Indicates which chords can be before the "key" chord.
	private HashMap<String, LinkedList<String>> chordConsequents; //Indicates which chords can follow the "key" chord
	
	//Local variables used for harmonizing
	
	private LinkedList<List<SNPattern>> barsList; 	//With blackjack and whores. 
	private Tonality currentTonality;				//Current tonality for harmonization
	private int currentBarIndex;					//Index of bar which is been harmonized
	private String lastChord;						//Name of last chord that was
	private int chordsUsed;							//Number of beats in bar already having a harmony. Used to prevent many chords in bar.
	private int beatsHarmonized;
	
	private boolean halfByHalfHarmonization = false;
	
	
	public Harmonizer ()
	{
		
		//initAntecendents();
		initConsequents();
		initAntecendents();
		lastChord = "";
		currentBarIndex = 0;
	}
	
	private void initConsequents() {
		chordConsequents = new HashMap<String, LinkedList<String>>();
		chordConsequents.put("I", new LinkedList<String>(Arrays.asList("I", "IV", "V", "II", "III", "VI"))); //After I can go anything
		chordConsequents.put("II", new LinkedList<String>(Arrays.asList("II", "V", "I", "III"))); //Which can be after II
		chordConsequents.put("III", new LinkedList<String>(Arrays.asList("III", "IV", "I", "V", "VI", "VII")));
		chordConsequents.put("IV", new LinkedList<String>(Arrays.asList("IV", "V", "I", "II")));
		chordConsequents.put("V", new LinkedList<String>(Arrays.asList("V", "I", "VI", "III", "VII")));
		chordConsequents.put("VI", new LinkedList<String>(Arrays.asList("VI", "IV", "III", "I", "II")));
		chordConsequents.put("VII", new LinkedList<String>(Arrays.asList("VII", "I")));
		chordConsequents.put("all", new LinkedList<String>(Arrays.asList("I", "II", "III", "IV", "V", "VI", "VII")));
	}

	private void initAntecendents()
	{
		chordAntecendents = new HashMap<String, LinkedList<String>>();
		chordAntecendents.put("I", new LinkedList<String>(Arrays.asList("II", "III", "IV", "V", "VI", "VII"))); //After every chord can follow I
		chordAntecendents.put("II", new LinkedList<String>(Arrays.asList("I", "IV", "VI"))); //Which can be before II
		chordAntecendents.put("III", new LinkedList<String>(Arrays.asList("I", "II", "VI"))); //Etc
		chordAntecendents.put("IV", new LinkedList<String>(Arrays.asList("I", "III", "VI"))); 
		chordAntecendents.put("V", new LinkedList<String>(Arrays.asList("I", "II", "III", "IV"))); 
		chordAntecendents.put("VI", new LinkedList<String>(Arrays.asList("I", "III", "V"))); 
		chordAntecendents.put("VII", new LinkedList<String>(Arrays.asList("I", "III", "V"))); 
	}
	
	public HashMap<Double, Chord> getHarmonyForMotive(Motive m, Tonality cT)
	{
		currentTonality = cT;
		barsList = new LinkedList<List<SNPattern>>();
		halfByHalfHarmonization = false;
		beatsHarmonized = 0;
		
		HashMap<Double, Chord> harmony = new HashMap<Double, Chord>();
		HashMap<SNPattern, Chord> harmonizedMelody = new HashMap<SNPattern, Chord>();
		
		convertMotiveToBarsList(m);
		
		for (int i = 0; i < barsList.size(); i ++)
		{
			switch(currentBarIndex)
			{
				case 0:
				{
					harmonizedMelody.putAll(harmonizeFirstBar(barsList.get(i)));
					break;
				}
				case 3:
				{
					harmonizedMelody.putAll(harmonizeFourthBar(barsList.get(i)));
					break;
				}
				
				case 7:
				{
					harmonizedMelody.putAll(harmonizeEighthBar(barsList.get(i)));
					break;
				}
				
				default:
				{
					harmonizedMelody.putAll(harmonizeBar(barsList.get(i)));
				}
			}
			currentBarIndex ++;
			if(currentBarIndex > 8)
				currentBarIndex = 0;
		}
		
		
		double nextNotePosition = 0.0;
		for (SNPattern patt : m.firstRunStables)
		{
			harmony.put(nextNotePosition, harmonizedMelody.get(patt));
			nextNotePosition += patt.getAbsoluteLength() ;
		}
		
		//Это надо потом перенести в пост-обработку куда-нибудь
		
		//System.out.println("Harmony: " + harmony.toString());
		
		return harmony;
	}
	
	private HashMap<SNPattern, Chord> harmonizeFirstBar(List<SNPattern> stablesList) {
		HashMap<SNPattern, Chord> result = new HashMap<SNPattern, Chord>();
		
		lastChord = "I";
		analyzeBar(stablesList);
		result.put(stablesList.get(0), currentTonality.getChordByDegree(lastChord));
		
		chordsUsed = 1;
		
		result.putAll(harmonizeFurtherPart(stablesList.subList(1, stablesList.size())));
		
		return result;
	}
	
	private HashMap<SNPattern, Chord> harmonizeFourthBar(List<SNPattern> stablesList) {
		
		HashMap<SNPattern, Chord> result = new HashMap<SNPattern, Chord>();
		
		lastChord = "V";
		chordsUsed = 2;
		
		analyzeBar(stablesList);
		//result.put(stablesList.get(0), currentTonality.getChordByDegree(lastChord));
		for (SNPattern pattern : stablesList)
		{
			result.put(pattern, currentTonality.getChordByDegree(lastChord));
		}		

		return result;
	}
	
	private HashMap<SNPattern, Chord> harmonizeEighthBar(List<SNPattern> stablesList) {
		
		HashMap<SNPattern, Chord> result = new HashMap<SNPattern, Chord>();
		
		lastChord = "I";
		chordsUsed = 2;
		
		analyzeBar(stablesList);
		for (SNPattern pattern : stablesList)
		{
			result.put(pattern, currentTonality.getChordByDegree(lastChord));
		}		

		return result;
	}

	private HashMap<SNPattern, Chord> harmonizeBar(List<SNPattern> stablesList) {
		
		chordsUsed = 0;
		HashMap<SNPattern, Chord> result = new HashMap<SNPattern, Chord>();
		
		analyzeBar(stablesList);
		result.putAll(harmonizeFurtherPart(stablesList));
		
		return result;
	}
	
	private void analyzeBar(List<SNPattern> stablesList) {
		//Если у нас 4 четверти то первую половину гармонизируем одним аккордом а вторую другим
		if (stablesList.size() == 4)
		{
			halfByHalfHarmonization = true;
		}
	}

	private void convertMotiveToBarsList(Motive m)
	{
		int startId = 0;
		int i = 1;
		
		//Iterate through list of stables in motive
		while (i < m.firstRunStables.size())
		{
			if(m.firstRunStables.get(i).getRhythmValue() == 0) //Look for beginnings of bar
			{
				barsList.add(m.firstRunStables.subList(startId, i)); //If found - add sublist to bars list
				startId = i;
			}
			i++;
		}
		barsList.add(m.firstRunStables.subList(startId, m.firstRunStables.size())); //Add last bar to bars list
	}
	
	private HashMap<SNPattern, Chord> harmonizeFurtherPart(List<SNPattern> list)
	{
		HashMap<SNPattern, Chord> result = new HashMap<SNPattern, Chord>();
		
		HashMap<SNPattern, Chord> furtherPart = harmonizeSublist(list);
		
		//If we failed to harmonize
		if(furtherPart == null)
		{
			//Cannot harmonize. Sorry bro!
			Log.severe("Harmonizer > harmonizeSublist() > Harmonization failed. Last chord = " + lastChord);
			
			lastChord = "V";
			for (SNPattern pattern : list)
			{
				result.put(pattern, currentTonality.getChordByDegree(lastChord));
			}
		}
		else
		{
			result = furtherPart;
		}
		
		return result;
	}
	
	private HashMap<SNPattern, Chord> harmonizeSublist(List<SNPattern> list)
	{
		HashMap<SNPattern, Chord> result = new HashMap<SNPattern, Chord>();
		
		if(list.isEmpty())
		{
			return result;
		}
		
		int notePitch = list.get(0).getPitch();
		final int OCTAVE = 12;
		
		/**
		 *   armaxis (17:45:18 6/04/2013) 
			это у меня есть нота допустим МИ
			и есть тональность ля-мажор
			и я определяю какая по счету будет нота ми в ля-мажоре =) Сперва привожу их в одну октаву, потом делаю сдвиг и вычитаю
			ми = 52, ля = 69
			( ( 52 % 12 ) + 12 - (69 % 12) ) % 12
			( 4 + 12 - 9 ) % 12 = 7
			и таки да - ми в ля - седьмая по счету
		 *  */
		//TODO: Non-chord notes must be processed too!!!
		//int noteValueInTonality = ( ( (notePitch % OCTAVE) + OCTAVE ) - (currentTonality.getPitch() % OCTAVE) ) % OCTAVE;  
		//Everything above was wrong, we need absolute position of the note in gamma, independent from tonality.
		int noteValueInTonality = notePitch % OCTAVE;
		
		LinkedList<String> possibleChords = new LinkedList<String>(); 
		possibleChords.addAll(chordConsequents.get(lastChord)); //Get list of possible chords
		
		//If it's a beginning of bar we can also use any other chord, but high prioiry is on consequents
		if(chordsUsed == 0)
		{
			possibleChords.addAll(chordConsequents.get("all"));
		}
		
		if (halfByHalfHarmonization && chordsUsed == 1 && beatsHarmonized == 1)
		{
			result.put(list.get(0), currentTonality.getChordByDegree(lastChord));
			beatsHarmonized++;
			HashMap<SNPattern, Chord> furtherPart = harmonizeSublist(list.subList(1, list.size()));
			if (furtherPart == null)
			{
				beatsHarmonized--;
				return null;
			}
			result.putAll(furtherPart);
			return result;
		}
		
		//System.out.println("Notevalue = " + noteValueInTonality + " Pitch = " + notePitch + " Possible chords: " + possibleChords.toString());
		
		if(chordsUsed >= 2 && Math.random() > 0.7)
		{
			for (SNPattern pattern : list)
			{
				result.put(pattern, currentTonality.getChordByDegree(lastChord));
				
			}
			//System.out.println("2 beats already, harmonized with " + lastChord);
			return result;
		}
		
		String originalLastChord = lastChord;
		
		for (String ch : possibleChords)
		{
			//If note is a part of a possible chord. E.g. "7" is a part of chords [0, 3, 7] and [3, 7, 10].
			if ( containsInt(currentTonality.getChordByDegree(ch).getChord(), noteValueInTonality) )
			{
				//System.out.println("Trying to use " + ch);
				
				//If the chord is same as previous, but we are at the beginning of bar we need to reharmonize
				if(ch.equals(lastChord) && chordsUsed == 0)
				{
					//System.out.println("Need reharmonize ");
					continue;
				}
				
				//If this bar is no 3 - don't allow using V 
//				if(ch.equals("V") && currentBarIndex == 2)
//				{
//					continue;
//				}
				
				//If this bar is no 7 - don't allow using I
				if(ch.equals("I") && currentBarIndex == 6)
				{
				//	System.out.println("this bar is no 7 - don't allow using I");
					continue;
				}
				
				if (!lastChord.equals(ch))
					chordsUsed++;
				
				beatsHarmonized++;
				
				//Try to harmonize the rest;
				lastChord = ch;
				HashMap<SNPattern, Chord> furtherPart = harmonizeSublist(list.subList(1, list.size()));
				
				if(furtherPart == null) //If cannot be harmonized - continue to 
				{
				//	System.out.println("Cannot harmonize sublist, trying another ");
					chordsUsed--;
					beatsHarmonized--;
					lastChord = originalLastChord;
					continue;
				}
				else
				{
					//System.out.println("Harmonize sublist succeeded ");
					result.put(list.get(0), currentTonality.getChordByDegree(ch));
					result.putAll(furtherPart);
					return result;
				}
			}
		}

		return null;
	}
	
	private boolean containsInt(int[] array, int value)
	{
		for (int i = 0; i < array.length; i ++)
		{
			if (array[i] == value)
				return true;
		}
		
		return false;
	}
	
}
