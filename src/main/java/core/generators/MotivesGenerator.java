package core.generators;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import jm.JMC;
import util.Log;
import core.data.DataStorage;
import core.data.Melisma;
import core.data.Motive;
import core.data.PNote;
import core.data.Settings;
import core.data.patterns.CPattern;
import core.data.patterns.Pattern;
import core.data.patterns.SNPattern;
import core.data.tonality.Tonality;

public class MotivesGenerator extends BaseVoiceGenerator {

	private Motive previousMotive;
	private int firstNotePitch;
	LinkedList<List<Double>> lastBarRhythmLengths;
	
	public MotivesGenerator()
	{
		super();
		initLastBarRhythmLengths();
	}
	
	private void initLastBarRhythmLengths() {
		
		lastBarRhythmLengths = new LinkedList<List<Double>>();
		lastBarRhythmLengths.add(Arrays.asList(JMC.WHOLE_NOTE)); 															// O
		lastBarRhythmLengths.add(Arrays.asList(JMC.HALF_NOTE, JMC.HALF_NOTE));												// o + o
		lastBarRhythmLengths.add(Arrays.asList(JMC.QUARTER_NOTE, JMC.QUARTER_NOTE, JMC.HALF_NOTE));							// . + . + o
	}

	public Motive generateMotive(int pitch, Tonality cT, int octaveSummand)
	{
		currentMotive = new Motive();
		firstNotePitch = pitch + octaveSummand;
		currentTonality = cT;

		
		generateFirstRun(rhythmLengths);
		convertFirstRunToStables();

		generateSecondRun();
		convertSecondRunToStables();

		generateThirdRun();
		
		return currentMotive;
	}

	public Motive generateLastBarMotive(int pitch, Tonality cT, int octaveSummand)
	{
		//TODO: Here we can also add some volume things or specific stuff for last bar. For now it differs only by patterns lengths
		currentMotive = new Motive();
		firstNotePitch = pitch + octaveSummand;
		currentTonality = cT;
		generateFirstRun(lastBarRhythmLengths);
		convertFirstRunToStables();

		generateSecondRun();
		convertSecondRunToStables();

		generateThirdRun();
		
		return currentMotive;
	}
	
	/** 
	 * Creates main structure, skeleton which consists only of simple patterns of same length
	 */
	private void generateFirstRun(LinkedList<List<Double>> rhytms)
	{
		//Define length of items in one bar
		List<Double> rhythm = rhytms.get((new Random()).nextInt(rhytms.size()));

		//Add first note as SNPattern and set it as beginning of bar 
		PNote firstNote = new PNote(firstNotePitch, rhythm.get(0));
		firstNote.setRhythmValue(0);
		
		currentMotive.firstRunPatterns.add(new SNPattern(firstNote)); //First note in first run pattern.
		
		//Now loop until we fill whole bar with notes
		for (int i = 1; i < rhythm.size(); i++)
		{
			//TODO: Here goes balancing of upward and downward
			currentMotive.firstRunPatterns.add(
				new Pattern(
					DataStorage.getRandomPatternFromList(
						DataStorage.SIMPLE_PATTERNS_FOR_MOTIVE
					)
				)
			);
		}
		currentMotive.applyRhythm(rhythm, Motive.FIRST_RUN);
	}
	
	private void convertFirstRunToStables()
	{
		//Check if firstRunPatterns exists
		if(currentMotive.firstRunPatterns == null || currentMotive.firstRunPatterns.size() == 0)
		{
			Log.severe("Motives generator > convertFirstRunToStables() > Error! First run patterns list is undefined or empty!");
			return;
		}
		
		//Put first note without any changes. We believe that first note was STABLE
		currentMotive.firstRunStables.add( new SNPattern ( currentMotive.firstRunPatterns.getFirst() ) );
		
		//If there was only one note in pattern.. exit
		if(currentMotive.firstRunPatterns.size() == 1)
			return;
		
		//If there were more than one note - loop until end of firstRunPatterns, converting them to stables. Start from id = 1, because first one is already copied
		for (int i = 1; i < currentMotive.firstRunPatterns.size(); i ++)
		{
			//Elements used to create current Stable pattern: prev stable and current pattern
			Pattern currPattern = currentMotive.firstRunPatterns.get(i);
			SNPattern prevStablePattern = currentMotive.firstRunStables.get(i - 1);
			
			//Get curr pattern's values
			PNote firstNoteInCurrentPattern = currPattern.getNotes().get(0);
			int transitionType = firstNoteInCurrentPattern.getTransitionType();
			int[] transitionValue = firstNoteInCurrentPattern.getTransitionValue();
			int newStableNotePitch = DataStorage.getPitchByTransition(transitionType, transitionValue, prevStablePattern.getPitch(), currentTonality);
			
			double lengthChange = firstNoteInCurrentPattern.getLength();
			double newStableNoteLength = DataStorage.getAbsoluteLength(lengthChange, prevStablePattern.getAbsoluteLength());
			
			PNote newStableNote = new PNote(newStableNotePitch, newStableNoteLength);
			newStableNote.setRhythmValue(prevStablePattern.getFirstNote().getRhythmValue() + newStableNoteLength); //Calculate new position in bar
			currentMotive.firstRunStables.add(new SNPattern(newStableNote));
		}
	}	
	
