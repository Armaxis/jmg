package core.generators.multigen;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import jm.JMC;
import core.data.DataStorage;
import core.data.Motive;
import core.data.PInstrument;
import core.data.PNote;
import core.data.chords.Chord;
import core.data.patterns.CPattern;
import core.data.patterns.Pattern;
import core.data.patterns.SNPattern;
import core.data.tonality.Tonality;
import core.generators.BaseVoiceGenerator;

public class SecondVoiceGenerator extends BaseVoiceGenerator{

	private HashMap<Double, Chord> harmony;
	private int lastNote;
	
	public SecondVoiceGenerator()
	{
		initRhythmLengths();
	}
	
	@Override
	public void setPossiblePatterns(PInstrument instrument)
	{
		possiblePatterns = DataStorage.SECOND_VOICE_PATTERNS; //TODO: Pinstrument can be used here to randomize patterns 
	}
	
	@Override
	protected void initRhythmLengths()
	{
		rhythmLengths = new LinkedList<List<Double>>();
		rhythmLengths.add(Arrays.asList(JMC.WHOLE_NOTE)); 															// O
		rhythmLengths.add(Arrays.asList(JMC.HALF_NOTE, JMC.HALF_NOTE));												// o + o
		rhythmLengths.add(Arrays.asList(JMC.HALF_NOTE, JMC.QUARTER_NOTE, JMC.QUARTER_NOTE));						// o + . + .
		rhythmLengths.add(Arrays.asList(JMC.DOTTED_HALF_NOTE, JMC.QUARTER_NOTE));									// o. + .
		rhythmLengths.add(Arrays.asList(JMC.QUARTER_NOTE, JMC.QUARTER_NOTE, JMC.QUARTER_NOTE, JMC.QUARTER_NOTE));	// . + . + . + .	
	}
	
	public Motive generateMotive(
			HashMap<Double, Chord> 	harm, 
			Tonality 				currentTonality, 
			int 					octaveSummand, 
			int 					lNote) {
		currentMotive = new Motive();
		
		this.currentTonality = currentTonality;
		this.harmony = harm;
		this.lastNote = lNote;
		
		//If we don't know how to start a phrase - we select from random note
		if(lNote == 0)
		{
			this.lastNote = harmony.get(0.0).getChord()[ (new Random()).nextInt(3) ] 
							+ 60 
							+ octaveSummand;
		}
		
		generateSecondRun();
		convertSecondRunToStables();
		
		generateThirdRun();
		return currentMotive;
	}
	
	public Motive generateMotive(
			HashMap<Double, Chord> 	harmony, 
			Tonality 				currentTonality, 
			int 					octaveSummand) 
	{	
		return  generateMotive(harmony, currentTonality, octaveSummand, 0);
	}
	
	public Motive generateMotive(
			HashMap<Double, Chord>	harmony, 
			Tonality 				currentTonality) 
	{	
		return  generateMotive(harmony, currentTonality, 0, 0);
	}
	
	private void generateSecondRun()
	{
		List<Double> rhythm = rhythmLengths.get((new Random()).nextInt(rhythmLengths.size()));
		
		//Add first note to second voice
		//http://cdn.memegenerator.net/instances/400x/35334325.jpg
		PNote firstNote = new PNote(lastNote, rhythm.get(0));
		firstNote.setRhythmValue(0);
		
		CPattern firstPattern = new CPattern();
		firstPattern.addPattern(new SNPattern(firstNote));
		currentMotive.secondRunPatterns.add(firstPattern); //First note in pattern.
		
		//Iterate through array of rhythms
		for (int i = 1; i < rhythm.size(); i++)
		{
			
			CPattern currentPattern = new CPattern();
			currentPattern.addPattern(
					new Pattern(
						DataStorage.getRandomPatternFromList( possiblePatterns )
					)
			);
			
			currentMotive.secondRunPatterns.add(currentPattern);
		}
		
		currentMotive.applyRhythm(rhythm, Motive.SECOND_RUN);
	}
	
	
	private void convertSecondRunToStables() 
	{
		for (int i = 0; i < currentMotive.secondRunPatterns.size(); i++)
		{
			//Finally insert this pattern into SecondRunStables
			currentMotive.secondRunStables.add(currentMotive.secondRunPatterns.get(i));
		}
	}
	

	private void generateThirdRun() {
		for (int i = 0; i < currentMotive.secondRunStables.size(); i++)
			currentMotive.thirdRunPatterns.add(new CPattern((CPattern)currentMotive.secondRunStables.get(i)));	
	}
}
