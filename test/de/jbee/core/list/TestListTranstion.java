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

	@Test
	public void testSorts() {
		List<Integer> l = List.with.elements( 4, 5, 7, 3, 1, 8 );
		assertThat( List.which.sorts().from( l ), hasEqualElementsAsIn( List.with.elements( 1, 3,
				4, 5, 7, 8 ) ) );
	}

	@Test
	public void testNubs() {
		List<Integer> l = List.with.elements( 4, 5, 4, 6, 5, 7, 4 );
		assertThat( List.which.nubs().from( l ), hasEqualElementsAsIn( List.with.elements( 4, 5, 6,
				7 ) ) );
	}

}
