package core.data.patterns;

import java.util.LinkedList;
import java.util.List;

import core.data.PNote;

public class CPattern extends Pattern {

	private List<Pattern> patterns;

	public CPattern(String inputdata) {
		super(inputdata);
		patterns = new LinkedList<Pattern>();
		initNotes();
	}
	
	public CPattern(LinkedList<Pattern> _patterns) {
		patterns = _patterns;
		initNotes();
	}
	
	
	public CPattern() {
		patterns = new LinkedList<Pattern>();
		initNotes();
	}
	
	public CPattern (CPattern cp)
	{
		patterns = new LinkedList<Pattern>();
		
		for (Pattern p : cp.getPatterns())
		{
			patterns.add(new Pattern(p));
		}
		
		initNotes();
	}
	
	public void addPattern(Pattern p){
		if(patterns == null)
			patterns = new LinkedList<Pattern>();
		
		patterns.add(p);
		initNotes();
	}
	
	public void initNotes() {
		notes = new LinkedList<PNote>();
		for (Pattern p : patterns)
		{
			notes.addAll(p.getNotes());
		}
	}
	
	@Override
	public void multiplyAllLengths(double multiplier)
	{
		for (Pattern p : patterns)
		{
			p.multiplyAllLengths(multiplier);
		}
		initNotes();
	}
	
	@Override
	public void multiplyFirstNoteLength (double multiplier)
	{
		if (patterns != null && patterns.size() > 0)
		{
			patterns.get(0).multiplyFirstNoteLength(multiplier);
		}
	}
	
	@Override
	public int getOverallPatternJumpLength()
	{
		int overallJump = 0;
		for (Pattern p : patterns)
		{
			overallJump += p.getOverallPatternJumpLength();
		}
		return overallJump;
	}
	
	public List<Pattern> getPatterns() {
		return patterns;
	}
	
	public int sizePatterns()
	{
		return patterns.size();
	}
	
	public int size ()
	{
		int totalSize = 0;
		for (Pattern p : patterns)
		{
			totalSize +=p.size();
		}
		return totalSize;
	}

}
