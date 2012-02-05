package de.jbee.lang.seq;

import org.junit.BeforeClass;

import de.jbee.lang.Lang;

public class TestEvolutionLister
		extends TestLister {

	@BeforeClass
	public static void setUp() {
		Lang.setUp( Sequences.LISTER_ENUMERATOR_FACTORY );
	}
}
