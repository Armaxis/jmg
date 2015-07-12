package core.data;

public class Melisma {

	public static final int GRACE = 0;
	public static final int TRILL = 1;
	private int pitchValue;
	private int type;
	 
	
	//TODO: Can only be applied for stable notes for now, sorry,
	
	public Melisma(int type, int pitchValue) {
		super();
		this.pitchValue = pitchValue;
		this.type = type;
	}
	
	public Melisma(Melisma m) {
		// TODO Auto-generated constructor stub
		this.pitchValue = m.pitchValue;
		this.type 		= m.type;
	}

	public int getPitchValue() {
		return pitchValue;
	}
	public void setPitchValue(int pitchValue) {
		this.pitchValue = pitchValue;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	
	@Override
	public String toString()
	{
		return "[Melisma." +
				((pitchValue != 0 ) ? " Pitch value = " + pitchValue + ";": "") +
				((type != 0 ) ? " Type = " + type: "") +"]\n";
	}
}
