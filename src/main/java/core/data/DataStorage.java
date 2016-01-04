package core.data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import jm.JMC;
import jm.music.data.Note;
import jm.music.data.Phrase;
import util.Log;
import util.Tools;
import core.data.chords.Chord;
import core.data.patterns.Pattern;
import core.data.patterns.SNPattern;
import core.data.tonality.Tonality;

public class DataStorage {
	
	public static LinkedList<Pattern> SIMPLE_PATTERNS;
	public static LinkedList<Pattern> SIMPLE_TONAL_PATTERNS;
	public static LinkedList<Pattern> SIMPLE_CHORD_PATTERNS;
	public static LinkedList<Pattern> SIMPLE_CHROMATIC_PATTERNS;
	public static LinkedList<Pattern> SIMPLE_SAME_PATTERNS;
	public static LinkedList<Pattern> SIMPLE_PATTERNS_FOR_MOTIVE;
	public static LinkedList<Pattern> BASE_ACCOMPANIMENT_PATTERNS;
	public static HashMap<PInstrument, LinkedList<Pattern>> INSTRUMENTS_ACCOMPANIMENTS;
	
	
	public static LinkedList<Pattern> SECOND_VOICE_PATTERNS;
	public static LinkedList<Pattern> MOVEMENT_PATTERNS;
	public static HashMap<Integer, LinkedList<Pattern>> MOVEMENT_PATTERNS_VALUED; //Integer - movements jump value
	public static LinkedList<PInstrument> INSTRUMENTS;
	public static LinkedList<Melisma> MELISMAS;
	
