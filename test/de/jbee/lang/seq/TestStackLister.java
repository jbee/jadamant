package de.jbee.lang.seq;

import org.junit.BeforeClass;

import de.jbee.lang.Lang;
import de.jbee.lang.seq.StackList;

public class TestStackLister
		extends TestLister {

	@BeforeClass
	public static void setUp() {
		Lang.setUp( StackList.ENUMERATOR_FACTORY );
	}
}
