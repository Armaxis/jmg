package gui;

import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import core.record.RecordsManager;

public class RecordFrame extends JFrame {

	private MainPanel mainPanel;
	private JPanel panel;
	private RecordsManager recordsManager;

	public RecordFrame(MainFrame mainFrame){
		super("Запись сценариев");
		this.mainPanel = mainFrame.getMainPanel();
		
		this.recordsManager = mainFrame.getGenerator().recordsManager;
		initInterface();
		setSize(300, 400);
	}

	private void initInterface() {
		panel = new JPanel();
		
		panel.setLayout(new GridBagLayout());
		
		JButton recordButton = new JButton("Запись");
		recordButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				recordsManager.startRecord();
			}
		});
		
		panel.add(recordButton, new GBC(0,0,1,1));
		setContentPane(panel);
	}
	
}
