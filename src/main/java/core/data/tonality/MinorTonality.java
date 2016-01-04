package core.data.tonality;

import core.data.chords.DimChord;
import core.data.chords.MajorChord;
import core.data.chords.MinorChord;

public class MinorTonality extends Tonality {
	
	public MinorTonality(int pitch) {
		super(pitch, new int[]{0,2,3,5,7,8,10});
		this.degrees.put("I", new MinorChord(pitch + intervals[0]));
		this.degrees.put("II", new DimChord(pitch + intervals[1]));
		this.degrees.put("III", new MajorChord(pitch + intervals[2]));
		this.degrees.put("IV", new MinorChord(pitch + intervals[3]));
		this.degrees.put("V", new MinorChord(pitch + intervals[4]));
		this.degrees.put("VI", new MajorChord(pitch + intervals[5]));
		this.degrees.put("VII", new MajorChord(pitch + intervals[6]));
		
		this.type = "minor";
	}
}
