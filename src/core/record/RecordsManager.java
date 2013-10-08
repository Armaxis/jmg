package core.record;

import java.util.HashMap;
import java.util.LinkedList;

import core.data.DataStorage;
import gui.GUI;
import gui.HeaderPanel;
import gui.InstrumentPanel;
import gui.MainPanel;

public class RecordsManager {

	public static int RECORDER_BAR_COUNTER = 0;
	private MainPanel mainPanel;
	private HeaderPanel headerPanel;
	private boolean isRecording;
	private LinkedList<RecordEvent> recordedEvents;
	private HashMap<InstrumentPanel, Integer> instrumentPanelsIds;
	private int nextPanelIdCounter;
	
	public RecordsManager() {
		isRecording = false;
		RECORDER_BAR_COUNTER = 0;
		nextPanelIdCounter = 0;
		recordedEvents = new LinkedList<RecordEvent>();
		instrumentPanelsIds = new HashMap<InstrumentPanel, Integer>();
	}

	public void startRecord() {
		isRecording = true;
		
		//Record initial state of generator
		//Tonality (key and type)
		record(RecordEvent.CHANGE_TONALITY_KEY, headerPanel.tonalityKey.getSelectedIndex());
		record(RecordEvent.CHANGE_TONALITY_TYPE, headerPanel.tonalityTypes.getFirst().isSelected());
		
		//Melismas
		record(RecordEvent.SET_MELISMAS, headerPanel.melismaCheckBox.isSelected());
		record(RecordEvent.SET_MELISMAS_CHANCE, headerPanel.melismaComboBox.getSelectedIndex());
		
		//Tempo
		record(RecordEvent.SET_TEMPO, headerPanel.tempoSlider.getValue());
		
		//Volume
		record(RecordEvent.SET_OVERALL_VOLUME, headerPanel.volumeSlider.getValue());
		
		//Add all instruments
		for (InstrumentPanel ip : mainPanel.instrumentPanels)
		{
			record(RecordEvent.ADD_INSTRUMENT, nextPanelIdCounter);
			instrumentPanelsIds.put(ip, nextPanelIdCounter);
			nextPanelIdCounter ++;
		}
		
		//Init all instruments
		for (InstrumentPanel ip : instrumentPanelsIds.keySet())
		{
			int instrId = instrumentPanelsIds.get(ip); 
			record(RecordEvent.SET_INSTRUMENT_TYPE, DataStorage.INSTRUMENTS.get(ip.instrumentCombo.getSelectedIndex()).midiId, instrId);
			record(RecordEvent.SET_INSTRUMENT_ROLE, ip.instrumentTypeCombo.getSelectedIndex(), instrId);
			record(RecordEvent.SET_INSTRUMENT_VOLUME, ip.volumeSlider.getValue(), instrId);
			record(RecordEvent.SET_INSTRUMENT_OCTAVE, ip.octaveComboBox.getSelectedIndex(), instrId);
			record(RecordEvent.SET_INSTRUMENT_MUTE, ip.muteCheckBox.isSelected(), instrId);
		}
		System.out.println(recordedEvents.toString());
	}

	public void init(GUI gui) {
		this.mainPanel = gui.frame.getMainPanel();
		this.headerPanel = mainPanel.headerPanel;
	}
	
	private void record(String eventType, int value)
	{
		record(eventType, Integer.toString(value), -1);
	}
	
	private void record(String eventType, int value, int instrId)
	{
		record(eventType, Integer.toString(value), instrId);
	}
	
	private void record(String eventType, boolean value)
	{
		int val = value? 1 : 0; 
		record(eventType, Integer.toString(val), -1);
	}
	
	private void record(String eventType, boolean value, int instrId)
	{
		int val = value? 1 : 0; 
		record(eventType, Integer.toString(val), instrId);
	}
	
	private void record(String eventType, String value)
	{
		record(eventType, value, -1);
	}
	
	private void record(String eventType, String value, int instrumentId)
	{
		recordedEvents.add(new RecordEvent(eventType, value, RECORDER_BAR_COUNTER, instrumentId));
	}

}
