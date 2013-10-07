package gui;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import jm.music.data.Tempo;
import core.data.DataStorage;
import core.data.Settings;

public class HeaderPanel extends JPanel {

	private MainPanel parent;
	private JLabel tonalityLabel;
	public JComboBox tonalityKey;
	public LinkedList<JRadioButton> tonalityTypes;
	//private JLabel tempoLabel;
	private JSlider tempoSlider;
	
	
	public JCheckBox melismaCheckBox;
	public JPanel melismaPanel;
	
	public JButton addInstrumentButton;
	private JButton resetButton;
	private JButton recordButton;
	
	public String[] keys;
	private String[] tonalityTypesString;
	private JComboBox<Integer> melismaComboBox;
	private JLayeredPane tonalityPanel;
	private JLayeredPane generationPanel;
	private JSlider volumeSlider;
	private JPanel buttonsPanel;	
	public JCheckBox muteAllCheckBox;
	
	public HeaderPanel(MainPanel _parent)
	{
		this.parent = _parent;
		
	    setLayout(new GridBagLayout());
	    
		//=============================================
	    //Initialize elements
		//=============================================
	    
	    initTonalityPanel();
	    initGenerationPanel();
	    initButtonsPanel();
	    
        
        //=============================================
        //Add elements to panel
		//=============================================

	    add(tonalityPanel, new GBC(0,0,1,1).fill(GBC.HORIZONTAL));
	    add(generationPanel, new GBC(1,0,1,1).fill(GBC.HORIZONTAL));
	    //add(tempoPanel, new GBC(0,1,1,1).fill(GBC.HORIZONTAL));
	    add(buttonsPanel, new GBC(2,0,1,1).fill(GBC.HORIZONTAL));
	}

