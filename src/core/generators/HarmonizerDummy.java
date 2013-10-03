package core.generators;

import java.util.HashMap;

import core.data.Motive;
import core.data.chords.Chord;
import core.data.tonality.MajorTonality;
import core.data.tonality.Tonality;

public class HarmonizerDummy extends Harmonizer{

	private HashMap<Double, Chord> harmony;
	private Tonality currentTonality;

	
	public HarmonizerDummy ()
	{
		harmony = new HashMap<Double, Chord>();

	}
	
	
	@Override
	public HashMap<Double, Chord> getHarmonyForMotive(Motive m, Tonality cT)
	{

		//Chord cd = new MajorChord(60);
		//Chord cd = new MinorChord(48);
		Chord cd = new MajorTonality(60).getRandomDegreeChord();
		harmony.put((double) 0, cd);
		
		return harmony;
	}
	
}
