package util;

import it.sauronsoftware.jave.AudioAttributes;
import it.sauronsoftware.jave.Encoder;
import it.sauronsoftware.jave.EncoderException;
import it.sauronsoftware.jave.EncodingAttributes;
import it.sauronsoftware.jave.InputFormatException;

import java.io.File;

public class FileEncoder extends Thread {

	private File inputFile;
	private File outputFile;
	private EncodingAttributes attrs;
	private Encoder encoder;
	
	public FileEncoder(String inFileName, String outFileName)
	{
		encoder = new Encoder();
		AudioAttributes audio = new AudioAttributes();
		audio.setCodec("libmp3lame");
		audio.setBitRate(new Integer(320000));
		audio.setChannels(new Integer(2));
		audio.setSamplingRate(new Integer(44100));
		
		attrs = new EncodingAttributes();
		attrs.setFormat("mp3");
		attrs.setAudioAttributes(audio);
		
		inputFile = new File(inFileName);
		outputFile = new File(outFileName);
	}
	
	@Override
	public void run()
	{
		try {
			encoder.encode(inputFile, outputFile, attrs);
			System.out.println("Audio saved to file: " + outputFile.getName());
			inputFile.delete();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InputFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (EncoderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
