package de.jbee.core;

import org.junit.BeforeClass;

import de.jbee.core.list.CoreList;
import de.jbee.core.type.Enumerate;

public class TestEnumLister
		extends TestLister {

	@BeforeClass
	public static void setUp() {
		Core.setUp( CoreList.ENUM_LISTER_FACTORY );
		Core.setUpCharacters( Core.enumerator.enumerates( Enumerate.CHARACTERS ) );
		Core.setUpLetters( Core.enumerator.enumerates( Enumerate.LETTERS ) );
	}
}
