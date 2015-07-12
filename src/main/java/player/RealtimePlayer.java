package player;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.util.LinkedList;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;
import javax.sound.midi.Soundbank;
import javax.sound.midi.Synthesizer;
import javax.sound.midi.Transmitter;

import jm.midi.SMF;
import jm.music.data.Score;
import util.Log;

import com.sun.media.sound.SF2Soundbank;

import core.data.DataStorage;
import core.data.PInstrument;

public class RealtimePlayer extends Player {

	private final String SOUNDFONT_FILENAME = "fluid.sf2";
	private final String SOUNDFONT_FOLDER 	= "soundfonts/";

	private Sequencer sequencer;
	private Synthesizer synthesizer;
	private LinkedList<Sequence> playbackSequencesQueue;
	
	@Override
	public void playScore(Score score) {	

		if (score == null)
		{
			Log.warning("RealtimeMidiPlayer > PlayScore > Score is null.");
			return;
		}
		
		if (score.getPartList().size() == 0) {
			Log.info("RealtimeMidiPlayer > PlayScore > Score is empty.");
			return;
		}
		
		playbackSequencesQueue.add(scoreToSequence(score));
		Log.info("RealtimeMidiPlayer > PlayScore > Score added to queue.");

	}

	@Override
	public void init() {
				
		playbackSequencesQueue = new LinkedList<Sequence>();
		try {
			synthesizer = MidiSystem.getSynthesizer();
			synthesizer.open();
			
			File soundBankFile = new File(SOUNDFONT_FOLDER + SOUNDFONT_FILENAME);
			if(soundBankFile.exists())
			{
				Soundbank soundbank = new SF2Soundbank(new FileInputStream(soundBankFile));
				
				Log.info("RealtimeMidiPlayer > init > Loading soundfonts from " + SOUNDFONT_FILENAME);
				//Load only instruments that are available for playback
				for (PInstrument instr : DataStorage.INSTRUMENTS)
				{
					synthesizer.loadInstrument(soundbank.getInstruments()[instr.midiId]);
				}
				
				Log.info("RealtimeMidiPlayer > init > Loading of soundfonts complete!");
			}
			sequencer = MidiSystem.getSequencer();
			Transmitter seqTrans = sequencer.getTransmitter();
			sequencer.getTransmitters().get(0).close();

			Receiver synthRec = synthesizer.getReceiver();
			seqTrans.setReceiver(synthRec);
			sequencer.open();
			
			isReady = true; //We are ready to play now, folks
			
		} catch (FileNotFoundException e) {
			Log.severe("RealtimeMidiPlayer > init > SOUNDFONTS FILE NOT FOUND! " + SOUNDFONT_FILENAME);
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (MidiUnavailableException e) {
			e.printStackTrace();
		}
		
	}
	
	@Override
	public void run ()
	{
		while(true)
		{
			//If there is nothing to play - sleep for 10 millisecs and try again.
			if(playbackSequencesQueue.isEmpty() || !isReady)
			{
				try {Thread.sleep(10);} catch (InterruptedException e) {};
				continue;
			}
			
			Sequence sq = playbackSequencesQueue.removeFirst();
			try {
				sequencer.setSequence(sq);
				sequencer.start();
				
				while (true) {
					if (sequencer.isRunning()) {
						try {
							Thread.sleep(100); // Check every 100 milliseconds
						} catch (InterruptedException ignore) {
							break;
						}
					} else {
						break;
					}
				}
			} catch (InvalidMidiDataException imde) {
				Log.severe("RealtimeMidiPlayer > PlayScore > Invalid Midi data!");
			}
		}
	}
	
	private Sequence scoreToSequence(Score s)
	{
		Sequence sq = null;
		Log.info("RealtimeMidiPlayer > PlayScore > Converting score.");
		
		SMF smf = new SMF();
		smf.clearTracks();
		
		jm.midi.MidiParser.scoreToSMF(s, smf);
		OutputStream os = new ByteArrayOutputStream();
		try {
			smf.write(os);
			sq = MidiSystem.getSequence(new ByteArrayInputStream(((ByteArrayOutputStream) os).toByteArray()));
		} catch (InvalidMidiDataException imde) {
			Log.severe("RealtimeMidiPlayer > PlayScore > Invalid Midi data!");
		} catch (IOException ioe) {
			Log.severe("RealtimeMidiPlayer > PlayScore > Score conversion error!");
			return null;
		}
			
		Log.info("RealtimeMidiPlayer > PlayScore > Score converted to MIDI.");
		
		return sq;
	}

}
