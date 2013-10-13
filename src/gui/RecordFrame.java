package gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import core.record.RecordsManager;

public class RecordFrame extends JFrame {

	private MainPanel mainPanel;
	private JPanel panel;
	private RecordsManager recordsManager;
	private JButton recordButton;
	private JButton playButton;
	private JButton deleteButton;
	
	public static JFrame frame;
	private DefaultListModel<String> listModel;
	private JList<String> recordsList;

	public RecordFrame(MainFrame mainFrame){
		super("Запись сценариев");
		this.mainPanel = mainFrame.getMainPanel();
		
		this.recordsManager = mainFrame.getGenerator().recordsManager;
		RecordFrame.frame = this;
		initInterface();
		setSize(300, 400);
	}

	private void initInterface() {
		panel = new JPanel();
		
		panel.setLayout(new GridBagLayout());
		
		recordButton = new JButton("Запись");
		recordButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if (!RecordsManager.isRecording)
				{
					recordsManager.startRecord();
					recordButton.setText("Остановить");
				}
				else
				{
					recordsManager.stopRecord();
					recordButton.setText("Запись");
				}
			}
		});
		
		listModel = new DefaultListModel<String>();
		recordsList = new JList<String>(listModel);
		refreshRecordsList();
		
		playButton = new JButton("Загрузить");
		playButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if (!RecordsManager.isPlaying)
				{
					recordsManager.playRecord(recordsList.getSelectedValue());
					playButton.setText("Остановить");
				} 
				else
				{
					recordsManager.stopPlayingRecord();
					playButton.setText("Загрузить");
				}
			}
		});
		
		deleteButton = new JButton("Удалить");
		deleteButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				int dialogResult = JOptionPane.showConfirmDialog (null, "Вы уверены, что хотите удалить запись?", "Подтверждение", JOptionPane.YES_NO_OPTION);
				if(dialogResult == JOptionPane.YES_OPTION){
					recordsManager.deleteRecord(recordsList.getSelectedValue());
					refreshRecordsList();
				}
			}
		});
		
		panel.add(recordButton, new GBC(0,0,2,1,0.1,0.1));
		panel.add(recordsList, new GBC(0,1,1,2,1,1).fill(GridBagConstraints.BOTH));
		panel.add(playButton, new GBC(1,1,1,1));
		panel.add(deleteButton, new GBC(1,2,1,1));
		setContentPane(panel);
	}
	
	public void refreshRecordsList()
	{
		listModel = new DefaultListModel<String>();
		for (String el : recordsManager.recordsList)
			listModel.addElement(el);
		recordsList.setModel(listModel);
		recordsList.setSelectedIndex(0);
		recordsList.ensureIndexIsVisible(0);
		
	}
	
	
}
