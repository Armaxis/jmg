package core.data;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Random;

import jm.music.data.Phrase;
import util.Log;
import util.Tools;
import core.data.chords.Chord;
import core.data.structure.MotiveStructure;
import core.data.structure.MotiveStructureStorage;
import core.data.structure.PhraseStructure;
import core.data.tonality.Tonality;
import core.generators.BaseVoiceGenerator;
import core.generators.MotivesGenerator;
import core.generators.multigen.AccompanimentGenerator;
import core.generators.multigen.SecondVoiceGenerator;

public class Voice {
	
	public static final int MELODY 			= 0;
	public static final int SECOND_VOICE	= 1;
	public static final int ACCOMPANIMENT 	= 2;
	
	public int 					role;
	public String 				name;
	public int 					channel;
	//public int 					instrument;
	public int					volume;
	public int 					octaveSummand;
	public PInstrument 			pinstrument;
	private BaseVoiceGenerator 	voiceGenerator;
	private Motive 				motive;
	private int 				lastNote;
	private boolean				muted;

	private LinkedList<PhraseStructure> allPhraseStructures;

	/**
	 * @param role
	 * @param name
	 * @param instrument
	 * @param channel
	 * @param volume
	 * @param octaveSummand
	 */
//	public Voice(
//			int role, 
//			String name,
//			int instrument,
//			int channel,
//			int volume,
//			int octaveSummand) {
//		initVoice(role, name, instrument, channel, volume, octaveSummand);
//	}
	
	/**
	 * @param role
	 * @param name
	 * @param pInstrument
	 * @param channel
	 * @param volume
	 */
	public Voice(
			int role, 
			String name,
			PInstrument pInstrument,
			int channel,
			int volume) {
		initVoice(role, name, pInstrument, channel, volume, 0);
	}
	
	/**
	 * @param role
	 * @param name
	 * @param instrument
	 * @param channel
	 */
	public Voice(
			int role, 
			String name,
			PInstrument instrument,
			int channel) {
		initVoice(role, name, instrument, channel, 100, 0);
	}
	
	public Voice() {
		initVoice(MELODY, "Main Melody", DataStorage.getInstrumentByName("Piano"), 1, 0, 100);
	}
	
	private void initVoice(
			int role, 
			String name,
			PInstrument instrument,
			int channel,
			int volume,
			int octaveSummand) {
		this.role = role;
		this.name = name;
		this.pinstrument = instrument;
		this.channel = channel;
		this.volume = volume;
		this.octaveSummand = Tools.octaveIndexToShift(instrument.octaveIndex);
		this.muted = false;
		allPhraseStructures = new LinkedList<PhraseStructure>();
		
		updateRole();
	}
	
	public Phrase getNextPhrase()
	{
		return null;
	}
	
	/** Generates a motive (main melody)
	 * @param pitch
	 * @param cT
	 */
	public void generateMotive(Tonality cT)
	{

		//If there are no phrases at all, or the last phrase is finished
		if(allPhraseStructures.size() == 0 || allPhraseStructures.getLast().hasAllMotives())
		{
			//get random motive structure
			MotiveStructure mStructure = MotiveStructureStorage.MOTIVES_STRUCTURES.get(new Random().nextInt(MotiveStructureStorage.MOTIVES_STRUCTURES.size()));
			//create new phraseStructure
			allPhraseStructures.add(new PhraseStructure(mStructure));
			Log.info("Multi Voice Generator > getScore() > New Phrase Structure created! Phrase: " + allPhraseStructures.getLast().toString());
		}
		
		PhraseStructure currentPStructure = allPhraseStructures.getLast();
		
		switch (currentPStructure.getRequiredGenerationType()) {
			case PhraseStructure.MOTIVE_EXISTS:
				motive = currentPStructure.getExistingMotive();
				break;
			
			case PhraseStructure.NEW_MOTIVE:
				motive = ((MotivesGenerator)voiceGenerator).generateMotive(lastNote, cT, octaveSummand);
				currentPStructure.addMotive(motive);
				break;
				
			case PhraseStructure.SIMILAR_MOTIVE:
				motive = ((MotivesGenerator)voiceGenerator).generateSimilarMotive(currentPStructure.getMotiveForSimilar(), lastNote, cT, octaveSummand);
				currentPStructure.addMotive(motive);
				break;
				
			case PhraseStructure.LAST_BAR:
				motive = ((MotivesGenerator)voiceGenerator).generateLastBarMotive(lastNote, cT, octaveSummand);
				currentPStructure.addMotive(motive);
				break;
				
			default:
				motive = ((MotivesGenerator)voiceGenerator).generateMotive(lastNote, cT, octaveSummand);
				break;
		}
		
		setMotiveVolume(volume);
		
		currentPStructure.iterate(); //Iterate pstructure, so next time it will work with another motive
		
	}
	

	/** Generates motive (second voice & accompaniment)
	 * @param harmony
	 * @param currentTonality
	 */
	public void generateMotive(HashMap<Double, Chord> harmony, Tonality currentTonality)
	{
		switch (role) {
		case MELODY:
			//Do nothing. For main melody motive already exists
			break;
			
		case SECOND_VOICE:
			motive = ((SecondVoiceGenerator)voiceGenerator).generateMotive(harmony, currentTonality, octaveSummand);
			break;
			
		case ACCOMPANIMENT:
			motive = ((AccompanimentGenerator)voiceGenerator).generateMotive(harmony, currentTonality, octaveSummand, lastNote);
			break;
		
		default:
			voiceGenerator = new MotivesGenerator();
			break;
		}
		
		setMotiveVolume(volume);
	}
	
	public Phrase getPhrase(HashMap<Double, Chord> harmony, Tonality currentTonality) {
		return motive.getPhrase(harmony, currentTonality);
	}

	public void setLastNote(int lastNote) {
		this.lastNote = lastNote;
	}
	
	public Motive getMotive() {
		return motive;
	}
	
	public void setMotiveVolume(int volume)
	{
		motive.setOverallVolume((int)(volume * ((double)Settings.OVERALL_VOLUME / 100)));
	}

	public boolean isMuted() {
		return muted;
	}

	public void setMuted(boolean muted) {
		this.muted = muted;
	}
	
	public void updateRole()
	{
		switch (role) {
		case MELODY:
			voiceGenerator = new MotivesGenerator();
			break;
			
		case SECOND_VOICE:
			voiceGenerator = new SecondVoiceGenerator();
			break;
			
		case ACCOMPANIMENT:
			voiceGenerator = new AccompanimentGenerator();
			break;
		
		default:
			voiceGenerator = new MotivesGenerator();
			break;
		}
		voiceGenerator.setPossiblePatterns(pinstrument);
	}

	public void setPinstrument(PInstrument _pinstrument) {
		this.pinstrument = _pinstrument;
		this.octaveSummand = _pinstrument.octaveIndex;
		this.voiceGenerator.setPossiblePatterns(_pinstrument);
	}
	
	public void refreshAccompanimentPattern()
	{
		if (voiceGenerator instanceof AccompanimentGenerator)
			((AccompanimentGenerator)voiceGenerator).setCurrentRandomPattern();
	}
}
