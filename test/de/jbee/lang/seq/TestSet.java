package de.jbee.lang.seq;

import org.junit.Test;

import de.jbee.lang.List;
import de.jbee.lang.Set;

public class TestSet {

	@Test
	public void testInsert() {
		Set<Integer> s = List.which.restrictsToSet().from( List.with.elements( 1, 2, 3, 4, 2, 5 ) );
	}
}
