package de.jbee.lang.seq;

import org.junit.BeforeClass;

import de.jbee.lang.Lang;

public class TestStackLister
		extends TestLister {

	@BeforeClass
	public static void setUp() {
		Lang.setUp( StackList.ENUMERATOR_FACTORY );
	}
}