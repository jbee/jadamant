package de.jbee.lang.seq;

import static de.jbee.lang.seq.ListMatcher.hasEqualElementsAsIn;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import de.jbee.lang.List;
import de.jbee.lang.ListIndex;
import de.jbee.lang.Set;

public class TestSet {

	@Test
	public void testToSet() {
		Set<Integer> s = Set.derive.from( List.with.elements( 1, 1, 2, 5, 3, 4, 2, 5 ) );
		assertThat( s, hasEqualElementsAsIn( 1, 2, 3, 4, 5 ) );
	}

	@Test
	public void testInsertDuplicates() {
		Set<Integer> s = Set.derive.from( List.with.elements( 1, 2, 3, 4, 5 ) );
		assertThat( s.insert( 2 ), hasEqualElementsAsIn( 1, 2, 3, 4, 5 ) );
	}

	@Test
	public void testInsertHead() {
		Set<Integer> s = Set.derive.from( List.with.elements( 1, 2, 3, 4, 5 ) );
		assertThat( s.insert( 0 ), hasEqualElementsAsIn( 0, 1, 2, 3, 4, 5 ) );
	}

	@Test
	public void testIndexForHit() {
		Set<Integer> s = Set.derive.from( List.with.elements( 5, 4, 3, 2, 1 ) );
		for ( int i = 1; i <= 5; i++ ) {
			assertThat( s.indexFor( i ), is( i - 1 ) );
		}
	}

	@Test
	public void testIndexForMiss() {
		Set<Integer> s = Set.derive.from( List.with.elements( 5, 4, 3, 2, 1 ) );
		assertThat( s.indexFor( 0 ), is( ListIndex.NOT_CONTAINED ) );
		assertThat( s.indexFor( 6 ), is( ListIndex.NOT_CONTAINED ) );
		assertThat( s.indexFor( -1 ), is( ListIndex.NOT_CONTAINED ) );
	}

	public void testIndexForEmptySet() {
		Set<Integer> s = Set.with.noElements();
		assertThat( s.indexFor( 0 ), is( ListIndex.NOT_CONTAINED ) );
	}
}
