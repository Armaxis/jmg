package core.data.chords;

import java.util.Arrays;

public abstract class Chord {
	protected int pitch;
	protected int[] chord;
	protected int[] allChordNotes;
	
	
	public int[] getAllChordNotes() {
		return allChordNotes;
	}

	public Chord(int pitch, int[] chord) {
		super();
		this.pitch = pitch % 12; //При задании тональности "транспонируем" ноту в универсальную октаву (0-11)
		this.chord = new int[chord.length];
		for (int i=0; i< chord.length; ++i)
			this.chord[i] = ( chord[i] + pitch) % 12 ;
		Arrays.sort(this.chord);
		fillChordNotesArray();
	}
	
	public int[] getChord() {
		return chord;
	}
//	public int[] getChordPitched(int pitch) {
//		int[] newChord = chord.clone();
//		for(int i=0; i<newChord.length; ++i)
//			newChord[i]+=pitch;
//		return newChord;
//	}
	public void fillChordNotesArray() {
		//На входе имеем массив chord виде [1,4,9] (в случае ля-мажорного аккорда)
		//По нему строим массив всех возможных нот, т.е. ми, додиезов и ля во всех возможных октавах
		//И в итоге получаем allChordNotes вида
		this.allChordNotes = new int[9*chord.length];
		for (int i=0; i<9; ++i) {
			for(int j=0; j<chord.length; ++j) {
				allChordNotes[i*chord.length + j] = 12 + i*12 + chord[j];
			}
		}
	}
	
	@Override
	public String toString()
	{
		return "[" + pitch + " " + Arrays.toString(chord) + "]";
	}
}
