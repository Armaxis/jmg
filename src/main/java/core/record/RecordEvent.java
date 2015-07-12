package core.record;

public class RecordEvent implements Comparable<RecordEvent>{
	
	public static String CHANGE_TONALITY_KEY 	= "tonalityKeyChanged";
	public static String CHANGE_TONALITY_TYPE 	= "tonalityTypeChanged"; //0 - минор, 1 - мажор
	public static String SET_MELISMAS 			= "setMelismas";
	public static String SET_MELISMAS_CHANCE 	= "changeMelismaChance";
	public static String SET_TEMPO		 		= "setTempo";
	public static String SET_OVERALL_VOLUME 	= "setOverallVolume";
	public static String ADD_INSTRUMENT	 		= "addInstr";
	public static String REMOVE_INSTRUMENT		= "removeInstr";
	public static String REMOVE_ALL_INSTRUMENTS	= "removeAllInstr";
	public static String MUTE_ALL_INSTRUMENTS	= "muteAllInstr";
	
	public static String SET_INSTRUMENT_TYPE	= "setInstType";
	public static String SET_INSTRUMENT_ROLE	= "setInstRole";
	public static String SET_INSTRUMENT_VOLUME	= "setInstVolume";
	public static String SET_INSTRUMENT_OCTAVE	= "setInstOctave";
	public static String SET_INSTRUMENT_MUTE	= "setInstMute";
	
	
	private String type;
	private String value;
	private int instrumentId;
	private int bar;	//With blackjack and whores, of course!
	
	public RecordEvent(String type, String value, int bar) {
		super();
		this.type = type;
		this.value = value;
		this.instrumentId = -1;
		this.bar = bar;
	}

	public RecordEvent(String type, String value, int bar, int instrumentId) {
		super();
		this.type = type;
		this.value = value;
		this.instrumentId = instrumentId;
		this.bar = ( bar > 0 ) ? bar : 0;
	}

	@Override
	public String toString() {
		return "Record [Bar=" + bar + ", Type=" + type + ", Value=" + value
				+ ", InstrumentId=" + instrumentId + "]";
	}

	public String getType() {
		return type;
	}

	public String getValue() {
		return value;
	}

	public int getInstrumentId() {
		return instrumentId;
	}

	public int getBar() {
		return bar;
	}

	@Override
	public int compareTo(RecordEvent o) {
		if (this.bar < o.getBar())
			return -1;
		else if (this.bar > o.getBar())
			return 1;
		else if (this.instrumentId < o.getInstrumentId())
			return -1;
		else if (this.instrumentId > o.getInstrumentId())
			return 1;
		else if (this.type.equals(ADD_INSTRUMENT) && !o.getType().equals(ADD_INSTRUMENT))
			return -1;
		else if (!this.type.equals(ADD_INSTRUMENT) && o.getType().equals(ADD_INSTRUMENT))
			return 1;
		else 
			return this.getValue().compareTo(o.getValue());
	}
	
}
