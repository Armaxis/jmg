package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Before;
import org.junit.Test;

import core.data.tonality.MajorTonality;
import core.data.tonality.Tonality;

public class TonalityTests {

	private Tonality CDurTonality;
	
	@Before
	public void setUp() throws Exception {
		CDurTonality = new MajorTonality(60);
	}

	@Test
	public void testGetTonalityNoteByStep() {
		assertEquals(64, CDurTonality.getTonalityNoteByStep(2, 60));
		assertEquals(64, CDurTonality.getTonalityNoteByStep(2, 61));
		assertEquals(64, CDurTonality.getTonalityNoteByStep(1, 63));
		assertEquals(65, CDurTonality.getTonalityNoteByStep(2, 63));
		assertEquals(60, CDurTonality.getTonalityNoteByStep(-2, 64));
		assertEquals(60, CDurTonality.getTonalityNoteByStep(-2, 63));
		assertEquals(72, CDurTonality.getTonalityNoteByStep(7, 60));
		
		assertEquals(60, CDurTonality.getTonalityNoteByStep(-1, 62));
		
		assertFalse(62 == CDurTonality.getTonalityNoteByStep(2, 60));
	}

}
