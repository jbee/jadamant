package de.jbee.lang.seq;

import static de.jbee.lang.seq.ListMatcher.hasEqualElementsAsIn;
import static de.jbee.lang.seq.ListMatcher.hasNoElements;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import de.jbee.lang.Is;
import de.jbee.lang.List;
import de.jbee.lang.Predicate;
import de.jbee.lang.Set;

public class TestListAlteration {

	@Test
	public void testInit() {
		List<Integer> l = List.with.elements( 7, 9 );
		assertThat( List.alterBy.init().from( l ), hasEqualElementsAsIn( 7 ) );
	}

	@Test
	public void testTail() {
		assertThat( List.alterBy.tail().from( List.with.<Integer> noElements() ),
				hasNoElements( Integer.class ) );
		assertThat( List.alterBy.tail().from( List.with.elements( 7 ) ),
				hasNoElements( Integer.class ) );
		assertThat( List.alterBy.tail().tail().from( List.with.elements( 7, 9 ) ),
				hasNoElements( Integer.class ) );
		assertThat( List.alterBy.tail().from( List.with.elements( 7, 9 ) ),
				hasEqualElementsAsIn( 9 ) );
		assertThat( List.alterBy.tail().from( List.with.elements( 7, 9, 2 ) ),
				hasEqualElementsAsIn( 9, 2 ) );
	}

	@Test
	public void testDropsLast() {
		List<Integer> l = List.with.elements( 7, 9, 3, 1, 5 );
		assertThat( List.alterBy.dropRight( -1 ).from( l ), hasEqualElementsAsIn( 7, 9, 3, 1, 5 ) );
		assertThat( List.alterBy.dropRight( 0 ).from( l ), hasEqualElementsAsIn( 7, 9, 3, 1, 5 ) );
		assertThat( List.alterBy.dropRight( 1 ).from( l ), hasEqualElementsAsIn( 7, 9, 3, 1 ) );
		assertThat( List.alterBy.dropRight( 2 ).from( l ), hasEqualElementsAsIn( 7, 9, 3 ) );
		assertThat( List.alterBy.dropRight( 3 ).from( l ), hasEqualElementsAsIn( 7, 9 ) );
		assertThat( List.alterBy.dropRight( 4 ).from( l ), hasEqualElementsAsIn( 7 ) );
		assertThat( List.alterBy.dropRight( 5 ).from( l ), hasNoElements( Integer.class ) );
		assertThat( List.alterBy.dropRight( 6 ).from( l ), hasNoElements( Integer.class ) );
	}

	@Test
	public void testTakesFirst() {
		List<Integer> l = List.with.elements( 7, 9, 3, 1, 5 );
		assertThat( List.alterBy.take( -1 ).from( l ), hasNoElements( Integer.class ) );
		assertThat( List.alterBy.take( 0 ).from( l ), hasNoElements( Integer.class ) );
		assertThat( List.alterBy.take( 1 ).from( l ), hasEqualElementsAsIn( 7 ) );
		assertThat( List.alterBy.take( 2 ).from( l ), hasEqualElementsAsIn( 7, 9 ) );
		assertThat( List.alterBy.take( 3 ).from( l ), hasEqualElementsAsIn( 7, 9, 3 ) );
		assertThat( List.alterBy.take( 4 ).from( l ), hasEqualElementsAsIn( 7, 9, 3, 1 ) );
		assertThat( List.alterBy.take( 5 ).from( l ), hasEqualElementsAsIn( 7, 9, 3, 1, 5 ) );
		assertThat( List.alterBy.take( 6 ).from( l ), hasEqualElementsAsIn( 7, 9, 3, 1, 5 ) );
	}