	private void initTonalityPanel() {
		
		tonalityPanel = new JLayeredPane();
	    tonalityPanel.setPreferredSize(new Dimension(150, 90));
	    tonalityPanel.setBorder(BorderFactory.createTitledBorder(
                "Тональность"));
	    tonalityPanel.setLayout(new GridBagLayout());
	    
	    
	    tonalityLabel = new JLabel("Тон  ");
	    tonalityLabel.setHorizontalAlignment(SwingConstants.RIGHT);
	    
	    keys = new String[] { "C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B" };
	    
	    tonalityKey = new JComboBox(keys);
	    tonalityKey.setSelectedIndex(0);
	    tonalityKey.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Settings.setTonalityPitch(tonalityKey.getSelectedIndex());
			}
		});
	    
	    
	    //Initialize key types elements
        JPanel tonalityTypePanel = new JPanel();
        tonalityTypePanel.setLayout(new GridBagLayout());
        
        tonalityTypesString = new String[] {"мажор", "минор"};
        tonalityTypes = new LinkedList<JRadioButton>();
        ButtonGroup typeGroup = new ButtonGroup();
        for (int i=0; i< tonalityTypesString.length; i++)
        {
        	JRadioButton jrb = new JRadioButton(tonalityTypesString[i]);
        	tonalityTypes.add(jrb);
        	typeGroup.add(jrb);
        	jrb.setActionCommand(tonalityTypesString[i]);
        	jrb.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					Settings.setTonalityType(DataStorage.getTonalityTypeByName(e.getActionCommand()));
				}
			});
        }
        tonalityTypes.getFirst().setSelected(true);
        
        tonalityPanel.add(tonalityLabel, new GBC(0,0,1,1).fill(GBC.HORIZONTAL));
        tonalityPanel.add(tonalityKey, new GBC(1,0,1,1).fill(GBC.HORIZONTAL));
	    
	    for(JRadioButton jrb : tonalityTypes) {
	    	tonalityTypePanel.add(jrb, new GBC(tonalityTypes.indexOf(jrb), 0, 1, 1).fill(GBC.HORIZONTAL));
	    }
	    
	    tonalityPanel.add(tonalityTypePanel, new GBC(0,1,2,1).fill(GBC.HORIZONTAL));
	}

	private void initGenerationPanel() {
		
		generationPanel = new JLayeredPane();
        generationPanel.setPreferredSize(new Dimension(450, 90));
        generationPanel.setBorder(BorderFactory.createTitledBorder(
                "Генерация"));
        generationPanel.setLayout(new GridBagLayout());
 
	    tempoSlider = new JSlider(JSlider.HORIZONTAL, 50, 150, (int)Tempo.DEFAULT_TEMPO);
	    tempoSlider.setBorder(BorderFactory.createTitledBorder("Темп"));
	    tempoSlider.setMajorTickSpacing(50);
	    tempoSlider.setMinorTickSpacing(10);
	    tempoSlider.setPaintTicks(true);
        tempoSlider.setPaintLabels(true);
        
        tempoSlider.addChangeListener(new ChangeListener() {
			
			@Override
			public void stateChanged(ChangeEvent arg0) {
				Settings.TEMPO.setTempo(tempoSlider.getValue());
			}
		});

        
        melismaCheckBox = new JCheckBox("Мелизмы");
        melismaCheckBox.setSelected(false);
        melismaCheckBox.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Settings.USE_MELISMAS = melismaCheckBox.isSelected();
				melismaComboBox.setEnabled(Settings.USE_MELISMAS);
			}
		});
        
        JLabel melismaFreqLabel = new JLabel("Частота мелизмов ");
        melismaFreqLabel.setHorizontalAlignment(SwingConstants.LEFT);
        
        Integer[] melismaFreqs = new Integer[] {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
	    
	    melismaComboBox = new JComboBox<Integer>(melismaFreqs);
	    melismaComboBox.setSelectedIndex(4);
	    melismaComboBox.setEnabled(false);
	    melismaComboBox.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				Settings.MELISMAS_CHANCE = melismaComboBox.getSelectedIndex() + 1;
			}
		});
	    
	    melismaPanel = new JPanel();
        melismaPanel.setLayout(new GridBagLayout());
        melismaPanel.add(melismaFreqLabel, new GBC(0,0,1,1,1,1,GridBagConstraints.WEST, GridBagConstraints.NONE));
        melismaPanel.add(melismaComboBox, new GBC(1,0,1,1,1,1,GridBagConstraints.WEST, GridBagConstraints.NONE));
        
	    JLabel volumeLabel = new JLabel("Громкость");
	    volumeLabel.setHorizontalAlignment(SwingConstants.CENTER);
	    volumeSlider = new JSlider(JSlider.HORIZONTAL, 0, 100, 100);
	    volumeSlider.setBorder(BorderFactory.createTitledBorder("Общая громкость %"));
	    volumeSlider.addChangeListener(new ChangeListener() {
			
			@Override
			public void stateChanged(ChangeEvent arg0) {
				Settings.OVERALL_VOLUME = volumeSlider.getValue();
			}
		});
	    
	    volumeSlider.setMajorTickSpacing(50);
	    volumeSlider.setMinorTickSpacing(10);
	    volumeSlider.setPaintTicks(true);
	    volumeSlider.setPaintLabels(true);

	    generationPanel.add(melismaCheckBox, new GBC(0,0,1,1,0.1,0.1,GridBagConstraints.WEST, GridBagConstraints.NONE));
	    generationPanel.add(melismaPanel, new GBC(0,1,1,1,0.1,0.1,GridBagConstraints.WEST, GridBagConstraints.NONE));
	    generationPanel.add(tempoSlider, new GBC(1,0,1,2,1,1,GridBagConstraints.WEST, GridBagConstraints.NONE).fill(GBC.HORIZONTAL));
	    generationPanel.add(volumeSlider, new GBC(2,0,1,2,1,1,GridBagConstraints.WEST, GridBagConstraints.NONE).fill(GBC.HORIZONTAL));
	}

	private void initButtonsPanel() {
        buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new GridBagLayout());
        
        addInstrumentButton = new JButton("Добавить");
        addInstrumentButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				parent.addInstrument();
			}
		});
        
//        recordButton = new JButton("Запись");
//        recordButton.addActionListener(new ActionListener() {
//			
//			@Override
//			public void actionPerformed(ActionEvent arg0) {
//				//parent.removeAllInstruments();
//			}
//		});
        
        resetButton = new JButton("Очистить");
        resetButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				parent.removeAllInstruments();
			}
		});
        
        muteAllCheckBox = new JCheckBox("Выкл все");
        muteAllCheckBox.setSelected(true);
        muteAllCheckBox.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				for (InstrumentPanel ip : parent.instrumentPanels)
				{
					ip.muteCheckBox.setSelected(muteAllCheckBox.isSelected());
					ip.setMuted(muteAllCheckBox.isSelected());
				}
			}
		});

	    buttonsPanel.add(addInstrumentButton, new GBC(0,0,1,1).fill(GBC.HORIZONTAL));
	    buttonsPanel.add(resetButton, new GBC(0,1,1,1).fill(GBC.HORIZONTAL));	
	    buttonsPanel.add(muteAllCheckBox, new GBC(0,2,1,1).fill(GBC.HORIZONTAL));
	}
}
