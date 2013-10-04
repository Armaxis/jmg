package main;

import gui.LoadingWindow;
import player.FileOutputPlayer;
import player.Player;
import player.RealtimePlayer;
import core.MusicGeneratorManager;
import core.data.DataStorage;
import core.data.structure.MotiveStructureStorage;
import core.generators.multigen.MultiVoicesGenerator;

public class Main {

	public static void main(String[] args) {

		LoadingWindow.showLoadingWindow();
		// Log.setLevel(Level.INFO);

		DataStorage.init();
		MotiveStructureStorage.init();
		launchGenerator();
	}

	public static void launchGenerator() {
		MusicGeneratorManager generatorManager = new MusicGeneratorManager();

		//GUI player shows score, are therefore slows down the generating process. So better turn it off.
		// Player guiPlayer = new SheetOutputPlayer(); 
		
		//Realtime player plays music in real time. What else did you expected it to do?
		Player realtimePlayer = new RealtimePlayer();
		
		//File player saves music to midi and mp3 file (available in GUI interface). Currently mp3 saving doesn't work, and midi is kind'a broken too.
		Player filePlayer = new FileOutputPlayer();

		// guiPlayer.init();
		realtimePlayer.init();
		filePlayer.init();

		// generatorManager.addPlayer(guiPlayer);
		generatorManager.addPlayer(realtimePlayer);
		generatorManager.addPlayer(filePlayer);

		generatorManager.setGenerator(new MultiVoicesGenerator());
		generatorManager.start();
	}
}
