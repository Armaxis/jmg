package core.generators;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import jm.JMC;
import util.Log;
import core.data.Motive;
import core.data.PInstrument;
import core.data.patterns.CPattern;
import core.data.patterns.Pattern;
import core.data.tonality.Tonality;

public class BaseVoiceGenerator {

	protected Motive currentMotive;
	protected Tonality currentTonality;
	protected LinkedList<List<Double>> rhythmLengths;
	protected LinkedList<Pattern> possiblePatterns;

	public BaseVoiceGenerator() {
		initRhythmLengths();
	}

	public Motive generateMotive(Object... arguments)
	{
		Log.severe("Base Motive Generator > Error! Method generateMotive() is unimplemented!");
		return null;
	}
	
	protected void initRhythmLengths() {
		rhythmLengths = new LinkedList<List<Double>>();
		//rhythmLengths.add(Arrays.asList(JMC.WHOLE_NOTE)); 														// O
		rhythmLengths.add(Arrays.asList(JMC.HALF_NOTE, JMC.HALF_NOTE));												// o + o
		rhythmLengths.add(Arrays.asList(JMC.HALF_NOTE, JMC.QUARTER_NOTE, JMC.QUARTER_NOTE));						// o + . + .
		rhythmLengths.add(Arrays.asList(JMC.DOTTED_HALF_NOTE, JMC.QUARTER_NOTE));									// o. + .
		rhythmLengths.add(Arrays.asList(JMC.QUARTER_NOTE, JMC.QUARTER_NOTE, JMC.QUARTER_NOTE, JMC.QUARTER_NOTE));	// . + . + . + .	
	}

	public void setPossiblePatterns(PInstrument instrument)
	{
		possiblePatterns = null; 
	}
	
	protected void duplicatePatternsList(LinkedList<Pattern> oldList, LinkedList<Pattern> newList) 
	{
		for (Pattern p : oldList)
		{
			newList.add(new Pattern(p));
		}
	}

	protected void duplicateCPatternsList(LinkedList<CPattern> oldList, LinkedList<CPattern> newList) 
	{
		for (CPattern p : oldList)
		{
			newList.add(new CPattern(p));
		}
	}

	protected void duplicatePatternsListAsComplexPattern(LinkedList oldList, LinkedList newList) 
	{
		for (Object p : oldList)
		{
			CPattern newPattern = new CPattern();
			newPattern.addPattern(new Pattern((Pattern)p));
			newList.add(newPattern);
		}
	}

}