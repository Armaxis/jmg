package core.data.patterns;

import java.util.LinkedList;

import core.data.PNote;

/** Single-note pattern. Contains only one note with mark "Stable Note"
 * @author Armaxis
 *
 */
public class SNPattern extends Pattern {

	
	public SNPattern (int pitch, double length)
	{
		//Creates a pattern with single note, which is beginning of phrase
		PNote note = new PNote(pitch, length);
		notes = new LinkedList<PNote>();
		notes.add(note);
	}
	
//	public SNPattern (int pitch, double length, int volume)
//	{
//		//Creates a pattern with single note, which is beginning of phrase
//		PNote note = new PNote(pitch, length, volume);
//		notes = new LinkedList<PNote>();
//		notes.add(note);
//	}
	
	public SNPattern (PNote note)
	{
		//Creates a pattern with single note, which is beginning of phrase
		notes = new LinkedList<PNote>();
		notes.add(note);
	}

	public SNPattern(Pattern first) {
		PNote firstNoteInPattern = first.getNotes().get(0);
		
		notes = new LinkedList<PNote>();
		notes.add(new PNote(firstNoteInPattern.getPitch(), firstNoteInPattern.getAbsolute_length()));
	}
	
	public SNPattern(SNPattern snPattern) {
		notes = new LinkedList<PNote>();
		notes.add(new PNote(snPattern.getPitch(), snPattern.getAbsoluteLength()));
	}

	public int getPitch()
	{
		return notes.get(0).getPitch();
	}
	
	public double getAbsoluteLength()
	{
		return notes.get(0).getAbsolute_length();
	}
	
	public double getRhythmValue()
	{
		return notes.get(0).getRhythmValue();
	}
	
	@Override
	public int getOverallPatternJumpLength()
	{
		return 0;
	}
}
