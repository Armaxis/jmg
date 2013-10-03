package main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.UIManager;




import jm.JMC;
import player.FileOutputPlayer;
import player.Player;
import player.RealtimePlayer;
import player.SheetOutputPlayer;
import core.MusicGeneratorManager;
import core.data.DataStorage;
import core.data.patterns.Pattern;
import core.data.structure.MotiveStructureStorage;
import core.data.tonality.MajorTonality;
import core.generators.IMusicGenerator;
import core.generators.MotivesGenerator;
import core.generators.multigen.MultiVoicesGenerator;

public class Main {

	/**
	 * @param args
	 */
	
	public static JDialog LOADING_PANE;
	
	public static void main(String[] args) {
		showLoadingWindow();
		
		//Log.setLevel(Level.INFO);
		//Log.setLevel(Level.WARNING);
		
		DataStorage.init();
		MotiveStructureStorage.init();
		launchGenerator();
	}
	
	public static void showLoadingWindow()
	{
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
		} 
		JOptionPane pane = new JOptionPane("Загрузка. Пожалуйста, подождите...", JOptionPane.PLAIN_MESSAGE, JOptionPane.DEFAULT_OPTION, null, new Object[]{}, null);
		
		LOADING_PANE = new JDialog();
		LOADING_PANE.setModal(false);

		LOADING_PANE.setContentPane(pane);

		LOADING_PANE.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

		LOADING_PANE.setLocation(100, 200);
		LOADING_PANE.pack();
		LOADING_PANE.setVisible(true);
	}
	
	public static void hideLoadingWindow()
	{
		LOADING_PANE.dispose();
		LOADING_PANE.setVisible(false);
	}
	
	public static void testPatterns()
	{
		File f = new File("database/SimplePatterns.txt");
		List<Pattern> patterns = new LinkedList<Pattern>();
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
					patterns.add(p);
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void testMotives()
	{
		MotivesGenerator mg = new MotivesGenerator();
		mg.generateMotive(JMC.C4, new MajorTonality(JMC.C4));
	}
	
	public static void launchGenerator()
	{
		MusicGeneratorManager generatorManager = new MusicGeneratorManager();
		
		//Player guiPlayer = new SheetOutputPlayer();
		Player midiPlayer = new RealtimePlayer();
		Player filePlayer = new FileOutputPlayer();
		
		//guiPlayer.init();
		midiPlayer.init();
		filePlayer.init();
		
		//generatorManager.addPlayer(guiPlayer);
		generatorManager.addPlayer(midiPlayer);
		generatorManager.addPlayer(filePlayer);
		
		
		IMusicGenerator single = new MultiVoicesGenerator();
		generatorManager.setGenerator(single);
		
		generatorManager.start();
	}
}