	@Test
	public void testDropsFirst() {
		List<Integer> l = List.with.elements( 7, 9, 3, 1, 5 );
		assertThat( List.alterBy.drop( -1 ).from( l ), hasEqualElementsAsIn( 7, 9, 3, 1, 5 ) );
		assertThat( List.alterBy.drop( 0 ).from( l ), hasEqualElementsAsIn( 7, 9, 3, 1, 5 ) );
		assertThat( List.alterBy.drop( 1 ).from( l ), hasEqualElementsAsIn( 9, 3, 1, 5 ) );
		assertThat( List.alterBy.drop( 2 ).from( l ), hasEqualElementsAsIn( 3, 1, 5 ) );
		assertThat( List.alterBy.drop( 3 ).from( l ), hasEqualElementsAsIn( 1, 5 ) );
		assertThat( List.alterBy.drop( 4 ).from( l ), hasEqualElementsAsIn( 5 ) );
		assertThat( List.alterBy.drop( 5 ).from( l ), hasNoElements( Integer.class ) );
		assertThat( List.alterBy.drop( 6 ).from( l ), hasNoElements( Integer.class ) );
	}

	@Test
	public void testTakesLast() {
		List<Integer> l = List.with.elements( 7, 9, 3, 1, 5 );
		assertThat( List.alterBy.takeRight( -1 ).from( l ), hasNoElements( Integer.class ) );
		assertThat( List.alterBy.takeRight( 0 ).from( l ), hasNoElements( Integer.class ) );
		assertThat( List.alterBy.takeRight( 1 ).from( l ), hasEqualElementsAsIn( 5 ) );
		assertThat( List.alterBy.takeRight( 2 ).from( l ), hasEqualElementsAsIn( 1, 5 ) );
		assertThat( List.alterBy.takeRight( 3 ).from( l ), hasEqualElementsAsIn( 3, 1, 5 ) );
		assertThat( List.alterBy.takeRight( 4 ).from( l ), hasEqualElementsAsIn( 9, 3, 1, 5 ) );
		assertThat( List.alterBy.takeRight( 5 ).from( l ), hasEqualElementsAsIn( 7, 9, 3, 1, 5 ) );
		assertThat( List.alterBy.takeRight( 6 ).from( l ), hasEqualElementsAsIn( 7, 9, 3, 1, 5 ) );
	}

	@Test
	public void testTakeFirstDropLast() {
		List<Integer> l = List.with.elements( 1, 2, 3, 4, 5 );
		assertThat( List.alterBy.take( 3 ).dropRight( 1 ).from( l ), hasEqualElementsAsIn( 1, 2 ) );
	}

	@Test
	public void testTrims() {
		List<Integer> l = List.with.elements( 1, 2, 3, 4, 5 );
		assertThat( List.alterBy.trim( 1 ).from( l ), hasEqualElementsAsIn( 2, 3, 4 ) );
		assertThat( List.alterBy.trim( 2 ).from( l ), hasEqualElementsAsIn( 3 ) );
		assertThat( List.alterBy.trim( 3 ).from( l ), hasNoElements( Integer.class ) );
	}

	@Test
	public void testSorts() {
		List<Integer> l = List.with.elements( 4, 5, 7, 3, 1, 8 );
		assertThat( List.alterBy.sort().from( l ), hasEqualElementsAsIn( 1, 3, 4, 5, 7, 8 ) );
	}

	@Test
	public void testShuffle() {
		List<Integer> l = List.with.elements( 4, 5, 7, 3, 1, 8 );
		assertThat( List.alterBy.shuffle().from( l ),
				not( hasEqualElementsAsIn( 4, 5, 7, 3, 1, 8 ) ) );
	}

	@Test
	public void testNubs() {
		List<Integer> l = List.with.elements( 4, 5, 4, 6, 5, 7, 4 );
		assertThat( List.alterBy.nub().from( l ), hasEqualElementsAsIn( 4, 5, 6, 7 ) );
	}

	@Test
	public void testSwaps_AscendingCase() {
		List<Integer> l = List.with.elements( 1, 2, 3, 4 );
		Integer[] swap01 = { 2, 1, 3, 4 };
		assertThat( List.alterBy.swap( 0, 1 ).from( l ), hasEqualElementsAsIn( swap01 ) );
		assertThat( List.alterBy.swap( 1, 0 ).from( l ), hasEqualElementsAsIn( swap01 ) );
	}

