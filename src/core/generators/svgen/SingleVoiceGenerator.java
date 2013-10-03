package core.generators.svgen;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Random;

import jm.JMC;
import jm.music.data.Note;
import jm.music.data.Part;
import jm.music.data.Phrase;
import jm.music.data.Score;
import util.Log;
import core.data.Motive;
import core.data.Voice;
import core.data.chords.Chord;
import core.data.structure.MotiveStructure;
import core.data.structure.MotiveStructureStorage;
import core.data.structure.PhraseStructure;
import core.data.tonality.MajorTonality;
import core.data.tonality.Tonality;
import core.generators.Harmonizer;
import core.generators.IMusicGenerator;
import core.generators.MotivesGenerator;

public class SingleVoiceGenerator implements IMusicGenerator {

	private boolean firstTimeRun;
	private LinkedList<Motive> motives;
	private Tonality currentTonality;
	private Harmonizer harmonizer;
	private MotivesGenerator motivesGenerator;
	private int lastNote;
	
	private LinkedList<PhraseStructure> allPhraseStructures;
	
	@Override
	public void init() {
		firstTimeRun 		= true;
		motives 			= new LinkedList<Motive>();
		currentTonality 	= new MajorTonality(JMC.C4);
		//currentTonality 	= new MinorTonality(JMC.C3);
		harmonizer 			= new Harmonizer();
		motivesGenerator	= new MotivesGenerator();
		allPhraseStructures = new LinkedList<PhraseStructure>();
		lastNote 			= currentTonality.getRandomMainNotePitched();
	}

	@Override
	public Score getScore() {
		Phrase phr = null;
		
		//If there are no phrases at all, or the last phrase is finished
		if(allPhraseStructures.size() == 0 || allPhraseStructures.getLast().hasAllMotives())
		{
			//get random motive structure
			MotiveStructure mStructure = MotiveStructureStorage.MOTIVES_STRUCTURES.get(new Random().nextInt(MotiveStructureStorage.MOTIVES_STRUCTURES.size()));
			//create new phraseStructure
			allPhraseStructures.add(new PhraseStructure(mStructure));
			Log.info("Single Voice Generator > getScore() > New Phrase Structure created! Phrase: " + allPhraseStructures.getLast().toString());
		}
		
		PhraseStructure currentPStructure = allPhraseStructures.getLast();
		
		Motive currentMotive;
		
		switch (currentPStructure.getRequiredGenerationType()) {
		case PhraseStructure.MOTIVE_EXISTS:
			currentMotive = currentPStructure.getExistingMotive();
			break;
		
		case PhraseStructure.NEW_MOTIVE:
			currentMotive = motivesGenerator.generateMotive(lastNote, currentTonality);
			currentPStructure.addMotive(currentMotive);
			break;
			
		case PhraseStructure.SIMILAR_MOTIVE:
			currentMotive = motivesGenerator.generateSimilarMotive(currentPStructure.getMotiveForSimilar(), lastNote, currentTonality, 12);
			currentPStructure.addMotive(currentMotive);
			break;
			
		default:
			currentMotive = motivesGenerator.generateMotive(lastNote, currentTonality);
			break;
		}
		
		currentPStructure.iterate(); //Iterate pstructure, so next time it will work with another motive
		
		HashMap<Double, Chord> harmony = harmonizer.getHarmonyForMotive(currentMotive, currentTonality);
		phr = currentMotive.getSecondPhrase(harmony, currentTonality);
		lastNote = ((Note)(phr.getNoteList().firstElement())).getPitch();
//		
//		if(firstTimeRun)
//		{
//			Motive m1 = motivesGenerator.generateMotive(currentTonality.getRandomMainNotePitched(), currentTonality);
//			HashMap<Double, Chord> harmony = harmonizer.getHarmonyForMotive(m1);
//			phr = m1.getSecondPhrase(harmony, currentTonality);
//			lastNote = ((Note)(phr.getNoteList().lastElement())).getPitch();
//			motives.add(m1);
//			firstTimeRun = false;
//		}
//		else
//		{
//			Motive m2 = motivesGenerator.generateSimilarMotive(motives.getFirst(), lastNote, currentTonality);
//			HashMap<Double, Chord> harmony = harmonizer.getHarmonyForMotive(m2);
//			phr = m2.getSecondPhrase(harmony, currentTonality);
//			lastNote = ((Note)(phr.getNoteList().lastElement())).getPitch();
//		}
		
		Score s = new Score();
		s.add(new Part(phr, "First phrase", JMC.PIANO));
		
		return s;
	}

	@Override
	public void addVoice(Voice v) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeVoice(Voice v) {
		// TODO Auto-generated method stub
		
	}

}
