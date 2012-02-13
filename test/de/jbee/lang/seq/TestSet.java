package de.jbee.lang.seq;

import static de.jbee.lang.seq.ListMatcher.hasEqualElementsAsIn;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import de.jbee.lang.List;
import de.jbee.lang.Order;
import de.jbee.lang.Set;

public class TestSet {

	@Test
	public void testToSet() {
		Set<Integer> s = Set.refine.in( List.with.elements( 1, 1, 2, 5, 3, 4, 2, 5 ) );
		assertThat( s, hasEqualElementsAsIn( 1, 2, 3, 4, 5 ) );
	}

	@Test
	public void testInsert_DuplicateCase() {
		Set<Integer> s = Set.refine.in( List.with.elements( 1, 2, 3, 4, 5 ) );
		Set<Integer> s2 = s.insert( 2 );
		assertThat( s2, hasEqualElementsAsIn( 1, 2, 3, 4, 5 ) );
		assertThat( s2, sameInstance( s ) );
	}

	@Test
	public void testInsert_HeadCornerCase() {
		Set<Integer> s = Set.refine.in( List.with.elements( 1, 2, 3, 4, 5 ) );
		assertThat( s.insert( 0 ), hasEqualElementsAsIn( 0, 1, 2, 3, 4, 5 ) );
	}

	@Test
	public void testIndexFor_ExistingElementCase() {
		Set<Integer> s = Set.refine.in( List.with.elements( 5, 4, 3, 2, 1 ) );
		for ( int i = 1; i <= 5; i++ ) {
			assertThat( s.indexFor( i ), is( i - 1 ) );
		}
	}

	@Test
	public void testIndexFor_MissingElementCase() {
		Set<Integer> s = Set.refine.in( List.with.elements( 5, 4, 3, 2, 1 ) );
		assertThat( s.indexFor( 0 ), is( -1 ) );
		assertThat( s.indexFor( 6 ), is( -6 ) );
		assertThat( s.indexFor( -1 ), is( -1 ) );
	}

	@Test
	public void testIndexFor_PrefixMatchCase() {
		Set<String> s = Set.with.elements( Order.typeaware( Order.alphabetical, String.class ),
				List.with.elements( "a.b", "a.c" ) );
		assertThat( s.indexFor( "a" ), is( -1 ) );
		assertThat( s.indexFor( "a." ), is( -1 ) );
		assertThat( s.indexFor( "a.a" ), is( -1 ) );
		assertThat( s.indexFor( "a.d" ), is( -3 ) );
		assertThat( s.indexFor( "a.b" ), is( 0 ) );
		assertThat( s.indexFor( "a.c" ), is( 1 ) );
	}

	@Test
	public void testIndexFor_EmptySetCase() {
		Set<Integer> s = Set.with.noElements();
		assertThat( s.indexFor( 0 ), is( -1 ) );
	}

	@Test
	public void testEntriesAt_ExistingElementCase() {
		Set<Integer> s = Set.refine.in( List.with.elements( 5, 4, 3, 2, 1 ) );
		assertThat( s.entriesAt( 0 ), hasEqualElementsAsIn( 1 ) );
	}
}
