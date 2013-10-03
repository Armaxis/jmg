package core.data;

import util.Log;

public class PNote {

	private int pitch; //Note pitch from 0 to 128
	
	private double absolute_length; //Absolute length. Used only in first note. Otherwise is 0
	private double length; //Relative length. Used for every note.
	
	private int transitionType; //Type of transition between this and previous note. Found in PConstants.
	
	private int[] transitionValues; //Values for transition. 0 for SAME_PITCH
	
	
	private int volumeChange; //Decreasement/increasement in volume. TODO: Implement
	
	private double rhythmValue; //Position inside a bar. 0 - then element is beginning of bar; -1  = not attached to bar

	private Melisma melismas; //Melismas. TODO: replace with lists of melismas
	
	public PNote (String data)
	{
		String[] values = data.split("~");
		transitionType 	= (values.length > 0) ? Integer.parseInt(values[0]) : PConstants.STABLE_NOTE;
		if (transitionType == PConstants.COMPLEX)
		{
			transitionValues = parseComplexTransitionValues(values[1]);
		}
		else
		{
			transitionValues = new int [] {Integer.parseInt(values[1])};
		}
		
		length 			= (values.length > 2) ? Double.parseDouble(values[2]) : 1;
		rhythmValue 	= (values.length > 3) ? Integer.parseInt(values[3]) : 0;
		//pitch 			= (values.length > 4) ? Integer.parseInt(values[4]) : 0;
		//absolute_length = (values.length > 5) ? Integer.parseInt(values[5]) : JMC.QUARTER_NOTE;
		volumeChange 	= 0;
		melismas		= null;
	}
	
	private int[] parseComplexTransitionValues(String s)
	{
		String[] vals = s.split("@");
		int[] result = new int[vals.length];
		for (int i = 0; i < vals.length; i++)
			result[i] = Integer.parseInt(vals[i]);
		return result;
	}
	
	public PNote ()
	{
		
	}
	
	public PNote (int _pitch, double _length)
	{
		this.pitch 				= _pitch;
		this.absolute_length 	= _length;
		this.length 			= 1;
		this.transitionType 	= PConstants.STABLE_NOTE;
		this.transitionValues 	= new int[] {0};
		this.volumeChange 		= 100;
		this.rhythmValue 		= 0;
		this.melismas			= null;
	}
	
	public PNote(PNote _p)
	{
		this.pitch 				= _p.getPitch();
		this.absolute_length 	= _p.getAbsolute_length();
		this.length 			= _p.getLength();
		this.transitionType 	= _p.getTransitionType();
		this.transitionValues 	= _p.getTransitionValue();
		this.volumeChange 		= _p.getVolumeChange();
		this.rhythmValue 		= _p.getRhythmValue();
		this.melismas			= null;
	}

	public PNote(int pitch2, double length2, int volume) {
		this.pitch 				= pitch2;
		this.absolute_length 	= length2;
		this.length 			= 1;
		this.transitionType 	= PConstants.STABLE_NOTE;
		this.transitionValues 	= new int[] {0};
		this.volumeChange 		= volume;
		this.rhythmValue 		= 0;
		this.melismas			= null;
	}

	public int getPitch() {
		return pitch;
	}

	public void setPitch(int pitch) {
		this.pitch = pitch;
		
		if(pitch <= 0)
		{
			Log.severe("PNote > setPitch > Invalid pitch value! Pitch = " + pitch);
		}
	}

	public double getAbsolute_length() {
		return absolute_length;
	}

	public double getLength() {
		return length;
	}

	public void setLength(double length) {
		this.length = length;
	}

	public int getTransitionType() {
		return transitionType;
	}

	public int[] getTransitionValue() {
		return transitionValues;
	}

	public double getRhythmValue() {
		return rhythmValue;
	}
	
	public void setRhythmValue(double d) {
		this.rhythmValue = d;
	}

	public int getVolumeChange() {
		return volumeChange;
	}

	public void setVolumeChange(int volumeChange) {
		this.volumeChange = volumeChange;
	}

	public Melisma getMelismas() {
		return melismas;
	}

	public void setMelismas(Melisma melismas) {
		this.melismas = melismas;
	}
	
	public boolean hasMelismas()
	{
		return (melismas != null);
	}

	public void multiplyLength(double multiplier)
	{
		this.length = this.length * multiplier;
	}
	
	@Override
	public String toString()
	{
		return "[Pnote." +
				((pitch != 0 ) ? " Pitch = " + pitch + ";": "") +
				((absolute_length != 0 ) ? " Absolute Length = " + absolute_length  + ";": "") +
				" Length = " + length  + ";" + 
				" Transition Type = " + PConstants.getTransitionName(transitionType)  + ";" +
				" Transition Value = " + transitionValues  + ";" +
				((rhythmValue != 0 ) ? " Rhythm value = " + rhythmValue + ";" : "") +
				((volumeChange  != 0 ) ? " Volume change = " + volumeChange + ";": "") + 
				((melismas != null ) ? " Melisma = " + melismas.getType() : "") +"]\n";
	}

	public int getVolume() {
		if(this.transitionType == PConstants.STABLE_NOTE)
			return this.volumeChange;
		else
			return 100;
	}
}
