package de.jbee.lang.seq;

import org.junit.BeforeClass;

public class TestEvolutionLister
		extends TestLister {

	@BeforeClass
	public static void setUp() {
		Sequences.setUp( Sequences.LISTER_ENUMERATOR_FACTORY );
	}
}
