package de.jbee.lang.seq;

import org.junit.BeforeClass;

import de.jbee.lang.Enumerate;
import de.jbee.lang.Lang;

public class TestEnumLister
		extends TestLister {

	@BeforeClass
	public static void setUp() {
		Lang.setUp( EnumList.ENUMERATOR_FACTORY );
		Lang.setUpCharacters( Lang.enumerator.enumerates( Enumerate.CHARACTERS ) );
		Lang.setUpLetters( Lang.enumerator.enumerates( Enumerate.LETTERS ) );
	}
}
