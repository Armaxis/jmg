package util;

import java.util.GregorianCalendar;

public class TStamps {
	private static long timestamp = 0;
	
	public static void addTimeStamp()
	{
		timestamp = (new GregorianCalendar(2007, 9 - 1, 23)).getTimeInMillis();
	}
	
	public static long getTimeStamp()
	{
		return (new GregorianCalendar(2007, 9 - 1, 23)).getTimeInMillis() - timestamp;
	}
	
	public static void add()
	{
		addTimeStamp();
	}
	
	public static void print()
	{
		System.out.println(getTimeStamp());
	}
}
