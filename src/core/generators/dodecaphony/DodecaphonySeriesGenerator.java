package core.generators.dodecaphony;

import jm.JMC;
import jm.constants.Instruments;
import jm.music.data.Note;
import jm.music.data.Part;
import jm.music.data.Phrase;
import jm.music.data.Score;
import core.data.Voice;
import core.generators.IMusicGenerator;

/** This class can be used to generate dodecaphonic music which is based on twelve-notes series. 
 * @author Armaxis
 *
 */
public class DodecaphonySeriesGenerator implements IMusicGenerator {

	private Dodecaphonizer dodecaphonizer ;
	private final int VOICES_COUNT = 1;
	
	@Override
	public void init() {
		dodecaphonizer = new Dodecaphonizer(new int[]{JMC.B4, JMC.BF4, JMC.G4, JMC.CS5, JMC.EF5, JMC.C5, JMC.D5, JMC.A4, JMC.FS4, JMC.E4, JMC.AF4, JMC.F4});
	}

	@Override
	public Score getScore() {
		Score dodecScore = new Score();
		final int totalNotes = 12;
		for (int k=0; k<VOICES_COUNT; ++k){
			int[] longRow = generateLongRow(dodecaphonizer, totalNotes);
			double[] longRowLengths =generateLongRowLengths(totalNotes);
			int pitch = (int)(Math.random()*2 + Math.floor(k*0.5)+60);

	        Part inst = new Part("Piano", Instruments.PIANO , 0);
	        Phrase dodekPhrase = new Phrase();
	        for (int i=0; i<totalNotes; i++) {
	        	dodekPhrase.addNote(new Note(longRow[i]+pitch, longRowLengths[i], 85+((i<35)?35-i:0)));	
	        }
	        inst.add(dodekPhrase);
	        dodecScore.addPart(inst);
		}
		return dodecScore;
	}

	private  double[] generateLongRowLengths (int totalNotes) {
		double[] longRowLengths = new double[totalNotes];
		for (int i=0; i<totalNotes; i++) {
			if(i % 3 == 0)
			{
				longRowLengths[i] = JMC.QUAVER;
			}
			else
			{
				longRowLengths[i] = JMC.SIXTEENTH_NOTE;
			}
		}
		return longRowLengths;
	}
	private  int[] generateLongRow(Dodecaphonizer df, int totalNotes) {
		int[] longRow = new int[totalNotes];
		int i=0;
		while (i<totalNotes-1) {
			int [] rndSerie = df.getRandomRowPitched((int)(Math.random()*11));
			//Dodekafonizer.printIntArray(rndSerie);
			for (int j=0; j<rndSerie.length; ++j) {
				if(i>=totalNotes)
					break;
				longRow[i] = rndSerie[j];
				i++;
			}
		}
		return longRow;
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