	public static void init()
	{
		SIMPLE_PATTERNS 			= new LinkedList<Pattern>();
		SIMPLE_TONAL_PATTERNS 		= new LinkedList<Pattern>();
		SIMPLE_CHORD_PATTERNS 		= new LinkedList<Pattern>();
		SIMPLE_CHROMATIC_PATTERNS 	= new LinkedList<Pattern>();
		SIMPLE_SAME_PATTERNS 		= new LinkedList<Pattern>();
		SIMPLE_PATTERNS_FOR_MOTIVE 	= new LinkedList<Pattern>();
		
		parseFileIntoList("database/SimplePatterns.txt", SIMPLE_PATTERNS);
		
		for(Pattern p : SIMPLE_PATTERNS)
		{
			if(p.getName().toLowerCase().contains("same"))
				SIMPLE_SAME_PATTERNS.add(p);
			
			if(p.getName().toLowerCase().contains("chord"))
				SIMPLE_CHORD_PATTERNS.add(p);
			
			if(p.getName().toLowerCase().contains("chromatic"))
				SIMPLE_CHROMATIC_PATTERNS.add(p);
			
			if(p.getName().toLowerCase().contains("tonal"))
				SIMPLE_TONAL_PATTERNS.add(p);
		}
		
		//SIMPLE_PATTERNS_FOR_MOTIVE.addAll(SIMPLE_CHROMATIC_PATTERNS);
		SIMPLE_PATTERNS_FOR_MOTIVE.addAll(SIMPLE_TONAL_PATTERNS);
		SIMPLE_PATTERNS_FOR_MOTIVE.addAll(SIMPLE_SAME_PATTERNS);
		
		MOVEMENT_PATTERNS = new LinkedList<Pattern>();
		parseFileIntoList("database/MovementPatterns.txt", MOVEMENT_PATTERNS);
		MOVEMENT_PATTERNS_VALUED = new HashMap<Integer, LinkedList<Pattern>>();
		for(Pattern i : MOVEMENT_PATTERNS)
		{
			Pattern p = new Pattern(i);
			int patternJump = p.getOverallPatternJumpLength();
			if(MOVEMENT_PATTERNS_VALUED.containsKey(patternJump))
			{
				MOVEMENT_PATTERNS_VALUED.get(patternJump).add(p);
			}
			else
			{
				LinkedList<Pattern> newList = new LinkedList<Pattern>();
				newList.add(p);
				MOVEMENT_PATTERNS_VALUED.put(patternJump, newList);
			}
		}
		
		BASE_ACCOMPANIMENT_PATTERNS = new LinkedList<Pattern>();
		parseFileIntoList("database/accompaniments/BaseAccompanimentPatterns.txt", BASE_ACCOMPANIMENT_PATTERNS);
		
		SECOND_VOICE_PATTERNS = new LinkedList<Pattern>();
		parseFileIntoList("database/SecondVoicePatterns.txt", SECOND_VOICE_PATTERNS);
		
		MELISMAS = new LinkedList<Melisma>();
		MELISMAS.add(new Melisma(Melisma.GRACE, -1));
		MELISMAS.add(new Melisma(Melisma.GRACE, 1));
		
		MELISMAS.add(new Melisma(Melisma.GRACE, -2));
		MELISMAS.add(new Melisma(Melisma.GRACE, 2));
		
		MELISMAS.add(new Melisma(Melisma.TRILL, -1));
		MELISMAS.add(new Melisma(Melisma.TRILL, 1));
		
		//Filling list of available instruments
		INSTRUMENTS = new LinkedList<PInstrument>();
		INSTRUMENTS.add(new PInstrument("Piano", "Фортепиано", JMC.PIANO, 3));
		INSTRUMENTS.add(new PInstrument("Harpsichord", "Клавесин", JMC.HARPSICHORD, 3));
		INSTRUMENTS.add(new PInstrument("Organ", "Орган", JMC.CHURCH_ORGAN, 2));
		INSTRUMENTS.add(new PInstrument("Vibraphone", "Вибрафон", JMC.VIBES, 4));
		INSTRUMENTS.add(new PInstrument("Xylophone", "Ксилофон", JMC.XYLOPHONE, 4));
		INSTRUMENTS.add(new PInstrument("Glockenspiel", "Колокольчики", JMC.GLOCKENSPIEL, 4));
		INSTRUMENTS.add(new PInstrument("Chimes", "Колокола", JMC.TUBULAR_BELL, 2));
		INSTRUMENTS.add(new PInstrument("Harp", "Арфа", JMC.HARP, 3));
		INSTRUMENTS.add(new PInstrument("Violin", "Скрипка", JMC.VIOLIN, 4));
		INSTRUMENTS.add(new PInstrument("Viola", "Альт", JMC.VIOLA, 3));
		INSTRUMENTS.add(new PInstrument("Cello", "Виолончель", JMC.CELLO, 2));
		INSTRUMENTS.add(new PInstrument("Contrabass", "Контрабасс", JMC.DOUBLE_BASS, 1));
		INSTRUMENTS.add(new PInstrument("Strings", "Струнные", JMC.STRING_ENSEMBLE_1, 3));
		INSTRUMENTS.add(new PInstrument("Guitar", "Гитара", JMC.GUITAR, 3));
		INSTRUMENTS.add(new PInstrument("Flute", "Флейта", JMC.FLUTE, 4));
		INSTRUMENTS.add(new PInstrument("Clarinet", "Кларнет", JMC.CLARINET, 3));
		INSTRUMENTS.add(new PInstrument("Oboe", "Гобой", JMC.OBOE, 3));
		INSTRUMENTS.add(new PInstrument("Bassoon", "Фагот", JMC.BASSOON, 2));
		INSTRUMENTS.add(new PInstrument("Bagpipe", "Волынка", JMC.BAGPIPE, 3));
		INSTRUMENTS.add(new PInstrument("Shakuhachi", "Сякухати", JMC.SHAKUHACHI, 3));
		INSTRUMENTS.add(new PInstrument("Saxophone", "Саксофон", JMC.SAX, 2));
		INSTRUMENTS.add(new PInstrument("Trumpet", "Труба", JMC.TRUMPET, 3));
		INSTRUMENTS.add(new PInstrument("Trombone", "Тромбон", JMC.TROMBONE, 2));
		INSTRUMENTS.add(new PInstrument("Tuba", "Туба", JMC.TUBA, 2));
		INSTRUMENTS.add(new PInstrument("Choir", "Хор", JMC.AAH, 3));
		
//		INSTRUMENTS.add(new PInstrument("88", "88", JMC.FANTASIA, 3));
//		INSTRUMENTS.add(new PInstrument("89", "89", 89, 3));
//		INSTRUMENTS.add(new PInstrument("90", "90", 90, 3));
//		INSTRUMENTS.add(new PInstrument("91", "91", 91, 3));
//		INSTRUMENTS.add(new PInstrument("92", "92", 92, 3));
//		INSTRUMENTS.add(new PInstrument("93", "93", 93, 3));
//		INSTRUMENTS.add(new PInstrument("94", "94", 94, 3));
//		INSTRUMENTS.add(new PInstrument("95", "95", 95, 3));
//		INSTRUMENTS.add(new PInstrument("96", "96", 96, 3));
//		INSTRUMENTS.add(new PInstrument("97", "97", 97, 3));
//		INSTRUMENTS.add(new PInstrument("98", "98", 98, 3));
//		INSTRUMENTS.add(new PInstrument("99", "99", 99, 3));
//		INSTRUMENTS.add(new PInstrument("100", "100", 100, 3));
//		INSTRUMENTS.add(new PInstrument("101", "101", 101, 3));
//		INSTRUMENTS.add(new PInstrument("102", "102", 102, 3));
//		INSTRUMENTS.add(new PInstrument("103", "103", 103, 3));
		
		INSTRUMENTS_ACCOMPANIMENTS = new HashMap<PInstrument, LinkedList<Pattern>>();
		
		for (PInstrument pi : INSTRUMENTS)
		{
			addInstrumentAccompanimentPattern(pi);
		}
		
	}
	
