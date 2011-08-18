package de.jbee.lang.seq;

import static de.jbee.lang.seq.ListMatcher.hasEqualElementsAsIn;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import de.jbee.lang.List;

public class TestUtileListTranstion {

	@Test
	public void testTakeFirstDropLast() {
		List<Integer> l = List.with.elements( 1, 2, 3, 4, 5 );
		assertThat( List.which.takesFirst( 3 ).dropsLast( 1 ).from( l ),
				hasEqualElementsAsIn( 1, 2 ) );
	}

	@Test
	public void testTrims() {
		List<Integer> l = List.with.elements( 1, 2, 3, 4, 5 );
		assertThat( List.which.trims( 1 ).from( l ), hasEqualElementsAsIn( 2, 3, 4 ) );
		assertThat( List.which.trims( 2 ).from( l ), hasEqualElementsAsIn( 3 ) );
		assertThat( List.which.trims( 3 ).from( l ), hasEqualElementsAsIn( new Integer[0] ) );
	}

	@Test
	public void testSorts() {
		List<Integer> l = List.with.elements( 4, 5, 7, 3, 1, 8 );
		assertThat( List.which.sorts().from( l ), hasEqualElementsAsIn( 1, 3, 4, 5, 7, 8 ) );
	}

	@Test
	public void testShuffle() {
		List<Integer> l = List.with.elements( 4, 5, 7, 3, 1, 8 );
		assertThat( List.which.shuffle.from( l ), not( hasEqualElementsAsIn( 4, 5, 7, 3, 1, 8 ) ) );
	}

	@Test
	public void testNubs() {
		List<Integer> l = List.with.elements( 4, 5, 4, 6, 5, 7, 4 );
		assertThat( List.which.nubs().from( l ), hasEqualElementsAsIn( 4, 5, 6, 7 ) );
	}

	@Test
	public void testSwapsAscending() {
		List<Integer> l = List.with.elements( 1, 2, 3, 4 );
		Integer[] swap01 = { 2, 1, 3, 4 };
		assertThat( List.which.swaps( 0, 1 ).from( l ), hasEqualElementsAsIn( swap01 ) );
		assertThat( List.which.swaps( 1, 0 ).from( l ), hasEqualElementsAsIn( swap01 ) );
	}

	@Test
	public void testSwapsRandom() {
		List<Integer> l = List.with.elements( 9, 5, 2, 8 );
		Integer[] swap01 = { 5, 9, 2, 8 };
		assertThat( List.which.swaps( 0, 1 ).from( l ), hasEqualElementsAsIn( swap01 ) );
		assertThat( List.which.swaps( 1, 0 ).from( l ), hasEqualElementsAsIn( swap01 ) );
		Integer[] swap02 = { 2, 5, 9, 8 };
		assertThat( List.which.swaps( 0, 2 ).from( l ), hasEqualElementsAsIn( swap02 ) );
		assertThat( List.which.swaps( 2, 0 ).from( l ), hasEqualElementsAsIn( swap02 ) );
		Integer[] swap03 = { 8, 5, 2, 9 };
		assertThat( List.which.swaps( 0, 3 ).from( l ), hasEqualElementsAsIn( swap03 ) );
		assertThat( List.which.swaps( 3, 0 ).from( l ), hasEqualElementsAsIn( swap03 ) );
		Integer[] swap23 = { 9, 5, 8, 2 };
		assertThat( List.which.swaps( 2, 3 ).from( l ), hasEqualElementsAsIn( swap23 ) );
		assertThat( List.which.swaps( 3, 2 ).from( l ), hasEqualElementsAsIn( swap23 ) );
	}

	@Test
	public void testSublists() {
		Integer[] larr = { 3, 6, 1, 0, 9, 5, 2, 8 };
		List<Integer> l = List.with.elements( 3, 6, 1, 0, 9, 5, 2, 8 );
		assertThat( List.which.sublists( 0, l.length() ).from( l ), hasEqualElementsAsIn( larr ) );
		assertThat( List.which.sublists( 1, 3 ).from( l ), hasEqualElementsAsIn( 6, 1, 0 ) );
	}

	@Test
	public void testTakeWhile() {
		List<Integer> l = List.with.elements( 1, 4, 7, 9, 2, 4 );
		//TODO continue
	}

}
