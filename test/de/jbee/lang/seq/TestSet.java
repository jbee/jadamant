package de.jbee.lang.seq;

import static de.jbee.lang.seq.ListMatcher.hasEqualElementsAsIn;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import de.jbee.lang.List;
import de.jbee.lang.Set;

public class TestSet {

	@Test
	public void testToSet() {
		Set<Integer> s = List.which.restrictsToSet().from(
				List.with.elements( 1, 1, 2, 5, 3, 4, 2, 5 ) );
		assertThat( s, hasEqualElementsAsIn( 1, 2, 3, 4, 5 ) );
	}
}
