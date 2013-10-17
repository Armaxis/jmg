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
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.text.DefaultCaret;

import util.Log;
import core.record.RecordEvent;
import core.record.RecordsManager;

public class RecordFrame extends JFrame {

	private MainPanel mainPanel;
	private JPanel panel;
	private RecordsManager recordsManager;
	private JButton recordButton;
	private JButton playButton;
	private JButton deleteButton;
	private JButton viewButton;
	
	public static JFrame frame;
	private DefaultListModel<String> listModel;
	private JList<String> recordsList;
	private JTextArea logArea;

	public RecordFrame(MainFrame mainFrame){
		super("Запись сценариев");
		this.mainPanel = mainFrame.getMainPanel();
		
		this.recordsManager = mainFrame.getGenerator().recordsManager;
		RecordFrame.frame = this;
		initInterface();
		setSize(500, 400);
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
					log("Запись начата!");
					recordsManager.startRecord();
					recordButton.setText("Остановить");
				}
				else
				{
					log("Запись завершена!");
					recordsManager.stopRecord();
					recordButton.setText("Запись");
					refreshRecordsList();
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
					log("Воспроизведение " + recordsList.getSelectedValue() + " начато!");
					recordsManager.playRecord(recordsList.getSelectedValue());
					playButton.setText("Остановить");
				} 
				else
				{
					log("Воспроизведение завершено!");
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
					log("Запись " + recordsList.getSelectedValue() + " удалена!");
					refreshRecordsList();
				}
			}
		});
		
		viewButton = new JButton("Просмотреть");
		viewButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				recordsManager.readRecordFile(recordsList.getSelectedValue());
				log("=================================");
				log("Запись " + recordsList.getSelectedValue() + RecordsManager.RECORD_EXTENSION);
				for (RecordEvent ev : recordsManager.playbackEvents)
					log(ev.toString());
				log("=================================");
			}
		});
		
		logArea = new JTextArea();
        logArea.setRows(10);
        logArea.setFont(logArea.getFont().deriveFont(12f));
        
        JScrollPane logScroll = new JScrollPane(logArea);
        DefaultCaret caret = (DefaultCaret)logArea.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		
		panel.add(recordButton, new GBC(0,0,2,1,0.1,0.1));
		panel.add(recordsList, new GBC(0,1,1,2,1,1).fill(GridBagConstraints.BOTH));
		panel.add(playButton, new GBC(1,1,1,1));
		panel.add(deleteButton, new GBC(1,2,1,1));
		panel.add(viewButton, new GBC(1,3,1,1));
		panel.add(logScroll, new GBC(0,4,2,1).fill(GridBagConstraints.BOTH));
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
	
	public void log(String text)
	{
		logArea.append(text + "\n");
		Log.info("RecordFrame > Log: " + text);
	}
	
}