	private void generateSecondRun()
	{
		for (int i = 0; i < currentMotive.firstRunStables.size(); i++)
		{
			CPattern currentPattern = new CPattern();
			
			if (Math.random() < 0.4 && currentMotive.firstRunStables.get(i).getAbsoluteLength() == 1.0) //TODO: Horrible checking but it should work
			{				
				//Calculate difference between notes
				int jumpValue = 0;
				
				//For last note - just get random one between -12 and 12
				if (i == currentMotive.firstRunStables.size() - 1)
				{
					jumpValue = new Random().nextInt(25) - 12;
				}
				else
				{
					//This will work because these patterns are actually SNPatterns
					jumpValue = currentMotive.firstRunStables.get(i + 1).getFirstNote().getPitch() 
								- currentMotive.firstRunStables.get(i).getFirstNote().getPitch();
				}
				
				//We don't need odd numbers, like 7 or 3 inside. Replace them with even ones: 6 or 2.
				if (jumpValue % 2 != 0)
				{
					jumpValue -= 1;
				}
				
				//If we don't have any entries for current jump - substract 1 until good one is found
				while(!DataStorage.MOVEMENT_PATTERNS_VALUED.containsKey(jumpValue)){
					jumpValue -= 1;
				}
				
				Pattern randomPatternForJump = DataStorage.getRandomPatternByJumpValue(jumpValue, DataStorage.MOVEMENT_PATTERNS_VALUED);
				
				currentPattern.addPattern(randomPatternForJump);
				//currentPattern.multiplyFirstNoteLength((1.0 / (randomPatternForJump.getNotes().size() + 1)));
				
			}
			else
			{
				//Else we leave note as is. We don't add any notes inside cpattern, so it will be empty = no figures will be added to SNPattern at convertSecondToStables  
			}
			
			currentMotive.secondRunPatterns.add(currentPattern);
		}
	}
	
	
	private void convertSecondRunToStables() 
	{
		for (int i = 0; i < currentMotive.firstRunStables.size(); i++)
		{
			SNPattern pattern = currentMotive.firstRunStables.get(i);
			
			//Merge together stable notes and CPatterns.
			CPattern newPattern = new CPattern();
			newPattern.addPattern(new SNPattern(pattern));
			newPattern.addPattern(new CPattern ( currentMotive.secondRunPatterns.get(i) ) );
			
			//Adjust note length
			newPattern.multiplyFirstNoteLength( ( 1.0 / ( currentMotive.secondRunPatterns.get(i).getNotes().size() + 1) ) );
			
			//Finally insert this pattern into SecondRunStables
			currentMotive.secondRunStables.add(newPattern);
		}
	}
	
	private void generateThirdRun()
	{
		for (int i = 0; i < currentMotive.secondRunStables.size(); i++)
		{
			//Copy note to third run
			CPattern newPattern = new CPattern((CPattern)currentMotive.secondRunStables.get(i));
			currentMotive.thirdRunPatterns.add(newPattern);
			
			//And add some melismas. Just a little bit.
			if(Math.random() < (0.0005 * Settings.MELISMAS_CHANCE) && Settings.USE_MELISMAS)
			{
				newPattern.getFirstNote().setMelismas(new Melisma(DataStorage.getRandomMelismaFromList(DataStorage.MELISMAS)));
			}
		}
	}
	
	
	
	public Motive generateSimilarMotive(Motive prevMotive, int pitch, Tonality cT, int octaveSummand)
	{
		currentMotive = new Motive();
		previousMotive = prevMotive;
		firstNotePitch = pitch + octaveSummand;
		currentTonality = cT;
		
		//We copy old first run patterns to new motive, then change them a bit..
		duplicatePatternsList(previousMotive.firstRunPatterns, currentMotive.firstRunPatterns);
		
		//Now change first note's pitch: add something between -2/+2
		//TODO Add some random to pitch
		firstNotePitch = currentTonality.getTonalityNoteByStep( new Random().nextInt(5) - 2, firstNotePitch);
		currentMotive.firstRunPatterns.get(0).getFirstNote().setPitch(firstNotePitch);
		
		//Now convert to Stables
		convertFirstRunToStables();

		//Now we copy old second run patterns to new motive, then change them a bit (or maybe leave them as is)
		duplicateCPatternsList(previousMotive.secondRunPatterns, currentMotive.secondRunPatterns);

		//Now convert second to Stables
		convertSecondRunToStables();
		
		//currentMotive.testNotate(currentMotive.firstRunPatterns);
		//currentMotive.testNotate(currentMotive.secondRunPatterns);
		
		generateThirdRun();
		
		return currentMotive;
	}
}
