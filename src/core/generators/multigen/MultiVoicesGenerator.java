package core.generators.multigen;

import java.util.HashMap;
import java.util.LinkedList;

import jm.JMC;
import jm.music.data.Part;
import jm.music.data.Phrase;
import jm.music.data.Score;
import util.Log;
import core.data.Settings;
import core.data.Voice;
import core.data.chords.Chord;
import core.data.tonality.MajorTonality;
import core.generators.Harmonizer;
import core.generators.IMusicGenerator;
import core.record.RecordsManager;

public class MultiVoicesGenerator implements IMusicGenerator {

	//private Tonality currentTonality;
	private Harmonizer harmonizer;
	private LinkedList<Voice> voices;
	
	
	@Override
	public void init() {
		Settings.CURRENT_TONALITY 	= new MajorTonality(JMC.C4);
		//currentTonality 			= new MinorTonality(JMC.C4);
		harmonizer 					= new Harmonizer();
		//harmonizer 				= new HarmonizerDummy();
		initVoices();
	}

	private void initVoices()
	{
		voices = new LinkedList<Voice>();
		//voices.add(new Voice(Voice.MELODY, "Piano", JMC.PIANO, 1, 100));
		//voices.add(new Voice(Voice.SECOND_VOICE, "Violin", JMC.VIOLIN, 2, 12));
		//voices.add(new Voice(Voice.SECOND_VOICE, "Cello", JMC.CELLO, 3, -12));
		//voices.add(new Voice(Voice.ACCOMPANIMENT, "Piano accomp", JMC.PIANO, 4));
		//voices.add(new Voice(Voice.MELODY, "Piano", JMC.PIANO, 5));
	}
	
	private HashMap<Double, Chord> getVoicesHarmony()
	{
		for (Voice v : voices)
		{
			if(v.role == Voice.MELODY)
			{
				return harmonizer.getHarmonyForMotive(v.getMotive(), Settings.CURRENT_TONALITY);
			}
		}
		Log.severe("Multi Voice Generator > getVoicesHarmony() > Error! Can't find main melody in voices list!");
		return null;
	}
	
	private void generateMainMotive()
	{
		for (Voice v : voices)
		{
			if(v.role == Voice.MELODY)
			{
				v.setLastNote(Settings.CURRENT_TONALITY.getRandomMainNotePitched());
				v.generateMotive(Settings.CURRENT_TONALITY);
				break;
			}
		}
	}
	
	private boolean needGenerate()
	{
		for (Voice v : voices)
		{
			if (!v.isMuted())
				return true;
		}
		
		//No voices or all are muted
		return false;
	}
	
	public void updateBarCounterAndRefresh()
	{
		//Инкременирует счетчик тактов. 
		Settings.accompanimentBarCounter++;
		RecordsManager.incrementCounter();
		
		//Если прошло 4 такта то у всех аккомпанементов меняет структуру
		if(Settings.accompanimentBarCounter >= 4)
		{
			for (Voice v : voices)
			{
				v.refreshAccompanimentPattern();
			}
			Settings.accompanimentBarCounter = 0;
		}
	}
	
	@Override
	public Score getScore() {
		
		//Create new score
		Score s = new Score();
		
		if(!needGenerate())
			return s;
		
		//Generate main melody (search for main voice and ask it to generate)
		generateMainMotive();
		
		//Harmonize main melody (search for main voice and ask it to harmonize)
		HashMap<Double, Chord> harmony = getVoicesHarmony();
		
		for (Voice v : voices)
		{
			//Skip muted voices
			if(v.isMuted())
				continue;
			
			//Generate motive for each voice
			v.generateMotive(harmony, Settings.CURRENT_TONALITY);
			
			//Get phrase from that motive
			Phrase phr = v.getPhrase(harmony, Settings.CURRENT_TONALITY);
			
			//Set last note in voice
			v.setLastNote(phr.getNote(0).getPitch());
			
			//And finally add to score
			s.add(new Part(phr, v.name, v.pinstrument.midiId, v.channel));
		}
		
		updateBarCounterAndRefresh();
		
		return s;
	}

	@Override
	public void addVoice(Voice v) {
		voices.add(v);
	}

	@Override
	public void removeVoice(Voice v) {
		voices.remove(v);
	}

}
