package core;

import gui.GUI;
import gui.LoadingWindow;

import java.util.LinkedList;

import jm.music.data.Score;
import jm.music.data.Tempo;
import player.FileOutputPlayer;
import player.Player;
import util.Log;
import core.data.Settings;
import core.data.Voice;
import core.generators.IMusicGenerator;
import core.record.RecordsManager;

public class MusicGeneratorManager {

	private LinkedList<Player> players;
	private IMusicGenerator generator;
	private double timeSignature;
	private GUI gui;
	public RecordsManager recordsManager;
	
	/** Class constructor
	 * 
	 */
	public MusicGeneratorManager() {
		players = new LinkedList<Player>();
		timeSignature = 4 / 4 ;
		Settings.TEMPO = new Tempo(Tempo.DEFAULT_TEMPO); //60
		recordsManager = new RecordsManager();
		
		gui = new GUI();
		gui.init(this);
		
		//Only after initializing the gui we can init recordsManager. 
		recordsManager.init(gui);
	}
	
	public void addPlayer (Player player)
	{
		if (!players.contains(player))
		{
			players.add(player);
			player.start();
			Log.info("Music Generator > Players > Player added: " + player.getClass().getName());
			
			//Wait for player to be ready.
			while(!player.isReady)
			{
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					Log.severe("Music Generator > Players > Error while sleeping waiting for player " + player.getClass().getName());
				}
			}
		}
		else
		{
			Log.warning("Music Generator > Players > Player already exists: " + player.getClass().getName());
		}
	}
	
	public void addVoice(Voice v)
	{
		generator.addVoice(v);
	}
	
	public void removeVoice(Voice v)
	{
		generator.removeVoice(v);
	}
	
	public void setGenerator (IMusicGenerator _generator)
	{
		generator = _generator;
		generator.init();
	}
	
	public void start ()
	{
		gui.show();
		LoadingWindow.hideLoadingWindow();
		
		//RandomManager rm = new RandomManager(gui);
		//rm.start();
		Score score = generator.getScore();
		
		for (Player player : players)
			player.playScore(score);
		
		long startTime = 0;
		while (true)
		{
			double barLength = Settings.TEMPO.getPerSecond() * timeSignature * 4 * 1000; // Using current tempo and time signature calculate how many seconds does one bar last.
			if ((System.nanoTime() / 1000000) - startTime < barLength)
			{
				try {
					Thread.sleep(10);
					continue;
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			else 
			{
				recordsManager.playBar(RecordsManager.PLAYBACK_BAR_COUNTER);
				score = generator.getScore();
				
				score.setTempo(Settings.TEMPO.getPerMinute());
				
				for (Player player : players)
					player.playScore(score);
				startTime = System.nanoTime() / 1000000;
			}	
		}	
	}

	
	//********************************************************************
	//Methods for interacting with GUI
	//********************************************************************
	

	public void saveMidi(String filename) {
		for (Player p : players)
		{
			if (p instanceof FileOutputPlayer)
			{
				((FileOutputPlayer) p).saveMidiFile(filename);
			}
		}
	}
	

	public void saveMP3(String filename) {
		for (Player p : players)
		{
			if (p instanceof FileOutputPlayer)
			{
				((FileOutputPlayer) p).saveMp3File(filename);
			}
		}
	}
}
