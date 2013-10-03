package core.data;

import jm.music.data.Tempo;
import core.data.tonality.MajorTonality;
import core.data.tonality.MinorTonality;
import core.data.tonality.Tonality;

public class Settings {
	
	public static Tonality CURRENT_TONALITY;
	public static Tempo TEMPO;
	public static boolean USE_MELISMAS = false;
	public static int MELISMAS_CHANCE = 5;
	public static int OVERALL_VOLUME = 100;
	
	public static int accompanimentBarCounter = 0; //Считает такты. На каждом четвертом такте у всех абсолютно аккомпанементов меняется структура.
	

	public static void setTonalityPitch(int tonalityKey) {
		//TODO: REFACTOR!!!
		if (CURRENT_TONALITY.getType().equals("major"))
		{
			CURRENT_TONALITY = new MajorTonality(tonalityKey + 60);
		} 
		else if (CURRENT_TONALITY.getType().equals("minor"))
		{
			CURRENT_TONALITY = new MinorTonality(tonalityKey + 60);
		}
	}

	public static void setTonalityType(String key) {
		//TODO: REFACTOR!!!
		if (key.equals("major"))
		{
			CURRENT_TONALITY = new MajorTonality(CURRENT_TONALITY.getPitch());
		} 
		else if (key.equals("minor"))
		{
			CURRENT_TONALITY = new MinorTonality(CURRENT_TONALITY.getPitch());
		}
	}

}
