package core.data;

public final class PConstants {
	
	//Transition types
	
	public static final int CHROMATIC_JUMP = 0;
	public static final int TONALITY_JUMP = 1;
	public static final int CHORD_JUMP = 2;
	public static final int SAME_PITCH = 3;
	public static final int STABLE_NOTE = 4;
	public static final int COMPLEX = 5; //Первая часть - прыжок по аккорду, вторая - по тональности;
	
	//Transition jumps weights
	
	public static final int[] PATTERN_WEIGHTS = new int[] {1, 2, 4, 0, 0};
	
	public static String getTransitionName (int type)
	{
		switch (type) {
		case 0:
			return "Chromatic Jump";
		case 1:
			return "Tonality Jump";
		case 2:
			return "Chord Jump";
		case 3:
			return "Same pitch";
		case 4:
			return "Stable note";
		default:
			return "Unknown jump";
		}
	}
}
