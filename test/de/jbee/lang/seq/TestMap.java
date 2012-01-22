package de.jbee.lang.seq;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import de.jbee.lang.List;
import de.jbee.lang.Map;

public class TestMap {

	@Test
	public void testGet_NoEntriesCase() {
		assertThat( emptyMap().get( "one" ), nullValue() );
	}

	@Test
	public void testPut_OneEntryCase() {
		Map<Integer> m = emptyMap();
		m = m.put( "one", 1 );
		assertThat( m.get( "one" ), is( 1 ) );
	}

	@Test
	public void testPut_TwoEntriesCase() {
		Map<Integer> m = emptyMap();
		m = m.put( "one", 1 );
		m = m.put( "two", 2 );
		assertThat( m.get( "one" ), is( 1 ) );
		assertThat( m.get( "two" ), is( 2 ) );
	}

	@Test
	public void testPut_TenEntriesCase() {
		Map<Integer> m = emptyMap();
		for ( int i = 0; i < 10; i++ ) {
			String key = "number " + i;
			m = m.put( key, i );
			for ( int j = 0; j <= i; j++ ) {
				assertThat( m.get( key ), is( i ) );
			}
		}
	}

	private Map<Integer> emptyMap() {
		return SortedList.mapOf( SortedList.setOf( List.with.<Map.Entry<Integer>> noElements(),
				Map.ENTRY_ORDER ) );
	}
}
