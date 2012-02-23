package de.jbee.lang.seq;

import static de.jbee.lang.ListIndex.NOT_CONTAINED;
import static de.jbee.lang.seq.ListMatcher.hasEqualElementsAsIn;
import static de.jbee.lang.seq.ListMatcher.hasNoElements;
import static de.jbee.lang.seq.Sequences.entry;
import static de.jbee.lang.seq.Sequences.key;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import de.jbee.lang.List;
import de.jbee.lang.Map;
import de.jbee.lang.Multimap;
import de.jbee.lang.Ord;
import de.jbee.lang.Order;
import de.jbee.lang.Map.Entry;
import de.jbee.lang.Map.Key;

public class TestMultimap {

	private static final Key A = key( "a" );
	private static final Key B = key( "b" );
	private static final Key C = key( "c" );
	private static final Key D = key( "d" );
	private static final Key E = key( "e" );
	private static final Key F = key( "f" );
	private static final Key G = key( "g" );
	private static final Key ZERO = key( "zero" );
	private static final Key ONE = key( "one" );
	private static final Key TWO = key( "two" );

	@Test
	public void testIndexFor_NoEntriesCase() {
		Multimap<Integer> m = emptyMap();
		assertThat( m.indexFor( ONE ), is( NOT_CONTAINED ) );
	}

	@Test ( expected = IndexOutOfBoundsException.class )
	public void testValuesAt_NoEntriesCase() {
		emptyMap().valuesAt( 0 );
	}

	@Test
	public void testValuesAt_OneEntryCase() {
		Multimap<Integer> m = emptyMap();
		m = m.insert( ONE, 1 );
		assertThat( m.valuesAt( m.indexFor( ONE ) ), hasEqualElementsAsIn( 1 ) );
	}

	@Test
	public void testValuesAt_TwoEntriesCase() {
		Multimap<Integer> m = emptyMap();
		m = m.insert( ONE, 1 );
		m = m.insert( TWO, 2 );
		assertThat( m.valuesAt( m.indexFor( ONE ) ), hasEqualElementsAsIn( 1 ) );
		assertThat( m.valuesAt( m.indexFor( TWO ) ), hasEqualElementsAsIn( 2 ) );
	}

	@Test
	public void testValuesAt_TenEntriesCase() {
		Multimap<Integer> m = emptyMap();
		for ( int i = 0; i < 10; i++ ) {
			Map.Key key = key( "number " + i );
			m = m.insert( key, i );
			for ( int j = 0; j <= i; j++ ) {
				assertThat( m.valuesAt( m.indexFor( key ) ), hasEqualElementsAsIn( i ) );
			}
		}
	}

	@Test
	public void testValuesAt_MultipleEntriesOnlyCase() {
		Multimap<Integer> m = emptyMap();
		m = m.insert( ONE, 2 );
		m = m.insert( ONE, 1 );
		m = m.insert( ONE, 3 );
		assertThat( m.valuesAt( m.indexFor( ONE ) ), hasEqualElementsAsIn( 1, 2, 3 ) );
	}

	@Test
	public void testValuesAt_MultipleEntriesAndOthersCase() {
		Multimap<Integer> m = emptyMap();
		m = m.insert( ZERO, 0 );
		m = m.insert( ONE, 3 );
		m = m.insert( ONE, 1 );
		m = m.insert( TWO, 5 );
		m = m.insert( ONE, 2 );
		m = m.insert( TWO, 4 );
		assertThat( m.valuesAt( m.indexFor( ONE ) ), hasEqualElementsAsIn( 1, 2, 3 ) );
		assertThat( m.valuesAt( m.indexFor( ZERO ) ), hasEqualElementsAsIn( 0 ) );
		assertThat( m.valuesAt( m.indexFor( TWO ) ), hasEqualElementsAsIn( 4, 5 ) );
	}

	@Test
	public void testValues_Empty() {
		assertThat( emptyMap().values(), hasNoElements( Integer.class ) );
	}

