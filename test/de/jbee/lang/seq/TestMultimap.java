package de.jbee.lang.seq;

import static de.jbee.lang.seq.ListMatcher.hasEqualElementsAsIn;
import static de.jbee.lang.seq.ListMatcher.hasNoElements;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import de.jbee.lang.List;
import de.jbee.lang.ListIndex;
import de.jbee.lang.Map;
import de.jbee.lang.Multimap;
import de.jbee.lang.Ord;
import de.jbee.lang.Order;

public class TestMultimap {

	@Test
	public void testIndexFor_NoEntriesCase() {
		Multimap<Integer> m = emptyMap();
		assertThat( m.indexFor( "one" ), is( ListIndex.NOT_CONTAINED ) );
	}

	@Test ( expected = IndexOutOfBoundsException.class )
	public void testValuesAt_NoEntriesCase() {
		emptyMap().valuesAt( 0 );
	}

	@Test
	public void testValuesAt_OneEntryCase() {
		Multimap<Integer> m = emptyMap();
		m = m.insert( "one", 1 );
		assertThat( m.valuesAt( m.indexFor( "one" ) ), hasEqualElementsAsIn( 1 ) );
	}

	@Test
	public void testValuesAt_TwoEntriesCase() {
		Multimap<Integer> m = emptyMap();
		m = m.insert( "one", 1 );
		m = m.insert( "two", 2 );
		assertThat( m.valuesAt( m.indexFor( "one" ) ), hasEqualElementsAsIn( 1 ) );
		assertThat( m.valuesAt( m.indexFor( "two" ) ), hasEqualElementsAsIn( 2 ) );
	}

	@Test
	public void testValuesAt_TenEntriesCase() {
		Multimap<Integer> m = emptyMap();
		for ( int i = 0; i < 10; i++ ) {
			String key = "number " + i;
			m = m.insert( key, i );
			for ( int j = 0; j <= i; j++ ) {
				assertThat( m.valuesAt( m.indexFor( key ) ), hasEqualElementsAsIn( i ) );
			}
		}
	}

	@Test
	public void testValuesAt_MultipleEntriesOnlyCase() {
		Multimap<Integer> m = emptyMap();
		m = m.insert( "one", 2 );
		m = m.insert( "one", 1 );
		m = m.insert( "one", 3 );
		assertThat( m.valuesAt( m.indexFor( "one" ) ), hasEqualElementsAsIn( 1, 2, 3 ) );
	}

	@Test
	public void testValuesAt_MultipleEntriesAndOthersCase() {
		Multimap<Integer> m = emptyMap();
		m = m.insert( "zero", 0 );
		m = m.insert( "one", 3 );
		m = m.insert( "one", 1 );
		m = m.insert( "two", 5 );
		m = m.insert( "one", 2 );
		m = m.insert( "two", 4 );
		assertThat( m.valuesAt( m.indexFor( "one" ) ), hasEqualElementsAsIn( 1, 2, 3 ) );
		assertThat( m.valuesAt( m.indexFor( "zero" ) ), hasEqualElementsAsIn( 0 ) );
		assertThat( m.valuesAt( m.indexFor( "two" ) ), hasEqualElementsAsIn( 4, 5 ) );
	}

	@Test
	public void testValues_Empty() {
		assertThat( emptyMap().values(), hasNoElements( Integer.class ) );
	}

	@Test
	public void testValues_OneEntry() {
		assertThat( emptyMap().insert( "a", 1 ).values(), hasEqualElementsAsIn( 1 ) );
	}

	@Test
	public void testValues_TwoEntries() {
		Multimap<Integer> m = emptyMap();
		m = m.insert( "a", 2 );
		m = m.insert( "a", 1 );
		assertThat( m.values(), hasEqualElementsAsIn( 1, 2 ) );
	}

	@Test
	public void testValues_InsertionSequenceKept() {
		Multimap<Integer> m = emptyMap( Order.keep );
		m = m.insert( "a", 5 );
		m = m.insert( "b", 8 );
		m = m.insert( "a", 3 );
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
		m = m.insert( "a", 5 );
		m = m.insert( "b", 8 );
		assertThat( m.entriesAt( 0 ).values(), hasEqualElementsAsIn( 5 ) );
		assertThat( m.entriesAt( 1 ).values(), hasEqualElementsAsIn( 8 ) );
	}

	@Test
	public void testEntriesAt_TwoEntries() {
		Multimap<Integer> m = emptyMap( Order.keep );
		m = m.insert( "a", 5 );
		m = m.insert( "b", 8 );
		m = m.insert( "a", 7 );
		assertThat( m.entriesAt( 0 ).values(), hasEqualElementsAsIn( 5, 7 ) );
		assertThat( m.entriesAt( 1 ).values(), hasEqualElementsAsIn( 5, 7 ) );
		assertThat( m.entriesAt( 2 ).values(), hasEqualElementsAsIn( 8 ) );
	}

	@Test
	public void testIndexFor() {
		Multimap<Integer> m = emptyMap();
		m = m.insert( "a", 5 );
		m = m.insert( "b", 8 );
		m = m.insert( "a", 7 );
		assertThat( m.indexFor( SortedList.entry( "a", 7 ) ), is( 1 ) );
		assertThat( m.indexFor( SortedList.entry( "a", 5 ) ), is( 0 ) );
		assertThat( m.indexFor( "a" ), is( 0 ) );
	}

	private Multimap<Integer> emptyMap() {
		return emptyMap( Order.typeaware( Order.numerical, Integer.class ) );
	}

	private Multimap<Integer> emptyMap( Ord<Object> valueOrder ) {
		return SortedList.multimapOf( List.with.<Map.Entry<Integer>> noElements(), Map.ENTRY_ORDER,
				valueOrder );
	}
}
