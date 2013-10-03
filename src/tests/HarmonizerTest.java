package tests;

import java.util.HashMap;
import java.util.LinkedList;

import org.junit.Before;
import org.junit.Test;

import core.data.Motive;
import core.data.PNote;
import core.data.chords.Chord;
import core.data.patterns.SNPattern;
import core.data.tonality.MajorTonality;
import core.generators.Harmonizer;

public class HarmonizerTest {

	
	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testGetHarmonyForMotive() {
//		Motive m = new Motive();
//		m.firstRunStables = new LinkedList<SNPattern>();
//		
//		m.firstRunStables.add(new SNPattern(64, 1.0));
//		m.firstRunStables.add(new SNPattern(69, 1.0));
//		m.firstRunStables.add(new SNPattern(65, 1.0));
//		m.firstRunStables.add(new SNPattern(62, 1.0));
//		m.firstRunStables.add(new SNPattern(59, 1.0));
//		m.firstRunStables.add(new SNPattern(64, 1.0));
//		m.firstRunStables.add(new SNPattern(60, 1.0));
//		m.firstRunStables.add(new SNPattern(57, 1.0));
//		
//		Harmonizer hr = new Harmonizer();
//		HashMap<Double, Chord> harmony = hr.getHarmonyForMotive(m, new MajorTonality(60));
		//System.out.println(harmony);
	}

	@Test
	public void testGetHarmony1() {
		
		int[] pitches = new int[]{72, 76, 69, 67, 67, 74, 76, 74};
		int[] bars = new int[]{0, 2, 0, 3, 0, 2, 3, 0};
		Motive m = new Motive();
		m.firstRunStables = new LinkedList<SNPattern>();
		
		for (int i = 0; i < pitches.length; i++)
		{
			PNote pn = new PNote(pitches[i], 1.0);
			pn.setRhythmValue(bars[i]);
			m.firstRunStables.add(new SNPattern(pn));
		}
		
		Harmonizer hr = new Harmonizer();
		HashMap<Double, Chord> harmony = hr.getHarmonyForMotive(m, new MajorTonality(60));
		System.out.println(harmony);
	}
	
	@Test
	public void testGetHarmony2() {
		Motive m = new Motive();
		m.firstRunStables = new LinkedList<SNPattern>();
		
		m.firstRunStables.add(new SNPattern(76, 1.0));
		m.firstRunStables.add(new SNPattern(69, 1.0));
		m.firstRunStables.add(new SNPattern(72, 1.0));
		m.firstRunStables.add(new SNPattern(71, 1.0));
		m.firstRunStables.add(new SNPattern(74, 1.0));
		m.firstRunStables.add(new SNPattern(76, 1.0));
		m.firstRunStables.add(new SNPattern(72, 1.0));
		m.firstRunStables.add(new SNPattern(77, 1.0));
		m.firstRunStables.add(new SNPattern(74, 1.0));
		m.firstRunStables.add(new SNPattern(76, 1.0));
		m.firstRunStables.add(new SNPattern(74, 1.0));
		
		Harmonizer hr = new Harmonizer();
		HashMap<Double, Chord> harmony = hr.getHarmonyForMotive(m, new MajorTonality(60));
		System.out.println(harmony);
	}
}