	private static void addInstrumentAccompanimentPattern(PInstrument instrument)
	{
		LinkedList<Pattern> list = new LinkedList<Pattern>();
		parseFileIntoList("database/accompaniments/" + instrument.name + "Patterns.txt", list);
		INSTRUMENTS_ACCOMPANIMENTS.put(instrument, list);
	}
	
	public static PInstrument getInstrumentByName(String name)
	{
		for (PInstrument pi : INSTRUMENTS)
		{
			if (pi.name.equals(name))
				return pi;
		}
		return null;
	}
	
	private static void parseFileIntoList(String filename, LinkedList<Pattern> list)
	{
		File f = new File(filename);
		if (!f.exists())
			return;
		
		BufferedReader in;
		try {
			in = new BufferedReader(new FileReader(f));
			String inputLine;
			while ((inputLine = in.readLine()) != null) {
				if(inputLine.startsWith("//")) //Skip comments
					continue; 
				if(inputLine.startsWith("Name~")) //Start of new pattern; Parse till end
				{
					String name = inputLine.substring(inputLine.indexOf("~") + 1); //Get name
					String data = "";
					while(!(inputLine = in.readLine()).startsWith("End"))
					{
						data = data + inputLine + ";";
					}
					Pattern p = new Pattern(data);
					p.setName(name);
					list.add(p);
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static Phrase patternsToPhrase(
			List<Pattern> 			patterns, 
			HashMap<Double, Chord> 	harmony, 
			Tonality 				currentTonality
		)
	{
		Phrase phr = new Phrase();
		
		//Get all the PNotes from all patterns
		LinkedList<PNote> notes = new LinkedList<PNote>();
		for (Pattern p : patterns)
			notes.addAll(p.getNotes());
		
		if(notes.getFirst().getTransitionType() != PConstants.STABLE_NOTE)
		{
			Log.severe("DataStorage > patternsToPhrase > Error converting to phrase. First note is not beginning.");
			return null;
		}
		
		Note lastNote = null;
		double barPosition = 0.0;
		for (PNote note : notes)
		{
			if (note.getTransitionType() == PConstants.STABLE_NOTE)
			{
				if(note.hasMelismas())
				{
					Melisma melisma = note.getMelismas();
					switch (melisma.getType())
					{
					case Melisma.GRACE:
						//First, we add a grace note
						Note melismaNote = new Note(note.getPitch() + melisma.getPitchValue(), JMC.THIRTYSECOND_NOTE, note.getVolume() - 5);
						phr.add(melismaNote);
						
						//Then we add main note
						phr.add(new Note(note.getPitch(),  getAbsoluteLength(note.getLength(), note.getAbsolute_length()) - JMC.THIRTYSECOND_NOTE, note.getVolume()));
						
						break;
						
					case Melisma.TRILL:
						double trillLength = getAbsoluteLength(note.getLength(), note.getAbsolute_length());
						int trillMultiplier = 0;
						while (trillLength > 0)
						{
							phr.add(new Note(note.getPitch() + melisma.getPitchValue() * trillMultiplier, JMC.THIRTYSECOND_NOTE, note.getVolume()));
							trillMultiplier = ( trillMultiplier + 1 ) % 2;
							trillLength -= JMC.THIRTYSECOND_NOTE;
						}
						break;
					}
				}
				else
				{
					phr.add(new Note(note.getPitch(),  getAbsoluteLength(note.getLength(), note.getAbsolute_length()), note.getVolume()));
				}
				
				//Define last note independently. Any melismas doesn't affect this var.
				lastNote = new Note(note.getPitch(),  getAbsoluteLength(note.getLength(), note.getAbsolute_length()), note.getVolume());
				
			}
			else
			{
				//TODO: volume changes still doesn't work. Meh.
				Chord cd =  harmony.get(Tools.nearestKey(harmony, barPosition)); 
				Note n = new Note(getPitchByTransition(note.getTransitionType(),
														note.getTransitionValue(),
														lastNote.getPitch(), cd, currentTonality),
								  getAbsoluteLength(note.getLength(), lastNote.getRhythmValue()),
								  lastNote.getDynamic()
								  );
				phr.add(n);
				lastNote = n;
			}
			barPosition += lastNote.getRhythmValue();
		}
		
		return phr;
	}
	
	/** Get pitch based on transition parameters and other stuff. 
	 * @param transitionType
	 * @param transitionValue
	 * @param pitch
	 * @param cd
	 * @param currentTonality
	 * @return
	 */
	public static int getPitchByTransition(
			int 		transitionType, 
			int[] 		transitionValue, 
			int 		pitch, 
			Chord 		cd, 
			Tonality 	currentTonality
		) 
	{
		switch (transitionType) {
		case PConstants.SAME_PITCH:
			return pitch;
			
		case PConstants.CHORD_JUMP: 
			return Tools.getElementInArrayByStep(transitionValue[0], cd.getAllChordNotes(), pitch);
		
		case PConstants.CHROMATIC_JUMP:
			return pitch + transitionValue[0];
			
		case PConstants.TONALITY_JUMP:
			return currentTonality.getTonalityNoteByStep(transitionValue[0], pitch);

		case PConstants.COMPLEX:
			int chordResult = Tools.getElementInArrayByStep(transitionValue[0], cd.getAllChordNotes(), pitch);
			int tonalityResult = currentTonality.getTonalityNoteByStep(transitionValue[0], chordResult);
			return tonalityResult;
			
		default:
			return pitch;
		}
	}
	
	/** Get pitch based on transition parameters and other stuff except a chord.
	 * @param transitionType
	 * @param transitionValue
	 * @param pitch
	 * @param currentTonality
	 * @return
	 */
	public static int getPitchByTransition(
			int 		transitionType, 
			int[] 		transitionValue, 
			int 		pitch, 
			Tonality 	currentTonality
			)
	{
		switch (transitionType) {
		case PConstants.SAME_PITCH:
			return pitch;
			
		case PConstants.CHORD_JUMP: //This situation doesn't appear because when calling this method - we never use chords.
			return pitch;
		
		case PConstants.CHROMATIC_JUMP:
			return pitch + transitionValue[0];
			
		case PConstants.TONALITY_JUMP: 
			return currentTonality.getTonalityNoteByStep(transitionValue[0], pitch);

		case PConstants.COMPLEX: //This situation doesn't appear either because when calling this method - we never use chords.
			return pitch;
			
		default:
			return pitch;
		}
	}
	
	
	/** Returns a random pattern from hashMap which has required jumpvalue.   
	 * @param jumpValue
	 * @param patternsHashMap
	 * @return New instance of pattern (no needs to wrap into new Pattern());
	 */
	public static Pattern getRandomPatternByJumpValue(
			int 									jumpValue, 
			HashMap<Integer, LinkedList<Pattern>> 	patternsHashMap
		)
	{
		if(!patternsHashMap.containsKey(jumpValue))
		{
			Log.severe("DataStorage > getRandomPatternByJumpValue > No entries for this jump value! Value = " + jumpValue);
			return new SNPattern(JMC.D4, JMC.QUARTER_NOTE); // I'm a kind person, I always can give asker a D ;)
		}
		
		LinkedList<Pattern> goodPatterns = patternsHashMap.get(jumpValue);
		
		int randomId = (int)(Math.random()*(goodPatterns.size() - 1));
		return new Pattern(goodPatterns.get(randomId));
	}
	
	public static double getAbsoluteLength(
			double relativeLength, 
			double absoluteLength)
	{
		return relativeLength * absoluteLength;
	}
	
	
	public static Pattern getRandomPatternFromList(LinkedList<Pattern> list)
	{
		return list.get((new Random()).nextInt(list.size()));
	}
	
	public static Melisma getRandomMelismaFromList(LinkedList<Melisma> list)
	{
		return list.get((new Random()).nextInt(list.size()));
	}
	
	
	public static String getTonalityTypeByName(String name)
	{
		if(name.equals("минор"))
			return "minor";
		
		if(name.equals("мажор"))
			return "major";
		
		Log.severe("DataStorage > Unknown tonality: " + name);
		
		return "major";
	}
	
	public static String[] getInstrumentsImagesList()
	{
		String[] array = new String[INSTRUMENTS.size()];
		for (int i = 0; i < INSTRUMENTS.size(); i++)
		{
			array[i] = INSTRUMENTS.get(i).name;
		}
		return array;
	}
	
	public static String[] getInstrumentsRussianNamesList()
	{
		String[] array = new String[INSTRUMENTS.size()];
		for (int i = 0; i < INSTRUMENTS.size(); i++)
		{
			array[i] = INSTRUMENTS.get(i).RussianName;
		}
		return array;
	}
	
	public static int getInstrumentIdByMidiId(int midiId)
	{
		for (int i = 0; i < INSTRUMENTS.size(); i++)
		{
			PInstrument p = INSTRUMENTS.get(i);
			if (p.midiId == midiId)
				return i;
		}
		return -1;
	}
}
