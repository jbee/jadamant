package de.jbee.lang.seq;

import static de.jbee.lang.ListIndex.NOT_CONTAINED;
import static de.jbee.lang.seq.ListMatcher.hasEqualElementsAsIn;
import static de.jbee.lang.seq.Sequences.entry;
import static de.jbee.lang.seq.Sequences.key;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import de.jbee.lang.List;
import de.jbee.lang.Map;
import de.jbee.lang.Map.Key;

public class TestMap {

	private static final Key A = key( "a" );
	private static final Key B = key( "b" );
	private static final Key C = key( "c" );
	private static final Key ONE = key( "one" );
	private static final Key TWO = key( "two" );

	@Test
	public void testValueFor_NoEntriesCase() {
		assertThat( emptyMap().indexFor( ONE ), is( NOT_CONTAINED ) );
	}

	@Test
	public void testValueFor_OneEntryCase() {
		Map<Integer> m = emptyMap();
		m = m.insert( ONE, 1 );
		assertThat( m.at( m.indexFor( ONE ) ).value(), is( 1 ) );
	}

	@Test
	public void testValueFor_TwoEntriesCase() {
		Map<Integer> m = emptyMap();
		m = m.insert( ONE, 1 );
		m = m.insert( TWO, 2 );
		assertThat( m.at( m.indexFor( ONE ) ).value(), is( 1 ) );
		assertThat( m.at( m.indexFor( TWO ) ).value(), is( 2 ) );
	}

	@Test
	public void testValueFor_TenEntriesCase() {
		Map<Integer> m = emptyMap();
		for ( int i = 0; i < 10; i++ ) {
			Map.Key key = key( "number " + i );
			m = m.insert( key, i );
			for ( int j = 0; j <= i; j++ ) {
				assertThat( m.at( m.indexFor( key ) ).value(), is( i ) );
			}
		}
	}

	@Test
	public void testValues_EntryOverridenCase() {
		Map<Integer> m = emptyMap();
		m = m.insert( A, 1 );
		m = m.insert( A, 2 );
		assertThat( m.length(), is( 1 ) );
		assertThat( m.values(), hasEqualElementsAsIn( 2 ) );
	}

	@Test
	public void testValues_EntryOverridenAndOthersCase() {
		Map<Integer> m = emptyMap();
		m = m.insert( B, 1 );
		m = m.insert( C, 3 );
		m = m.insert( B, 2 );
		m = m.insert( A, 0 );
		assertThat( m.length(), is( 3 ) );
		assertThat( m.values(), hasEqualElementsAsIn( 0, 2, 3 ) );
	}

	/**
	 * This small example shows how to find the first key/entry *not* having a specific prefix as
	 * key value. Together with a 2nd index lookup you can determine the index range of the entries
	 * having the prefix. You can even slice out all keys between 'a' and 'c' or something similar.
	 */
	@Test
	public void testScenario_PrefixSearch() {
		Key aaa = key( "aabcd" );
		Key aa = key( "aacde" );
		Key az = key( "aza" );
		Key ba = key( "ba" );
		Map<Integer> m = emptyMap();
		m = m.insert( aaa, 0 );
		m = m.insert( aa, 1 );
		m = m.insert( az, 3 );
		m = m.insert( ba, 2 );
		int endExclusive = List.indexFor.insertBy(
				entry( key( "a" + Map.Key.PREFIX_TERMINATOR ), 0 ), m.order() ).in( m );
		assertThat( endExclusive, is( 3 ) );
		endExclusive = List.indexFor.insertBy( entry( key( "aa" + Map.Key.PREFIX_TERMINATOR ), 0 ),
				m.order() ).in( m );
		assertThat( endExclusive, is( 2 ) );
	}

	private Map<Integer> emptyMap() {
		return SortedList.mapOf( SortedList.setOf( List.with.<Map.Entry<Integer>> noElements(),
				Map.ENTRY_ORDER ) );
	}
}