	@Test
	public void testValues_OneEntry() {
		assertThat( emptyMap().insert( A, 1 ).values(), hasEqualElementsAsIn( 1 ) );
	}

	@Test
	public void testValues_TwoEntries() {
		Multimap<Integer> m = emptyMap();
		m = m.insert( A, 2 );
		m = m.insert( A, 1 );
		assertThat( m.values(), hasEqualElementsAsIn( 1, 2 ) );
	}

	@Test
	public void testValues_InsertionSequenceKept() {
		Multimap<Integer> m = emptyMap( Order.keep );
		m = m.insert( A, 5 );
		m = m.insert( B, 8 );
		m = m.insert( A, 3 );
		assertThat( m.values(), hasEqualElementsAsIn( 5, 3, 8 ) );
		assertThat( m.values().order(), sameInstance( Order.keep ) );
	}

	@Test
	public void testEntriesAt_NoEntries() {
		assertThat( emptyMap().entriesAt( 0 ).length(), is( 0 ) );
	}

	@Test
	public void testEntriesAt_OneEntry() {
		Multimap<Integer> m = emptyMap( Order.keep );
		m = m.insert( A, 5 );
		m = m.insert( B, 8 );
		assertThat( m.entriesAt( 0 ).values(), hasEqualElementsAsIn( 5 ) );
		assertThat( m.entriesAt( 1 ).values(), hasEqualElementsAsIn( 8 ) );
	}

	@Test
	public void testEntriesAt_TwoEntries() {
		Multimap<Integer> m = emptyMap( Order.keep );
		m = m.insert( A, 5 );
		m = m.insert( B, 8 );
		m = m.insert( A, 7 );
		assertThat( m.entriesAt( 0 ).values(), hasEqualElementsAsIn( 5, 7 ) );
		assertThat( m.entriesAt( 1 ).values(), hasEqualElementsAsIn( 5, 7 ) );
		assertThat( m.entriesAt( 2 ).values(), hasEqualElementsAsIn( 8 ) );
	}

	@Test
	public void testIndexFor() {
		Multimap<Integer> m = emptyMap();
		m = m.insert( A, 5 );
		m = m.insert( B, 8 );
		m = m.insert( A, 7 );
		assertThat( m.indexFor( entry( A, 7 ) ), is( 1 ) );
		assertThat( m.indexFor( entry( A, 5 ) ), is( 0 ) );
		assertThat( m.indexFor( A ), is( 0 ) );
	}

	@Test
	public void testIndexFor_InsertPosition() {
		Multimap<Integer> m = emptyMap();
		m = m.insert( A, 5 );
		m = m.insert( C, 8 );
		m = m.insert( A, 7 );
		assertThat( m.indexFor( entry( B, 6 ) ), is( -3 ) );
	}

	@Test
	public void testIndexForStartEnd_CornerCases() {
		Multimap<Integer> m = emptyMap();
		m = m.insert( A, 0 );
		m = m.insert( B, 1 );
		m = m.insert( C, 2 );
		m = m.insert( C, 22 );
		m = m.insert( D, 3 );
		m = m.insert( E, 4 );
		m = m.insert( F, 5 );
		assertThat( m.indexFor( D, 0, 4 ), is( -5 ) );
		assertThat( m.indexFor( D, 0, 5 ), is( 4 ) );
		assertThat( m.indexFor( D, 4, 5 ), is( 4 ) );
		assertThat( m.indexFor( D, 5, 10 ), is( -6 ) );
		assertThat( m.indexFor( D, 0, 8 ), is( 4 ) );
		assertThat( m.indexFor( D, -1, 8 ), is( 4 ) );
	}

	private Multimap<Integer> emptyMap() {
		return emptyMap( Order.typeaware( Order.numerical, Integer.class ) );
	}

	private Multimap<Integer> emptyMap( Ord<Object> valueOrder ) {
		return OrderedList.multimapOf( List.with.<Map.Entry<Integer>> noElements(),
				Entry.ORDER, valueOrder );
	}
}
