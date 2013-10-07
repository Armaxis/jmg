package gui;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.LinkedList;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import core.MusicGeneratorManager;
import core.data.DataStorage;
import core.data.Voice;

public class MainPanel extends JPanel{
	
    public LinkedList<InstrumentPanel> instrumentPanels;
    private JPanel instrumentsContainer;
    public HeaderPanel headerPanel;
    private int nextPanelId = 0; //Позволяет указать на какое место пихать следующую панель с инструментом
    private int nextChannelId = 1; 
    
    public MusicGeneratorManager generator;

    
	public MainPanel(MusicGeneratorManager musicGeneratorManager) {
        
		this.generator = musicGeneratorManager;
		
		setLayout(new BorderLayout());
		
        JPanel headerContainer = new JPanel();
    
        headerPanel = new HeaderPanel(this);
        headerContainer.add(headerPanel);
        
        instrumentsContainer = new JPanel();
        instrumentPanels = new LinkedList<InstrumentPanel>();
        
        instrumentsContainer.setLayout(new GridBagLayout());
        instrumentsContainer.add(new JPanel(), new GBC(0, 999, 1, 1, 1, 1)); //Ugly workaround to make elements inside panel aligned to North. Fuck Swing >_<

        JScrollPane scrollInstrumentsPane = new JScrollPane(instrumentsContainer);
        scrollInstrumentsPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        add(headerContainer, BorderLayout.PAGE_START);
        add(scrollInstrumentsPane, BorderLayout.CENTER);
	}
	
	

	public void addInstrument()
	{
		Voice newVoice;
		if(instrumentPanels.isEmpty())
		{
			newVoice = new Voice(Voice.MELODY, Integer.toString(nextPanelId), DataStorage.getInstrumentByName("Piano"), nextChannelId, 100);
		}
		else
		{
			newVoice = new Voice(Voice.SECOND_VOICE, Integer.toString(nextPanelId),  DataStorage.getInstrumentByName("Violin"), nextChannelId, 100);
		}
		
		newVoice.setMuted(true);
		generator.addVoice(newVoice);
		
		InstrumentPanel ip = new InstrumentPanel(nextPanelId, this);
		ip.setVoice(newVoice);
		
		instrumentPanels.add(ip);
		instrumentsContainer.add(ip, new GBC(0, nextPanelId, 1, 1, 1, 0).fill(GridBagConstraints.HORIZONTAL).anchor(GridBagConstraints.NORTH));
		instrumentsContainer.revalidate();
		instrumentsContainer.repaint();
		
		ip.firstTimeInit();
		nextPanelId++;
		nextChannelId++;
		
		//We avoid using panel no 9 which will lead to channel 10 which is for drums. Later will be changed
		//TODO: Change this
		if (nextChannelId == 9)
			nextChannelId++;
	}

	public void removeInstrument(int trackId) {
		InstrumentPanel panel = null;
		for (InstrumentPanel ip : instrumentPanels)
		{
			if(ip.trackId == trackId)
			{
				panel = ip;
				break;
			}
		}		
		
		//If we remove main melody - then set another voice as main melody
		if(panel.getVoice().role == Voice.MELODY)
		{
			for (InstrumentPanel ip: instrumentPanels)
			{
				if(!ip.getVoice().equals(panel.getVoice()) && ip.getVoice().role != Voice.MELODY)
					ip.instrumentTypeCombo.setSelectedIndex(Voice.MELODY);
			}
		}
		
		generator.removeVoice(panel.getVoice());
		
		//Remember channel where we can apply next instrument
		nextChannelId = panel.getVoice().channel;
		
		instrumentsContainer.remove(panel);
		instrumentPanels.remove(panel);
		
		instrumentsContainer.revalidate();
		instrumentsContainer.repaint();
		
		if(instrumentPanels.isEmpty())
			headerPanel.muteAllCheckBox.setSelected(true);
	}



	public void removeAllInstruments() {
		while(!instrumentPanels.isEmpty())
		{
			removeInstrument(instrumentPanels.getFirst().trackId);
		}
	}
}
