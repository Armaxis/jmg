package gui;

import java.awt.Dimension;

import javax.swing.JFrame;

import core.MusicGeneratorManager;


public class GUI {

	public MainFrame frame;
	
	public void init(MusicGeneratorManager musicGeneratorManager) {
		frame = new MainFrame("Music Generator", musicGeneratorManager);
	}
	

	public void show() {
        //Display the window.
        frame.pack();
        frame.setSize(new Dimension(750, 400));
        frame.setLocation(100, 200);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}
