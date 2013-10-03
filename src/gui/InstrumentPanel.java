package gui;

import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SwingConstants;
import javax.swing.border.EtchedBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import util.Tools;
import core.data.DataStorage;
import core.data.Voice;


public class InstrumentPanel extends JPanel {

	public JComboBox instrumentCombo;
	public JComboBox instrumentTypeCombo;
	public JSlider rangeSlider;
	public JSlider volumeSlider;
	public JSlider lengthSlider;
	private JSlider panSlider;
	public JCheckBox muteCheckBox;
	public JButton deleteButton;
	private JComboBox<String> octaveComboBox;
	
	private JLabel instrumentImage;
	private JPanel imagePanel;
	private JPanel melodyPanel;
	private JPanel optionsPanel;
	private JPanel buttonsPanel;
	
	private String[] instruments;
	private String[] instrumentTypes;
	private String[] images;
	
	public int trackId;
	private MainPanel mainPanel;
	
	private final String IMAGE_EXTENSION = ".bmp";
	private Voice voice;
	
	public InstrumentPanel(int id, MainPanel _mainPanel)
	{ 
		trackId = id;
		mainPanel = _mainPanel;
		
	    setLayout(new GridBagLayout());
	    
	    initGraphics();
	   
	    add(imagePanel, new GBC(0,0,1,1));
	    add(melodyPanel, new GBC(1,0,1,1).fill(GBC.HORIZONTAL));
	    add(optionsPanel, new GBC(2,0,1,1).fill(GBC.HORIZONTAL));
	    add(buttonsPanel, new GBC(3,0,1,1,0,0).fill(GBC.HORIZONTAL));
	    setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
	}
	
	private void initGraphics() {
		// TODO Auto-generated method stub
		initInstrumentImage();
		initMelodyPanel();
		initOptionsPanel();
		initButtonsPanel();
	}

