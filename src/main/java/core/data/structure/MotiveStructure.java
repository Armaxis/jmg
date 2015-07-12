package core.data.structure;

import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MotiveStructure {

	private LinkedList<String> motiveTypes;
	private String name;
	
	public MotiveStructure(String inputData)
	{
		motiveTypes = new LinkedList<String>();
		Pattern MOTIVE_PATTERN = Pattern.compile("\\w[']*");
		Matcher m = MOTIVE_PATTERN.matcher(inputData);
		while (m.find()) {
		    String s = m.group();
		    motiveTypes.add(s);
		}
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public String toString()
	{
		return "[" + name + " " + motiveTypes.toString() + "]";
	}
	
	public LinkedList<String> getMotiveTypes() {
		return motiveTypes;
	}
	
	/** Returns size of motiveTypes list.
	 * @return
	 */
	public int size()
	{
		return motiveTypes.size();
	}

}
