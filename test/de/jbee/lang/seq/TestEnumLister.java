package de.jbee.lang.seq;

import org.junit.BeforeClass;

import de.jbee.lang.Enumerate;

public class TestEnumLister
		extends TestLister {

	@BeforeClass
	public static void setUp() {
		Sequences.setUp( EnumList.ENUMERATOR_FACTORY );
		Sequences.setUpCharacters( Sequences.enumerator.enumerate( Enumerate.CHARACTERS ) );
		Sequences.setUpLetters( Sequences.enumerator.enumerate( Enumerate.LETTERS ) );
	}
}
