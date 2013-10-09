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
	public static boolean isRecording;
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
		recordTonalityKey();
		recordTonalityType();
		
		//Melismas
		recordSetMelismas();
		recordSetMelismasChance();
		
		//Tempo
		recordTempo();
		
		//Volume
		recordVolume();
		
		//Mute all position
		//recordInstrMuteAll();
		
		//Add all instruments
		for (InstrumentPanel ip : mainPanel.instrumentPanels)
			recordAddInstrument(ip);
		
		//Init all instruments
		for (InstrumentPanel ip : instrumentPanelsIds.keySet())
		{
			recordInstrType(ip);
			recordInstrRole(ip);
			recordInstrVolume(ip);
			recordInstrOctave(ip);
			recordInstrMute(ip);
		}
	}

	public void init(GUI gui) {
		this.mainPanel = gui.frame.getMainPanel();
		this.headerPanel = mainPanel.headerPanel;
	}
	
	public void recordTonalityKey() {
		record(RecordEvent.CHANGE_TONALITY_KEY, headerPanel.tonalityKey.getSelectedIndex());
	}

	public void recordTonalityType() {
		record(RecordEvent.CHANGE_TONALITY_TYPE, headerPanel.tonalityTypes.getFirst().isSelected());
	}
	
	public void recordSetMelismas() {
		record(RecordEvent.SET_MELISMAS, headerPanel.melismaCheckBox.isSelected());
	}
	
	public void recordSetMelismasChance() {
		record(RecordEvent.SET_MELISMAS_CHANCE, headerPanel.melismaComboBox.getSelectedIndex());
	}
	
	public void recordTempo() {
		record(RecordEvent.SET_TEMPO, headerPanel.tempoSlider.getValue());
	}
	
	public void recordVolume() {
		record(RecordEvent.SET_OVERALL_VOLUME, headerPanel.volumeSlider.getValue());
	}
	
	public void recordAddInstrument(InstrumentPanel ip) {
		if(!isRecording)
			return;
		
		record(RecordEvent.ADD_INSTRUMENT, nextPanelIdCounter);
		instrumentPanelsIds.put(ip, nextPanelIdCounter);
		nextPanelIdCounter ++;
	}
	
	public void recordInstrType(InstrumentPanel ip) {
		if(!isRecording)
			return;
		
		int instrId = instrumentPanelsIds.get(ip); 
		record(RecordEvent.SET_INSTRUMENT_TYPE, DataStorage.INSTRUMENTS.get(ip.instrumentCombo.getSelectedIndex()).midiId, instrId);
	}
	
	public void recordInstrRole(InstrumentPanel ip) {
		if(!isRecording)
			return;
		
		int instrId = instrumentPanelsIds.get(ip); 
		record(RecordEvent.SET_INSTRUMENT_ROLE, ip.instrumentTypeCombo.getSelectedIndex(), instrId);
	}
	
	public void recordInstrVolume(InstrumentPanel ip) {
		if(!isRecording)
			return;
		
		int instrId = instrumentPanelsIds.get(ip); 
		record(RecordEvent.SET_INSTRUMENT_VOLUME, ip.volumeSlider.getValue(), instrId);
	}
	
	public void recordInstrOctave(InstrumentPanel ip) {
		if(!isRecording)
			return;
		
		int instrId = instrumentPanelsIds.get(ip); 
		record(RecordEvent.SET_INSTRUMENT_OCTAVE, ip.octaveComboBox.getSelectedIndex(), instrId);
	}
	
	public void recordInstrMute(InstrumentPanel ip) {
		if(!isRecording)
			return;
		
		int instrId = instrumentPanelsIds.get(ip); 
		record(RecordEvent.SET_INSTRUMENT_MUTE, ip.muteCheckBox.isSelected(), instrId);
	}
	
	public void recordInstrMuteAll() { 
		record(RecordEvent.MUTE_ALL_INSTRUMENTS, headerPanel.muteAllCheckBox.isSelected());
	}
	
	public void recordRemoveAllInstr() {
		record(RecordEvent.REMOVE_ALL_INSTRUMENTS, 1);
		instrumentPanelsIds = new HashMap<InstrumentPanel, Integer>();
	}
	
	public void recordRemoveInstr(InstrumentPanel ip) {
		record(RecordEvent.REMOVE_INSTRUMENT, 1);
		instrumentPanelsIds.remove(ip);
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
		if(!isRecording)
			return;
		
		RecordEvent newEvent = new RecordEvent(eventType, value, RECORDER_BAR_COUNTER, instrumentId);
		recordedEvents.add(newEvent);
		System.out.println(newEvent.toString());
	}

	public static void incrementCounter() {
		if (isRecording)
			RECORDER_BAR_COUNTER++;
	}


}
