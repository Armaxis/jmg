package core.data;

public class PInstrument {
	public String name;
	public String RussianName;
	public String imageName;
	public int midiId;
	public int octaveIndex;
	
	public PInstrument(String name, String russianName, String imageName,
			int midiId, int octIndex) {
		super();
		this.name = name;
		RussianName = russianName;
		this.imageName = imageName;
		this.midiId = midiId;
		this.octaveIndex = octIndex;
	}
	
	public PInstrument(String name, String russianName, int midiId, int octIndex) {
		super();
		this.name = name;
		RussianName = russianName;
		this.imageName = name;
		this.midiId = midiId;
		this.octaveIndex = octIndex;
	}

	@Override
	public String toString() {
		return "PInstrument [name=" + name + ", midiId=" + midiId
				+ ", octaveIndex=" + octaveIndex + "]";
	}
	
	
}
