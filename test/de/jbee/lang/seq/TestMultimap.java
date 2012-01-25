package de.jbee.lang.seq;

import static de.jbee.lang.seq.ListMatcher.hasEqualElementsAsIn;
import static de.jbee.lang.seq.ListMatcher.hasNoElements;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import de.jbee.lang.Bag;
import de.jbee.lang.List;
import de.jbee.lang.Map;
import de.jbee.lang.Multimap;
import de.jbee.lang.Order;

public class TestMultimap {

	@Test
	public void testValuesFor_NoEntriesCase() {
		assertThat( emptyMap().valuesFor( "one" ), hasNoElements( Integer.class ) );
	}

	@Test
	public void testValuesFor_OneEntryCase() {
		Multimap<Integer> m = emptyMap();
		m = m.insert( "one", 1 );
		assertThat( m.valuesFor( "one" ), hasEqualElementsAsIn( 1 ) );
	}

	@Test
	public void testValuesFor_TwoEntriesCase() {
		Multimap<Integer> m = emptyMap();
		m = m.insert( "one", 1 );
		m = m.insert( "two", 2 );
		assertThat( m.valuesFor( "one" ), hasEqualElementsAsIn( 1 ) );
		assertThat( m.valuesFor( "two" ), hasEqualElementsAsIn( 2 ) );
	}

	@Test
	public void testValuesFor_TenEntriesCase() {
		Multimap<Integer> m = emptyMap();
		for ( int i = 0; i < 10; i++ ) {
			String key = "number " + i;
			m = m.insert( key, i );
			for ( int j = 0; j <= i; j++ ) {
				assertThat( m.valuesFor( key ), hasEqualElementsAsIn( i ) );
			}
		}
	}

	@Test
	public void testValuesFor_MultipleEntriesOnlyCase() {
		Multimap<Integer> m = emptyMap();
		m = m.insert( "one", 2 );
		m = m.insert( "one", 1 );
		m = m.insert( "one", 3 );
		assertThat( m.valuesFor( "one" ), hasEqualElementsAsIn( 1, 2, 3 ) );
	}

	@Test
	public void testValuesFor_MultipleEntriesAndOthersCase() {
		Multimap<Integer> m = emptyMap();
		m = m.insert( "zero", 0 );
		m = m.insert( "one", 3 );
		m = m.insert( "one", 1 );
		m = m.insert( "one", 2 );
		m = m.insert( "two", 4 );
		Bag<Integer> ones = m.valuesFor( "one" );
		assertThat( ones, hasEqualElementsAsIn( 1, 2, 3 ) );
		assertThat( m.valuesFor( "zero" ), hasEqualElementsAsIn( 0 ) );
	}

	private Multimap<Integer> emptyMap() {
		return SortedList.multimapOf( List.with.<Map.Entry<Integer>> noElements(), Map.ENTRY_ORDER,
				Order.typeaware( Order.numerical, Integer.class ) );
	}
}
