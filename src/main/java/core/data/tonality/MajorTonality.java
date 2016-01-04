package core.data.tonality;

import core.data.chords.DimChord;
import core.data.chords.MajorChord;
import core.data.chords.MinorChord;

public class MajorTonality extends Tonality{

	public MajorTonality(int pitch) {
		super(pitch, new int[]{0,2,4,5,7,9,11});
		this.degrees.put("I", new MajorChord(pitch + intervals[0]));
		this.degrees.put("II", new MinorChord(pitch + intervals[1]));
		this.degrees.put("III", new MinorChord(pitch + intervals[2]));
		this.degrees.put("IV", new MajorChord(pitch + intervals[3]));
		this.degrees.put("V", new MajorChord(pitch + intervals[4]));
		this.degrees.put("VI", new MinorChord(pitch + intervals[5]));
		this.degrees.put("VII", new DimChord(pitch + intervals[6]));
		
		this.type = "major";
	}
	
}
