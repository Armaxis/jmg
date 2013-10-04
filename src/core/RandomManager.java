package core;

import gui.GUI;
import gui.HeaderPanel;
import gui.MainPanel;

import java.util.Random;

import core.data.DataStorage;

public class RandomManager extends Thread {

	private GUI gui;
	private MainPanel mainPanel;
	private HeaderPanel headerPanel;
	
	public RandomManager(GUI _gui)
	{
		this.gui = _gui;
		this.mainPanel = _gui.frame.getMainPanel();
		this.headerPanel = mainPanel.headerPanel;
	}
	
	@Override
	public void run()
	{
		while(true)
		{
			//Поменять звук инструмента
			if (Math.random() < 0.4 && mainPanel.instrumentPanels.size() > 0)
			{
				int randomPanelId = (new Random()).nextInt(mainPanel.instrumentPanels.size());
				int randomInstrumentId = (new Random()).nextInt(DataStorage.getInstrumentsRussianNamesList().length);
				mainPanel.instrumentPanels.get(randomPanelId).instrumentCombo.setSelectedIndex(randomInstrumentId);
			}
			
			//Переназначить основную мелодию
			if (Math.random() < 0.2 && mainPanel.instrumentPanels.size() > 0)
			{
				int randomPanelId = (new Random()).nextInt(mainPanel.instrumentPanels.size());
				mainPanel.instrumentPanels.get(randomPanelId).instrumentTypeCombo.setSelectedIndex(0);
			}
			
			//Изменение громкости
			if (Math.random() < 0.3 && mainPanel.instrumentPanels.size() > 0)
			{
				int randomPanelId = (new Random()).nextInt(mainPanel.instrumentPanels.size());
				int randomVolume = 80 + (new Random()).nextInt(30);
				mainPanel.instrumentPanels.get(randomPanelId).volumeSlider.setValue(randomVolume);
			}
			
			//Смена тональности
			if (Math.random() < 0.1)
			{
				int randomTonalityId = (new Random()).nextInt(headerPanel.keys.length);
				headerPanel.tonalityKey.setSelectedIndex(randomTonalityId);
				
				//Минор или мажор
				if (Math.random() < 0.5)
					headerPanel.tonalityTypes.getFirst().setSelected(true);
				else
					headerPanel.tonalityTypes.get(1).setSelected(true);
			}
			
			//Добавление инструмента
			if (Math.random() < 0.1)
			{
				mainPanel.addInstrument();
			}
			
			//Удаление инструмента
			if (Math.random() < 0.08 && mainPanel.instrumentPanels.size() > 0)
			{
				int randomPanelId = (new Random()).nextInt(mainPanel.instrumentPanels.size());
				mainPanel.removeInstrument(mainPanel.instrumentPanels.get(randomPanelId).trackId);
			}
			
			try {
				sleep(6000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
