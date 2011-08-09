package de.jbee.core.list;

import static de.jbee.core.Core._;
import static de.jbee.core.list.ListMatcher.hasEqualElementsAsIn;
import static org.hamcrest.CoreMatchers.not;
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
	public void testShuffle() {
		List<Integer> l = List.with.elements( 4, 5, 7, 3, 1, 8 );
		assertThat( List.which.shuffle.from( l ), not( hasEqualElementsAsIn( l ) ) );
	}

	@Test
	public void testNubs() {
		List<Integer> l = List.with.elements( 4, 5, 4, 6, 5, 7, 4 );
		assertThat( List.which.nubs().from( l ), hasEqualElementsAsIn( List.with.elements( 4, 5, 6,
				7 ) ) );
	}

	@Test
	public void testSwapsAscending() {
		List<Integer> l = List.with.elements( 1, 2, 3, 4 );
		List<Integer> swap01 = List.with.elements( 2, 1, 3, 4 );
		assertThat( List.which.swaps( 0, 1 ).from( l ), hasEqualElementsAsIn( swap01 ) );
		assertThat( List.which.swaps( 1, 0 ).from( l ), hasEqualElementsAsIn( swap01 ) );
	}

	@Test
	public void testSwapsRandom() {
		List<Integer> l = List.with.elements( 9, 5, 2, 8 );
		List<Integer> swap01 = List.with.elements( 5, 9, 2, 8 );
		assertThat( List.which.swaps( 0, 1 ).from( l ), hasEqualElementsAsIn( swap01 ) );
		assertThat( List.which.swaps( 1, 0 ).from( l ), hasEqualElementsAsIn( swap01 ) );
		List<Integer> swap02 = List.with.elements( 2, 5, 9, 8 );
		assertThat( List.which.swaps( 0, 2 ).from( l ), hasEqualElementsAsIn( swap02 ) );
		assertThat( List.which.swaps( 2, 0 ).from( l ), hasEqualElementsAsIn( swap02 ) );
		List<Integer> swap03 = List.with.elements( 8, 5, 2, 9 );
		assertThat( List.which.swaps( 0, 3 ).from( l ), hasEqualElementsAsIn( swap03 ) );
		assertThat( List.which.swaps( 3, 0 ).from( l ), hasEqualElementsAsIn( swap03 ) );
		List<Integer> swap23 = List.with.elements( 9, 5, 8, 2 );
		assertThat( List.which.swaps( 2, 3 ).from( l ), hasEqualElementsAsIn( swap23 ) );
		assertThat( List.which.swaps( 3, 2 ).from( l ), hasEqualElementsAsIn( swap23 ) );
	}

	@Test
	public void testSublists() {
		List<Integer> l = List.with.elements( 3, 6, 1, 0, 9, 5, 2, 8 );
		assertThat( List.which.sublists( 0, l.size() ).from( l ), hasEqualElementsAsIn( l ) );
		assertThat( List.which.sublists( 1, 3 ).from( l ), hasEqualElementsAsIn( _( 6, 1, 0 ) ) );
	}

}
