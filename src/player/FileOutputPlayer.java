package player;

import java.io.File;

import jm.music.data.Part;
import jm.music.data.Phrase;
import jm.music.data.Score;
import jm.music.tools.Mod;
import jm.util.Write;
import util.FileEncoder;

public class FileOutputPlayer extends Player {
	
	private Score fullScore;
	private Process recordProcess;
	private final String TEMP_FILE_NAME = "temp.wav";
	private final String TEMP_ENCODE_FILE_NAME = "temp_encode.wav";

	@Override
	public void playScore(Score score) {
		
		if(score.size() == 0)
			return;
		
		Score dupl = new Score();
		for (int k = 0; k < score.size(); k++)
		{
			Part iteratorPart = score.getPart(k); 
			Part newP = new Part(iteratorPart.getTitle(), iteratorPart.getInstrument(), iteratorPart.getChannel());
			for (int i = 0; i < iteratorPart.size(); i++)
			{
				newP.addPhrase(new Phrase(iteratorPart.getPhrase(i).getNoteArray()));
			}
			dupl.add(newP);
		}
		
		Mod.merge(fullScore, dupl);
                    
		Part[] scoreParts = fullScore.getPartArray();
		
    	for(int i = 0; i < scoreParts.length; i ++)
    	{
    		if(scoreParts[i].size() > 1)
    		{
        		for (int k = 1; k < scoreParts[i].size(); k++)
        		{
        			scoreParts[i].getPhrase(0).addNoteList(scoreParts[i].getPhrase(k).getNoteArray());
        			scoreParts[i].getPhrase(k).getNoteList().clear();
        		}
    		}
    	}
        
	}

	@Override
	public void init() {
		isReady = true;
		fullScore = new Score();
		startRecorder();
	}

	@Override
	public void start() {
		
	}

	private void startRecorder()
	{
		
	}
	
	
	public void saveMidiFile(String filename)
	{
		Write.midi(fullScore, filename);
	}
	
	public void saveMp3File(String filename)
	{
		recordProcess.destroy();
		
		(new File(TEMP_FILE_NAME)).renameTo(new File(TEMP_ENCODE_FILE_NAME));
		
		if(!filename.endsWith(".mp3"))
			filename = filename + ".mp3";
		
		FileEncoder fileEnc = new FileEncoder(TEMP_ENCODE_FILE_NAME, filename);
		fileEnc.start();
		
		startRecorder();
	}
}
