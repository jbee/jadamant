package de.jbee.core.list;

import static de.jbee.core.list.ListMatcher.hasEqualElementsAsIn;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class TestListTranstion {

	@Test
	public void testTakeFirstDropLast() {
		List<Integer> l = List.with.elements( 1, 2, 3, 4, 5 );
		assertThat( List.which.takesFirst( 3 ).dropsLast( 1 ).from( l ),
				hasEqualElementsAsIn( List.with.elements( 1, 2 ) ) );
	}

	@Test
	public void testTrims() {
		List<Integer> l = List.with.elements( 1, 2, 3, 4, 5 );
		assertThat( List.which.trims( 1 ).from( l ), hasEqualElementsAsIn( List.with.elements( 2,
				3, 4 ) ) );
		assertThat( List.which.trims( 2 ).from( l ), hasEqualElementsAsIn( List.with.element( 3 ) ) );
		assertThat( List.which.trims( 3 ).from( l ),
				hasEqualElementsAsIn( List.with.<Integer> noElements() ) );
	}

}
