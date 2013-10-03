package core.generators.dodecaphony;

public class Dodecaphonizer {
	private int[] PToneRow;
	private int[] IToneRow;
	private int[] RToneRow;
	private int[] IRToneRow;
	private int pitch;
	
	public Dodecaphonizer(int[] notes) {
		super();
		this.pitch = notes[0];
		PToneRow = new int[notes.length];
		IToneRow = new int[notes.length];
		RToneRow = new int[notes.length];
		IRToneRow = new int[notes.length];
		for (int i=0; i<notes.length; ++i) 
			PToneRow[i] = notes[i]-pitch;
		for (int i=0; i<PToneRow.length; ++i){
			RToneRow[i] = PToneRow[PToneRow.length-i-1];
			IToneRow[i] = PToneRow[i]*(-1);
			IRToneRow[i] = PToneRow[PToneRow.length-i-1]*(-1);
		}
		//printIntArray(PToneRow);
		//printIntArray(IToneRow);
		//printIntArray(RToneRow);
		//printIntArray(IRToneRow);
	}
	public static void printIntArray(int[]arr) {
		for (int i=0; i<arr.length; ++i)
			System.out.print(arr[i]+" ");
		System.out.println();
	}
	public static void printDoubleArray(double[]arr) {
		for (int i=0; i<arr.length; ++i)
			System.out.print(arr[i]+" ");
		System.out.println();
	}
	public int[] getPToneRow() {
		return PToneRow;
	}
	public int[] getIToneRow() {
		return IToneRow;
	}
	public int[] getRToneRow() {
		return RToneRow;
	}
	public int[] getIRToneRow() {
		return IRToneRow;
	}
	public int[] getRandomRowPitched (int p) {
		double rnd = Math.random();
		int[] tempRow = new int[PToneRow.length];
		for (int i=0; i<tempRow.length; ++i){
			if(rnd<0.25)
				tempRow[i]=PToneRow[i]+p;
			else if (rnd>=0.25 && rnd<0.5)
				tempRow[i]=RToneRow[i]+p;
			else if (rnd>=0.5 && rnd<0.75)
				tempRow[i]=IToneRow[i]+p;
			else
				tempRow[i]=IRToneRow[i]+p;
		}
		return tempRow;
	}
}