	private void initInstrumentImage() {
		images = DataStorage.getInstrumentsImagesList();
		
	    BufferedImage pic;
		try {
			pic = ImageIO.read(new File("images/" + images[0] + IMAGE_EXTENSION));
			instrumentImage = new JLabel(new ImageIcon( pic ));
		} catch (IOException e1) {}
		
	    imagePanel = new JPanel();
	    imagePanel.setPreferredSize(new Dimension(96, 96));
	    imagePanel.setLayout(new GridBagLayout());
	    imagePanel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));

		imagePanel.add(instrumentImage, new GBC(0, 0, 1, 1).fill(GBC.BOTH));
	}

	private void initMelodyPanel() {

	    instruments = DataStorage.getInstrumentsRussianNamesList();
	    
	    instrumentCombo = new JComboBox(instruments);
	    instrumentCombo.setSelectedIndex(0);
	    instrumentCombo.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				//Refresh icon
				BufferedImage pic;
				try {
					//TODO: Refactor, getImageIndex is awful
					pic = ImageIO.read(new File("images/" + images[instrumentCombo.getSelectedIndex()] + IMAGE_EXTENSION));
					
					instrumentImage.setIcon(new ImageIcon( pic ));
				} catch (IOException e1) {}
					
				voice.setPinstrument(DataStorage.INSTRUMENTS.get(instrumentCombo.getSelectedIndex()));
				octaveComboBox.setSelectedIndex(voice.pinstrument.octaveIndex); //Это повторно переставит обновит voice.pinstrument.octaveIndex но заодно освежит комбобокс
			}
		});
	    
	    
	    instrumentTypes = new String[] { "Мелодия", "Второй голос", "Аккомпанемент" };
	    
	    instrumentTypeCombo = new JComboBox(instrumentTypes);
	    instrumentTypeCombo.setSelectedIndex(0);
	    instrumentTypeCombo.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				int newRole = instrumentTypeCombo.getSelectedIndex();
				
				//Check if we are trying to remove melody
				if(voice.role == Voice.MELODY && newRole != Voice.MELODY)
				{
					boolean thereIsNoAnotherMelody = true;
					for (InstrumentPanel ip: mainPanel.instrumentPanels)
					{
						if(!ip.voice.equals(voice) && ip.voice.role == Voice.MELODY)
						{
							thereIsNoAnotherMelody = false; 
							break;
						}
					}
					if (thereIsNoAnotherMelody)
					{
						//JOptionPane.showMessageDialog(mainPanel, "Невозможно сменить роль инструмента! В произведении должна быть основная мелодия. \nНазначьте другой инструмент как основную мелодию и попробуйте снова.", "Ошибка смены роли инструмента", JOptionPane.WARNING_MESSAGE);
						instrumentTypeCombo.setSelectedIndex(Voice.MELODY);
						return;
					}
				}
				
				//Check if we are selecting same role
				if(voice.role == newRole)
				{
					return;
				}
				
				voice.role = newRole;
				voice.updateRole();
				
				if(newRole == Voice.MELODY)
				{
					for (InstrumentPanel ip: mainPanel.instrumentPanels)
					{
						if(!ip.voice.equals(voice) && ip.voice.role == Voice.MELODY)
							ip.instrumentTypeCombo.setSelectedIndex(Voice.SECOND_VOICE);
					}
				}
				
			}
		});
	    
	    melodyPanel = new JPanel();
	    melodyPanel.setLayout(new GridBagLayout());
	    
	    melodyPanel.add(instrumentCombo, new GBC(0,0,1,1).fill(GBC.HORIZONTAL));
	    melodyPanel.add(instrumentTypeCombo, new GBC(0,1,1,1).fill(GBC.HORIZONTAL));
	}

	private void initOptionsPanel() {
		optionsPanel = new JPanel();
	    optionsPanel.setLayout(new GridBagLayout());
	    
	    JLabel volumeLabel = new JLabel("Громкость");
	    volumeLabel.setHorizontalAlignment(SwingConstants.CENTER);
	    volumeSlider = new JSlider(JSlider.HORIZONTAL, 0, 127, 100);
	    volumeSlider.addChangeListener(new ChangeListener() {
			
			@Override
			public void stateChanged(ChangeEvent arg0) {
				voice.volume = volumeSlider.getValue();
			}
		});
	    
	    volumeSlider.setMajorTickSpacing(10);
	    volumeSlider.setMinorTickSpacing(5);
	    
	    
	    JLabel octaveLabel = new JLabel("Октава ");
	    octaveLabel.setHorizontalAlignment(SwingConstants.LEFT);
        
        String[] octaveTypes = new String[] {"Контроктава", "Большая", "Малая", "Первая", "Вторая", "Третья", "Четвертая"};
	    
	    octaveComboBox = new JComboBox<String>(octaveTypes);
	    octaveComboBox.setSelectedIndex(DataStorage.INSTRUMENTS.get(instrumentCombo.getSelectedIndex()).octaveIndex);
	    octaveComboBox.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				voice.octaveSummand = Tools.octaveIndexToShift(octaveComboBox.getSelectedIndex());
			}
		});
	    
	    JLabel rangeLabel = new JLabel("Высота мелодии");
	    rangeLabel.setHorizontalAlignment(SwingConstants.CENTER);
	    rangeSlider = new JSlider(JSlider.HORIZONTAL, 30, 100, 60);
	    rangeSlider.addChangeListener(new ChangeListener() {
			
			@Override
			public void stateChanged(ChangeEvent arg0) {
				// TODO Auto-generated method stub
		//		Main.generator.setTrackProperty(trackId, "range", rangeSlider.getValue());
			}
		});
	    	
	    optionsPanel.add(volumeLabel, new GBC(0,0,2,1).fill(GBC.HORIZONTAL));
	    optionsPanel.add(volumeSlider, new GBC(0,1,2,1).fill(GBC.HORIZONTAL));
	    
	    optionsPanel.add(rangeLabel, new GBC(0,2,1,1).fill(GBC.HORIZONTAL));
	    optionsPanel.add(rangeSlider, new GBC(0,3,1,1).fill(GBC.HORIZONTAL));
	    
	    optionsPanel.add(octaveLabel, new GBC(2,0,1,1).fill(GBC.HORIZONTAL));
	    optionsPanel.add(octaveComboBox, new GBC(2,1,1,1).fill(GBC.HORIZONTAL));

	}
	
	private void initButtonsPanel() {

	    deleteButton = new JButton("Удалить");
	    deleteButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				mainPanel.removeInstrument(trackId);
			}
		});
	    
	    
	    muteCheckBox = new JCheckBox("Выкл");
	    muteCheckBox.setSelected(true);
	    muteCheckBox.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				setMuted(muteCheckBox.isSelected());
			}
		});
	    
	    buttonsPanel = new JPanel();
	    buttonsPanel.setLayout(new GridBagLayout());
	    
	    buttonsPanel.add(deleteButton, new GBC(0,0,1,1).fill(GBC.HORIZONTAL));
	    buttonsPanel.add(muteCheckBox, new GBC(0,1,1,1).fill(GBC.HORIZONTAL));
	}
	
	public void setMuted(boolean value)
	{
		voice.setMuted(value);
	}
	
	public void firstTimeInit()
	{
		instrumentCombo.setSelectedIndex(DataStorage.getInstrumentIdByMidiId(voice.pinstrument.midiId));
		instrumentTypeCombo.setSelectedIndex(voice.role);
		volumeSlider.setValue(voice.volume);
		octaveComboBox.setSelectedIndex( (voice.octaveSummand + 36 ) / 12); //Converts summand, e.g. 12, 24 to position in combobox, e.g. 4,5
		muteCheckBox.setSelected(voice.isMuted());
	}
	public Voice getVoice() {
		return voice;
	}

	public void setVoice(Voice newVoice) {
		this.voice = newVoice;
	}
}
