package de.jbee.lang.seq;

import static de.jbee.lang.seq.ListMatcher.hasEqualElementsAsIn;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import de.jbee.lang.List;
import de.jbee.lang.Map;

public class TestMap {

	@Test
	public void testValueFor_NoEntriesCase() {
		assertThat( emptyMap().valueFor( "one" ), nullValue() );
	}

	@Test
	public void testValueFor_OneEntryCase() {
		Map<Integer> m = emptyMap();
		m = m.insert( "one", 1 );
		assertThat( m.valueFor( "one" ), is( 1 ) );
	}

	@Test
	public void testValueFor_TwoEntriesCase() {
		Map<Integer> m = emptyMap();
		m = m.insert( "one", 1 );
		m = m.insert( "two", 2 );
		assertThat( m.valueFor( "one" ), is( 1 ) );
		assertThat( m.valueFor( "two" ), is( 2 ) );
	}

	@Test
	public void testValueFor_TenEntriesCase() {
		Map<Integer> m = emptyMap();
		for ( int i = 0; i < 10; i++ ) {
			String key = "number " + i;
			m = m.insert( key, i );
			for ( int j = 0; j <= i; j++ ) {
				assertThat( m.valueFor( key ), is( i ) );
			}
		}
	}

	@Test
	public void testValues_EntryOverridenCase() {
		Map<Integer> m = emptyMap();
		m = m.insert( "a", 1 );
		m = m.insert( "a", 2 );
		assertThat( m.length(), is( 1 ) );
		assertThat( m.values(), hasEqualElementsAsIn( 2 ) );
	}

	@Test
	public void testValues_EntryOverridenAndOthersCase() {
		Map<Integer> m = emptyMap();
		m = m.insert( "a", 1 );
		m = m.insert( "b", 3 );
		m = m.insert( "a", 2 );
		m = m.insert( "A", 0 );
		assertThat( m.length(), is( 3 ) );
		assertThat( m.values(), hasEqualElementsAsIn( 0, 2, 3 ) );
	}

	private Map<Integer> emptyMap() {
		return SortedList.mapOf( SortedList.setOf( List.with.<Map.Entry<Integer>> noElements(),
				Map.ENTRY_ORDER ) );
	}
}
