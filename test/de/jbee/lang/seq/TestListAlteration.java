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
		assertThat( List.alterBy.init.in( l ), hasEqualElementsAsIn( 7 ) );
	}

	@Test
	public void testTail() {
		assertThat( List.alterBy.tail.in( List.with.<Integer> noElements() ),
				hasNoElements( Integer.class ) );
		assertThat( List.alterBy.tail.in( List.with.elements( 7 ) ), hasNoElements( Integer.class ) );
		assertThat( List.alterBy.tail.in( List.with.elements( 7, 9 ) ), hasEqualElementsAsIn( 9 ) );
		assertThat( List.alterBy.tail.in( List.with.elements( 7, 9, 2 ) ), hasEqualElementsAsIn( 9,
				2 ) );
	}

	@Test
	public void testDropsLast() {
		List<Integer> l = List.with.elements( 7, 9, 3, 1, 5 );
		assertThat( List.alterBy.dropLast( -1 ).in( l ), hasEqualElementsAsIn( 7, 9, 3, 1, 5 ) );
		assertThat( List.alterBy.dropLast( 0 ).in( l ), hasEqualElementsAsIn( 7, 9, 3, 1, 5 ) );
		assertThat( List.alterBy.dropLast( 1 ).in( l ), hasEqualElementsAsIn( 7, 9, 3, 1 ) );
		assertThat( List.alterBy.dropLast( 2 ).in( l ), hasEqualElementsAsIn( 7, 9, 3 ) );
		assertThat( List.alterBy.dropLast( 3 ).in( l ), hasEqualElementsAsIn( 7, 9 ) );
		assertThat( List.alterBy.dropLast( 4 ).in( l ), hasEqualElementsAsIn( 7 ) );
		assertThat( List.alterBy.dropLast( 5 ).in( l ), hasNoElements( Integer.class ) );
		assertThat( List.alterBy.dropLast( 6 ).in( l ), hasNoElements( Integer.class ) );
	}

	@Test
	public void testTakesFirst() {
		List<Integer> l = List.with.elements( 7, 9, 3, 1, 5 );
		assertThat( List.alterBy.takeFirst( -1 ).in( l ), hasNoElements( Integer.class ) );
		assertThat( List.alterBy.takeFirst( 0 ).in( l ), hasNoElements( Integer.class ) );
		assertThat( List.alterBy.takeFirst( 1 ).in( l ), hasEqualElementsAsIn( 7 ) );
		assertThat( List.alterBy.takeFirst( 2 ).in( l ), hasEqualElementsAsIn( 7, 9 ) );
		assertThat( List.alterBy.takeFirst( 3 ).in( l ), hasEqualElementsAsIn( 7, 9, 3 ) );
		assertThat( List.alterBy.takeFirst( 4 ).in( l ), hasEqualElementsAsIn( 7, 9, 3, 1 ) );
		assertThat( List.alterBy.takeFirst( 5 ).in( l ), hasEqualElementsAsIn( 7, 9, 3, 1, 5 ) );
		assertThat( List.alterBy.takeFirst( 6 ).in( l ), hasEqualElementsAsIn( 7, 9, 3, 1, 5 ) );
	}

	@Test
	public void testDropsFirst() {
		List<Integer> l = List.with.elements( 7, 9, 3, 1, 5 );
		assertThat( List.alterBy.dropFirst( -1 ).in( l ), hasEqualElementsAsIn( 7, 9, 3, 1, 5 ) );
		assertThat( List.alterBy.dropFirst( 0 ).in( l ), hasEqualElementsAsIn( 7, 9, 3, 1, 5 ) );
		assertThat( List.alterBy.dropFirst( 1 ).in( l ), hasEqualElementsAsIn( 9, 3, 1, 5 ) );
		assertThat( List.alterBy.dropFirst( 2 ).in( l ), hasEqualElementsAsIn( 3, 1, 5 ) );
		assertThat( List.alterBy.dropFirst( 3 ).in( l ), hasEqualElementsAsIn( 1, 5 ) );
		assertThat( List.alterBy.dropFirst( 4 ).in( l ), hasEqualElementsAsIn( 5 ) );
		assertThat( List.alterBy.dropFirst( 5 ).in( l ), hasNoElements( Integer.class ) );
		assertThat( List.alterBy.dropFirst( 6 ).in( l ), hasNoElements( Integer.class ) );
	}

	@Test
	public void testTakesLast() {
		List<Integer> l = List.with.elements( 7, 9, 3, 1, 5 );
		assertThat( List.alterBy.takeLast( -1 ).in( l ), hasNoElements( Integer.class ) );
		assertThat( List.alterBy.takeLast( 0 ).in( l ), hasNoElements( Integer.class ) );
		assertThat( List.alterBy.takeLast( 1 ).in( l ), hasEqualElementsAsIn( 5 ) );
		assertThat( List.alterBy.takeLast( 2 ).in( l ), hasEqualElementsAsIn( 1, 5 ) );
		assertThat( List.alterBy.takeLast( 3 ).in( l ), hasEqualElementsAsIn( 3, 1, 5 ) );
		assertThat( List.alterBy.takeLast( 4 ).in( l ), hasEqualElementsAsIn( 9, 3, 1, 5 ) );
		assertThat( List.alterBy.takeLast( 5 ).in( l ), hasEqualElementsAsIn( 7, 9, 3, 1, 5 ) );
		assertThat( List.alterBy.takeLast( 6 ).in( l ), hasEqualElementsAsIn( 7, 9, 3, 1, 5 ) );
	}

	@Test
	public void testTakeFirstDropLast() {
		List<Integer> l = List.with.elements( 1, 2, 3, 4, 5 );
		assertThat( List.alterBy.takeFirst( 3 ).dropLast( 1 ).in( l ), hasEqualElementsAsIn( 1, 2 ) );
	}

	@Test
	public void testTrims() {
		List<Integer> l = List.with.elements( 1, 2, 3, 4, 5 );
		assertThat( List.alterBy.trim( 1 ).in( l ), hasEqualElementsAsIn( 2, 3, 4 ) );
		assertThat( List.alterBy.trim( 2 ).in( l ), hasEqualElementsAsIn( 3 ) );
		assertThat( List.alterBy.trim( 3 ).in( l ), hasNoElements( Integer.class ) );
	}

	@Test
	public void testSorts() {
		List<Integer> l = List.with.elements( 4, 5, 7, 3, 1, 8 );
		assertThat( List.alterBy.sort().in( l ), hasEqualElementsAsIn( 1, 3, 4, 5, 7, 8 ) );
	}

	@Test
	public void testShuffle() {
		List<Integer> l = List.with.elements( 4, 5, 7, 3, 1, 8 );
		assertThat( List.alterBy.shuffle.in( l ), not( hasEqualElementsAsIn( 4, 5, 7, 3, 1, 8 ) ) );
	}

	@Test
	public void testNubs() {
		List<Integer> l = List.with.elements( 4, 5, 4, 6, 5, 7, 4 );
		assertThat( List.alterBy.nub().in( l ), hasEqualElementsAsIn( 4, 5, 6, 7 ) );
	}

	@Test
	public void testSwaps_AscendingCase() {
		List<Integer> l = List.with.elements( 1, 2, 3, 4 );
		Integer[] swap01 = { 2, 1, 3, 4 };
		assertThat( List.alterBy.swap( 0, 1 ).in( l ), hasEqualElementsAsIn( swap01 ) );
		assertThat( List.alterBy.swap( 1, 0 ).in( l ), hasEqualElementsAsIn( swap01 ) );
	}

	@Test
	public void testSwaps_RandomCase() {
		List<Integer> l = List.with.elements( 9, 5, 2, 8 );
		Integer[] swap01 = { 5, 9, 2, 8 };
		assertThat( List.alterBy.swap( 0, 1 ).in( l ), hasEqualElementsAsIn( swap01 ) );
		assertThat( List.alterBy.swap( 1, 0 ).in( l ), hasEqualElementsAsIn( swap01 ) );
		Integer[] swap02 = { 2, 5, 9, 8 };
		assertThat( List.alterBy.swap( 0, 2 ).in( l ), hasEqualElementsAsIn( swap02 ) );
		assertThat( List.alterBy.swap( 2, 0 ).in( l ), hasEqualElementsAsIn( swap02 ) );
		Integer[] swap03 = { 8, 5, 2, 9 };
		assertThat( List.alterBy.swap( 0, 3 ).in( l ), hasEqualElementsAsIn( swap03 ) );
		assertThat( List.alterBy.swap( 3, 0 ).in( l ), hasEqualElementsAsIn( swap03 ) );
		Integer[] swap23 = { 9, 5, 8, 2 };
		assertThat( List.alterBy.swap( 2, 3 ).in( l ), hasEqualElementsAsIn( swap23 ) );
		assertThat( List.alterBy.swap( 3, 2 ).in( l ), hasEqualElementsAsIn( swap23 ) );
	}

	@Test
	public void testSublists() {
		Integer[] larr = { 3, 6, 1, 0, 9, 5, 2, 8 };
		List<Integer> l = List.with.elements( 3, 6, 1, 0, 9, 5, 2, 8 );
		assertThat( List.alterBy.sublist( 0, l.length() ).in( l ), hasEqualElementsAsIn( larr ) );
		assertThat( List.alterBy.sublist( 1, 3 ).in( l ), hasEqualElementsAsIn( 6, 1, 0 ) );
	}

	@Test
	public void testTakeWhile() {
		Integer[] larr = { 2, 4, 7, 9, 1, 4 };
		List<Integer> l = List.with.elements( larr );
		assertThat( List.alterBy.takeWhile( Is.true_() ).in( l ), hasEqualElementsAsIn( larr ) );
		Predicate<Object> le7 = Is.<Object> le( 7 );
		assertThat( List.alterBy.takeWhile( le7 ).in( l ), hasEqualElementsAsIn( 2, 4, 7 ) );
		Predicate<Object> lt9 = Is.<Object> lt( 9 );
		assertThat( List.alterBy.takeWhile( lt9 ).in( l ), hasEqualElementsAsIn( 2, 4, 7 ) );
		Predicate<Object> gt1 = Is.<Object> gt( 1 );
		assertThat( List.alterBy.takeWhile( gt1 ).in( l ), hasEqualElementsAsIn( 2, 4, 7, 9 ) );
	}

	@Test
	public void testDropWhile() {
		Integer[] larr = { 2, 4, 7, 9, 1, 4 };
		List<Integer> l = List.with.elements( larr );
		assertThat( List.alterBy.dropWhile( Is.true_() ).in( l ), hasNoElements( Integer.class ) );
		Predicate<Object> le7 = Is.<Object> le( 7 );
		assertThat( List.alterBy.dropWhile( le7 ).in( l ), hasEqualElementsAsIn( 9, 1, 4 ) );
		Predicate<Object> lt9 = Is.<Object> lt( 9 );
		assertThat( List.alterBy.dropWhile( lt9 ).in( l ), hasEqualElementsAsIn( 9, 1, 4 ) );
		Predicate<Object> gt1 = Is.<Object> gt( 1 );
		assertThat( List.alterBy.dropWhile( gt1 ).in( l ), hasEqualElementsAsIn( 1, 4 ) );
	}

	@Test
	public void testRefinesToSet() {
		Set<Integer> s = List.alterBy.refineToSet().in( List.with.elements( 1, 2, 3, 4, 2, 5 ) );
		assertThat( s, hasEqualElementsAsIn( 1, 2, 3, 4, 5 ) );
	}
}