	@Test
	public void testSwaps_UnsortedCase() {
		List<Integer> l = List.with.elements( 9, 5, 2, 8 );
		Integer[] swap01 = { 5, 9, 2, 8 };
		assertThat( List.alterBy.swap( 0, 1 ).from( l ), hasEqualElementsAsIn( swap01 ) );
		assertThat( List.alterBy.swap( 1, 0 ).from( l ), hasEqualElementsAsIn( swap01 ) );
		Integer[] swap02 = { 2, 5, 9, 8 };
		assertThat( List.alterBy.swap( 0, 2 ).from( l ), hasEqualElementsAsIn( swap02 ) );
		assertThat( List.alterBy.swap( 2, 0 ).from( l ), hasEqualElementsAsIn( swap02 ) );
		Integer[] swap03 = { 8, 5, 2, 9 };
		assertThat( List.alterBy.swap( 0, 3 ).from( l ), hasEqualElementsAsIn( swap03 ) );
		assertThat( List.alterBy.swap( 3, 0 ).from( l ), hasEqualElementsAsIn( swap03 ) );
		Integer[] swap23 = { 9, 5, 8, 2 };
		assertThat( List.alterBy.swap( 2, 3 ).from( l ), hasEqualElementsAsIn( swap23 ) );
		assertThat( List.alterBy.swap( 3, 2 ).from( l ), hasEqualElementsAsIn( swap23 ) );
	}

	@Test
	public void testSublists() {
		Integer[] larr = { 3, 6, 1, 0, 9, 5, 2, 8 };
		List<Integer> l = List.with.elements( 3, 6, 1, 0, 9, 5, 2, 8 );
		assertThat( List.alterBy.sublist( 0, l.length() ).from( l ), hasEqualElementsAsIn( larr ) );
		assertThat( List.alterBy.sublist( 1, 3 ).from( l ), hasEqualElementsAsIn( 6, 1, 0 ) );
	}

	@Test
	public void testTakeWhile() {
		Integer[] larr = { 2, 4, 7, 9, 1, 4 };
		List<Integer> l = List.with.elements( larr );
		assertThat( List.alterBy.takeWhile( Is.true_() ).from( l ), hasEqualElementsAsIn( larr ) );
		Predicate<Object> le7 = Is.<Object> le( 7 );
		assertThat( List.alterBy.takeWhile( le7 ).from( l ), hasEqualElementsAsIn( 2, 4, 7 ) );
		Predicate<Object> lt9 = Is.<Object> lt( 9 );
		assertThat( List.alterBy.takeWhile( lt9 ).from( l ), hasEqualElementsAsIn( 2, 4, 7 ) );
		Predicate<Object> gt1 = Is.<Object> gt( 1 );
		assertThat( List.alterBy.takeWhile( gt1 ).from( l ), hasEqualElementsAsIn( 2, 4, 7, 9 ) );
	}

	@Test
	public void testDropWhile() {
		Integer[] larr = { 2, 4, 7, 9, 1, 4 };
		List<Integer> l = List.with.elements( larr );
		assertThat( List.alterBy.dropWhile( Is.true_() ).from( l ), hasNoElements( Integer.class ) );
		Predicate<Object> le7 = Is.<Object> le( 7 );
		assertThat( List.alterBy.dropWhile( le7 ).from( l ), hasEqualElementsAsIn( 9, 1, 4 ) );
		Predicate<Object> lt9 = Is.<Object> lt( 9 );
		assertThat( List.alterBy.dropWhile( lt9 ).from( l ), hasEqualElementsAsIn( 9, 1, 4 ) );
		Predicate<Object> gt1 = Is.<Object> gt( 1 );
		assertThat( List.alterBy.dropWhile( gt1 ).from( l ), hasEqualElementsAsIn( 1, 4 ) );
	}

	@Test
	public void testRefinesToSet() {
		Set<Integer> s = List.alterBy.refineToSet().from( List.with.elements( 1, 2, 3, 4, 2, 5 ) );
		assertThat( s, hasEqualElementsAsIn( 1, 2, 3, 4, 5 ) );
	}
}
