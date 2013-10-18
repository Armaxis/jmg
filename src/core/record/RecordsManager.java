package core.record;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Random;

import javax.swing.JOptionPane;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import util.Log;
import core.data.DataStorage;
import gui.GUI;
import gui.HeaderPanel;
import gui.InstrumentPanel;
import gui.MainPanel;
import gui.RecordFrame;

public class RecordsManager {

	public static int RECORDER_BAR_COUNTER = 0;
	public static int PLAYBACK_BAR_COUNTER = 0;
	public static final String RECORDS_FOLDER = "records"; 
	public static final String RECORD_EXTENSION = ".jmr";
	public static boolean isRecording;
	public static boolean isPlaying;
	
	private MainPanel mainPanel;
	private HeaderPanel headerPanel;
	private RecordFrame recorderFrame;
	private LinkedList<RecordEvent> recordedEvents;
	public LinkedList<RecordEvent> playbackEvents;
	private HashMap<InstrumentPanel, Integer> instrumentPanelsIds;
	private HashMap<Integer, InstrumentPanel> playbackInstrumentPanelsIds;
	private int nextPanelIdCounter;
	private int nextPlaybackPanelIdCounter;
	public LinkedList<String> recordsList;
	
	public RecordsManager() {
		isRecording = false;
		isPlaying = false;
		RECORDER_BAR_COUNTER = 0;
		nextPanelIdCounter = 0;
		nextPlaybackPanelIdCounter = 0;
		recordedEvents = new LinkedList<RecordEvent>();
		playbackEvents = new LinkedList<RecordEvent>();
		instrumentPanelsIds = new HashMap<InstrumentPanel, Integer>();
		playbackInstrumentPanelsIds = new HashMap<Integer, InstrumentPanel>();
		refreshRecordsList();
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


	public void stopRecord() {
		isRecording = false;

		//Ask user for name for record
		DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH-mm-ss");
		String defaultRecordName = "Record " + dateFormat.format(Calendar.getInstance().getTime());
		String fileName = (String)JOptionPane.showInputDialog(
                RecordFrame.frame,
                "Введите имя записи:",
                "Сохранение записи",
                JOptionPane.PLAIN_MESSAGE,
                null,
                null,
                defaultRecordName);

		if (fileName == null)
		{
			//User pressed cancel. Ok then.
			RECORDER_BAR_COUNTER = 0;
			return;
		}
		
		if(fileName.isEmpty())
			fileName= defaultRecordName;
		
		//Check if this record exists in folder. If does - add _1, _2, and so on until it doesnt exist
		int counter = 1;
		File saveFile = new File(RECORDS_FOLDER + "/" + fileName + RECORD_EXTENSION);
		
		while(saveFile.exists())
		{
			saveFile = new File(RECORDS_FOLDER + "/" + fileName + "_" + counter + RECORD_EXTENSION);
			counter++;
		}
		
		//Remove useless commands before writing to file
		filterEvents(); 
		
		//Save to file whatever
		try {
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

			// root elements
			Document doc = docBuilder.newDocument();
			Element rootElement = doc.createElement("record");
			doc.appendChild(rootElement);
			
			for(RecordEvent event : recordedEvents)
			{
				// Add event element
				Element eventElement = doc.createElement("Event");
				rootElement.appendChild(eventElement);
		 
				// Set attributes 
				Attr attr = doc.createAttribute("type");
				attr.setValue(event.getType());
				eventElement.setAttributeNode(attr);
				
				attr = doc.createAttribute("value");
				attr.setValue(event.getValue());
				eventElement.setAttributeNode(attr);
				
				attr = doc.createAttribute("bar");
				attr.setValue(Integer.toString(event.getBar()));
				eventElement.setAttributeNode(attr);
				
				attr = doc.createAttribute("instr");
				attr.setValue(Integer.toString(event.getInstrumentId()));
				eventElement.setAttributeNode(attr);
			}
			
			// write the content into xml file
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(saveFile);
	 
			// Output to console for testing
			//StreamResult result = new StreamResult(System.out);
	 
			transformer.transform(source, result);
	 
			Log.info("Record saved to: " + saveFile.getName());
			
		} catch (ParserConfigurationException pce) {
			pce.printStackTrace();
		} catch (TransformerException tfe) {
			tfe.printStackTrace();
		}
		
		//Update list of records
		refreshRecordsList();
		
		RECORDER_BAR_COUNTER = 0;
	}
	
	private void filterEvents()
	{
		HashMap<String, RecordEvent> eventsMap = new HashMap<String, RecordEvent>();
		
		for (RecordEvent event : recordedEvents)
		{
			String tag = event.getBar() + "~" + event.getInstrumentId() + "~" + event.getType();
			eventsMap.put(tag, event);
		}
		
		recordedEvents = new LinkedList<RecordEvent>();
		for (RecordEvent event : eventsMap.values())
		{
			recordedEvents.add(event);
		}
		
		Collections.sort(recordedEvents);
	}
	
	public void init(GUI gui) {
		this.mainPanel = gui.frame.getMainPanel();
		this.headerPanel = mainPanel.headerPanel;
		this.recorderFrame = gui.frame.getRecorderFrame();
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
		
		record(RecordEvent.ADD_INSTRUMENT, nextPanelIdCounter, nextPanelIdCounter);
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
		
		RecordEvent newEvent = new RecordEvent(eventType, value, RECORDER_BAR_COUNTER - 1, instrumentId);
		recordedEvents.add(newEvent);
		recorderFrame.log(newEvent.toString());
	}

	public static void incrementCounter() {
		if (isRecording)
			RECORDER_BAR_COUNTER++;
//		
//		if(isPlaying)
//			PLAYBACK_BAR_COUNTER++;
	}


	private void refreshRecordsList() {
		recordsList = new LinkedList<String>();
		File folder = new File(RECORDS_FOLDER);
		for(File f: folder.listFiles())
		    if(f.getName().endsWith(RECORD_EXTENSION))
		    	recordsList.add(f.getName().substring(0, f.getName().lastIndexOf(".")));
	}

	public void playRecord(String selectedValue) {
		isPlaying = true;
		nextPlaybackPanelIdCounter = 0;
		PLAYBACK_BAR_COUNTER = 0;
		readRecordFile(selectedValue);
	}

	public void stopPlayingRecord() {
		isPlaying = false;
		nextPlaybackPanelIdCounter = 0;
		PLAYBACK_BAR_COUNTER = 0;
	}

	public void playBar(int barId)
	{
		if(!isPlaying)
			return;
		
		for (RecordEvent event : playbackEvents)
		{
			if(event.getBar() != barId)
				continue;
				
			playEvent(event);
		}
		
		PLAYBACK_BAR_COUNTER++;
		
		//Check if we reached end
		if(playbackEvents.getLast().getBar() < barId)
		{
			stopPlayingRecord();
			recorderFrame.playButton.setText("Загрузить");
			recorderFrame.log("Воспроизведение завершено!");
		}
				
	}
	
	private void playEvent(RecordEvent event) {
		//There goes switch. But type in event isn't int, so there will be not switch but "equals"
		String eventType = event.getType();
		if(eventType.equals(RecordEvent.CHANGE_TONALITY_KEY)) {
			
			headerPanel.tonalityKey.setSelectedIndex(Integer.parseInt(event.getValue()));
			
		} else if(eventType.equals(RecordEvent.CHANGE_TONALITY_TYPE)) {
			
			if (event.getValue().equals("1")) 
				headerPanel.tonalityTypes.getFirst().setSelected(true); //Major
			else
				headerPanel.tonalityTypes.get(1).setSelected(true); //Minor
			
		} else if(eventType.equals(RecordEvent.SET_MELISMAS)) {
			
			headerPanel.melismaCheckBox.setSelected(event.getValue().equals("1"));
			
		} else if(eventType.equals(RecordEvent.SET_MELISMAS_CHANCE)) {
			
			headerPanel.melismaComboBox.setSelectedIndex(Integer.parseInt(event.getValue()));
			
		} else if(eventType.equals(RecordEvent.SET_TEMPO)) {
			
			headerPanel.tempoSlider.setValue(Integer.parseInt(event.getValue()));
			
		} else if(eventType.equals(RecordEvent.SET_OVERALL_VOLUME)) {
			
			headerPanel.volumeSlider.setValue(Integer.parseInt(event.getValue()));
			
		} else if(eventType.equals(RecordEvent.ADD_INSTRUMENT)) {
			
			InstrumentPanel panel = mainPanel.addInstrument();
			playbackInstrumentPanelsIds.put(nextPlaybackPanelIdCounter, panel);
			nextPlaybackPanelIdCounter++;
			
		} else if(eventType.equals(RecordEvent.REMOVE_INSTRUMENT)) {
			
			InstrumentPanel panel = playbackInstrumentPanelsIds.get(event.getInstrumentId());
			if (panel != null)
				mainPanel.removeInstrument(panel.trackId);
						
		} else if(eventType.equals(RecordEvent.REMOVE_ALL_INSTRUMENTS)) {
			
			mainPanel.removeAllInstruments();
			playbackInstrumentPanelsIds = new HashMap<Integer, InstrumentPanel>();
			
		} else if(eventType.equals(RecordEvent.MUTE_ALL_INSTRUMENTS)) {
			
			for (InstrumentPanel pan : playbackInstrumentPanelsIds.values())
			{
				pan.muteCheckBox.setSelected(event.getValue().equals("1"));
				pan.setMuted(event.getValue().equals("1"));
			}
		
		} else if(eventType.equals(RecordEvent.SET_INSTRUMENT_TYPE)) {
			
			InstrumentPanel panel = playbackInstrumentPanelsIds.get(event.getInstrumentId());
			if (panel != null)
				panel.instrumentCombo.setSelectedIndex(DataStorage.getInstrumentIdByMidiId(Integer.parseInt(event.getValue())));
			
		} else if(eventType.equals(RecordEvent.SET_INSTRUMENT_ROLE)) {
			
			InstrumentPanel panel = playbackInstrumentPanelsIds.get(event.getInstrumentId());
			if (panel != null)
				panel.instrumentTypeCombo.setSelectedIndex(Integer.parseInt(event.getValue()));
			
		} else if(eventType.equals(RecordEvent.SET_INSTRUMENT_VOLUME)) {
			
			InstrumentPanel panel = playbackInstrumentPanelsIds.get(event.getInstrumentId());
			if (panel != null)
				panel.volumeSlider.setValue(Integer.parseInt(event.getValue()));
			
		} else if(eventType.equals(RecordEvent.SET_INSTRUMENT_OCTAVE)) {
			
			InstrumentPanel panel = playbackInstrumentPanelsIds.get(event.getInstrumentId());
			if (panel != null)
				panel.octaveComboBox.setSelectedIndex(Integer.parseInt(event.getValue()));
			
		} else if(eventType.equals(RecordEvent.SET_INSTRUMENT_MUTE)) {
			
			InstrumentPanel panel = playbackInstrumentPanelsIds.get(event.getInstrumentId());
			if (panel != null)
			{
				panel.muteCheckBox.setSelected(event.getValue().equals("1"));
				panel.setMuted(event.getValue().equals("1"));
			}
		}
		
		recorderFrame.log("Выполнено: " + event.toString());
	}

	public void deleteRecord(String selectedValue) {
		File f = new File(RECORDS_FOLDER + "/" + selectedValue + RECORD_EXTENSION);
		if(f.exists())
			f.delete();
		refreshRecordsList();		
	}
	
	public void readRecordFile(String fileName)
	{
		try {
			playbackEvents = new LinkedList<RecordEvent>();
			File fXmlFile = new File(RECORDS_FOLDER + "/" + fileName + RECORD_EXTENSION);
			
			if (!fXmlFile.exists())
			{
				Log.severe("RecordsManager > readRecordFile() > File " + fileName + " not found!");
			}
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);
		 
			//optional, but recommended
			//read this - http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
			doc.getDocumentElement().normalize();
		 
			NodeList nList = doc.getElementsByTagName("Event");
		 	for (int temp = 0; temp < nList.getLength(); temp++) {
		 
				Node nNode = nList.item(temp);
		 		if (nNode.getNodeType() == Node.ELEMENT_NODE) {
		 
					Element el = (Element) nNode;
					
					RecordEvent event = new RecordEvent(
						el.getAttribute("type"),
						el.getAttribute("value"),
						Integer.parseInt(el.getAttribute("bar")),
						Integer.parseInt(el.getAttribute("instr"))
					);
					
					playbackEvents.add(event);
				}
			}
		 	Collections.sort(playbackEvents);
	    } catch (Exception e) {
	    	e.printStackTrace();
	    }
	}
}
