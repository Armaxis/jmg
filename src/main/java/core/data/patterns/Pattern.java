package core.data.patterns;

import java.util.LinkedList;
import java.util.List;

import core.data.PConstants;
import core.data.PNote;

public class Pattern{
	protected List<PNote> notes; //All the notes and patterns inside this one
	private String name; //Name of the pattern.

	public List<PNote> getNotes() {
		return notes;
	}
	
	public Pattern ()
	{
		
	}
	
	public Pattern (Pattern _p)
	{
		notes = new LinkedList<PNote>();
		for (PNote pn : _p.getNotes())
		{
			notes.add(new PNote(pn));
		}
	}
	
	public Pattern (String inputdata)
	{
		notes = new LinkedList<PNote>();
		String[] rows = inputdata.split(";");
		
		for (String s : rows)
			notes.add(new PNote(s));
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public void multiplyAllLengths(double multiplier)
	{
		for (PNote pnote : notes)
		{
			pnote.multiplyLength(multiplier);
		}
	}
	
	public void multiplyFirstNoteLength(double multiplier)
	{
		if (notes != null && notes.size() > 0)
			notes.get(0).multiplyLength(multiplier);
	}

	@Override
	public String toString()
	{
		String result = "[Pattern]";
		for (PNote pnote : notes)
		{
			result += pnote.toString();
		}
		return result;
	}
	
	public int getOverallPatternJumpLength()
	{
		int overallJump = 0;
		for (PNote pnote : notes)
		{
			if(pnote.getTransitionType() != PConstants.COMPLEX)
			{
				overallJump += (PConstants.PATTERN_WEIGHTS[pnote.getTransitionType()] * pnote.getTransitionValue()[0]);
			} else {
				overallJump += (PConstants.PATTERN_WEIGHTS[PConstants.CHORD_JUMP] * pnote.getTransitionValue()[0]);
				overallJump += (PConstants.PATTERN_WEIGHTS[PConstants.TONALITY_JUMP] * pnote.getTransitionValue()[1]);
			}
		}
		return overallJump;
	}
	
	public double getPatternRelativeLength()
	{
		double relativeLength = 0.0d;
		double prevLength = 1;
		for (PNote pnote : notes)
		{
			relativeLength += prevLength * pnote.getLength();
			prevLength = prevLength * pnote.getLength();
		}
		return relativeLength;
	}
	
	public PNote getFirstNote()
	{
		return notes.get(0);
	}
	
	public int size()
	{
		return notes.size();
	}
}
