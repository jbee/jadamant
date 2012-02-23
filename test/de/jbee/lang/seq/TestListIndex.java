package de.jbee.lang.seq;

import static de.jbee.lang.ListIndex.NOT_CONTAINED;
import static de.jbee.lang.seq.Sequences.key;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import de.jbee.lang.List;
import de.jbee.lang.Map;
import de.jbee.lang.Ord;
import de.jbee.lang.Order;
import de.jbee.lang.Set;
import de.jbee.lang.Map.Entry;

public class TestListIndex {

	@Test
	public void testAtIndexListIndex() {
		List<Integer> l = List.with.elements( 5, 6, 7 );
		assertThat( List.indexFor.elemAt( 2 ).in( l ), is( 2 ) );
		assertThat( List.indexFor.elemAt( 0 ).in( l ), is( 0 ) );
		assertThat( List.indexFor.elemAt( -1 ).in( l ), is( NOT_CONTAINED ) );
		assertThat( List.indexFor.elemAt( -5 ).in( l ), is( NOT_CONTAINED ) );
		assertThat( List.indexFor.elemAt( 3 ).in( l ), is( NOT_CONTAINED ) );
	}

	@Test
	public void testOnPosListIndex() {
		List<Integer> l = List.with.elements( 5, 6, 7 );
		assertThat( List.indexFor.elemOn( 2 ).in( l ), is( 2 ) );
		assertThat( List.indexFor.elemOn( 0 ).in( l ), is( 0 ) );
		assertThat( List.indexFor.elemOn( -1 ).in( l ), is( 2 ) );
		assertThat( List.indexFor.elemOn( -5 ).in( l ), is( NOT_CONTAINED ) );
		assertThat( List.indexFor.elemOn( 3 ).in( l ), is( NOT_CONTAINED ) );
	}

	@Test
	public void testElemListIndex() {
		List<Integer> l = List.with.elements( 5, 6, 7 );
		assertThat( List.indexFor.elem( 5 ).in( l ), is( 0 ) );
		assertThat( List.indexFor.elem( 7 ).in( l ), is( 2 ) );
		assertThat( List.indexFor.elem( 3 ).in( l ), is( NOT_CONTAINED ) );
	}

	@Test
	public void testNotElemListIndex() {
		List<Integer> l = List.with.elements( 5, 6, 7 );
		assertThat( List.indexFor.notElem( 5 ).in( l ), is( 1 ) );
		assertThat( List.indexFor.notElem( 6 ).in( l ), is( 0 ) );
	}

	@Test
	public void testNthElemListIndex() {
		List<Integer> l = List.with.elements( 3, 4, 3, 4, 3 );
		assertThat( List.indexFor.nthElem( 1, 3 ).in( l ), is( 0 ) );
		assertThat( List.indexFor.nthElem( 2, 3 ).in( l ), is( 2 ) );
		assertThat( List.indexFor.nthElem( 3, 3 ).in( l ), is( 4 ) );
		assertThat( List.indexFor.nthElem( 1, 5 ).in( l ), is( NOT_CONTAINED ) );
	}

	@Test
	public void testHeadListIndex() {
		assertThat( List.indexFor.head.in( List.with.elements( 42, 3, 5 ) ), is( 0 ) );
		assertThat( List.indexFor.head.in( List.with.elements( 42, 3 ) ), is( 0 ) );
		assertThat( List.indexFor.head.in( List.with.elements( 42 ) ), is( 0 ) );
		assertThat( List.indexFor.head.in( List.with.noElements() ), is( NOT_CONTAINED ) );
	}

	@Test
	public void testDuplicateForIndexListIndex() {
		List<Integer> l = List.with.elements( 1, 2, 3, 4, 2, 5, 3, 6 );
		assertThat( List.indexFor.duplicateOf( 0 ).in( l ), is( NOT_CONTAINED ) );
		assertThat( List.indexFor.duplicateOf( 1 ).in( l ), is( 4 ) );
		assertThat( List.indexFor.duplicateOf( 2 ).in( l ), is( 6 ) );
		assertThat( List.indexFor.duplicateOf( 3 ).in( l ), is( NOT_CONTAINED ) );
		assertThat( List.indexFor.duplicateOf( 4 ).in( l ), is( NOT_CONTAINED ) );
	}

	@Test
	public void testDuplicateFromIndexListIndex() {
		List<Integer> l = List.with.elements( 1, 2, 3, 4, 2, 5, 3, 6 );
		assertThat( List.indexFor.duplicate().in( l ), is( 4 ) );
		assertThat( List.indexFor.duplicateFrom( 0 ).in( l ), is( 4 ) );
		assertThat( List.indexFor.duplicateFrom( 2 ).in( l ), is( 6 ) );
		assertThat( List.indexFor.duplicateFrom( 3 ).in( l ), is( NOT_CONTAINED ) );
	}

	@Test
	public void testMaximumByListIndex() {
		List<Integer> l = List.with.elements( 2, 5, 1, 3, 8, 4 );
		Ord<Object> ord = Order.typeaware( Order.numerical, Integer.class );
		assertThat( List.indexFor.maximum().in( l ), is( 4 ) );
		assertThat( List.indexFor.maximumBy( ord ).in( l ), is( 4 ) );
	}

	@Test
	public void testMaximumByListIndexMistypedOrder() {
		List<Character> l = List.with.charactersIn( "Hello" );
		Ord<Object> ord = Order.typeaware( Order.numerical, Integer.class );
		assertThat( List.indexFor.maximumBy( ord ).in( l ), is( NOT_CONTAINED ) );
		// just check that a proper order would do it:
		ord = Order.typeaware( Order.abecedarian, Character.class );
		assertThat( List.indexFor.maximumBy( ord ).in( l ), is( 4 ) );
	}

	@Test
	public void testMaximumByListIndexMixed() {
		List<Object> l = List.with.<Object> elements( 1, 2, 'c', "blue", 'd', "e" );
		Ord<Object> intOrd = Order.typeaware( Order.numerical, Integer.class );
		assertThat( List.indexFor.maximumBy( intOrd ).in( l ), is( 1 ) );
		Ord<Object> charOrd = Order.typeaware( Order.abecedarian, Character.class );
		assertThat( List.indexFor.maximumBy( charOrd ).in( l ), is( 4 ) );
	}

	@Test
	public void testMinimumByListIndex() {
		List<Integer> l = List.with.elements( 2, 5, 1, 3, 8, 4 );
		Ord<Object> ord = Order.typeaware( Order.numerical, Integer.class );
		assertThat( List.indexFor.minimum().in( l ), is( 2 ) );
		assertThat( List.indexFor.minimumBy( ord ).in( l ), is( 2 ) );
	}

	@Test
	public void testNthAfterListIndex() {
		List<Integer> l = List.with.elements( 2, 5, 1, 3, 8, 4 );
		assertThat( List.indexFor.nthAfter( 1, List.indexFor.head ).in( l ), is( 1 ) );
		assertThat( List.indexFor.nthAfter( 2, List.indexFor.minimum() ).in( l ), is( 4 ) );
		assertThat( List.indexFor.nthAfter( 1, List.indexFor.maximum() ).in( l ), is( 5 ) );
		assertThat( List.indexFor.nthAfter( 2, List.indexFor.maximum() ).in( l ),
				is( NOT_CONTAINED ) );
	}

	@Test
	public void testNthBeforeListIndex() {
		List<Integer> l = List.with.elements( 2, 5, 1, 3, 8, 4 );
		assertThat( List.indexFor.nthBefore( 1, List.indexFor.head ).in( l ), is( NOT_CONTAINED ) );
		assertThat( List.indexFor.nthBefore( 2, List.indexFor.minimum() ).in( l ), is( 0 ) );
		assertThat( List.indexFor.nthBefore( 1, List.indexFor.maximum() ).in( l ), is( 3 ) );
		assertThat( List.indexFor.nthBefore( 2, List.indexFor.maximum() ).in( l ), is( 2 ) );
	}

	@Test
	public void testInsertListIndex() {
		List<Integer> l = List.with.elements( 1, 2, 3, 5, 6, 9, 12 );
		assertThat( List.indexFor.insert( 0 ).in( l ), is( 0 ) );
		assertThat( List.indexFor.insert( 1 ).in( l ), is( 0 ) );
		assertThat( List.indexFor.insert( 2 ).in( l ), is( 1 ) );
		assertThat( List.indexFor.insert( 4 ).in( l ), is( 3 ) );
		assertThat( List.indexFor.insert( 7 ).in( l ), is( 5 ) );
		assertThat( List.indexFor.insert( 13 ).in( l ), is( 7 ) );
	}

	@Test
	public void testInsertBy_PreviousErrorCase1() {
		Ord<Object> order = Order.typeaware( Order.alphabetical, CharSequence.class );
		Set<String> elems = Set.with.elements( order, List.with.elements( "deep..object",
				"deep.percent", "deep.flat.total", "deep.flat.name", "deep.flat..object" ) );
		String key = "deep.flat." + Map.Key.PREFIX_TERMINATOR;
		assertThat( List.indexFor.insertBy( key, elems.order() ).in( elems ), is( 4 ) );
	}

	@Test
	public void testInsertBy_MapPreviousErrorCase1() {
		Map<Object> m = Map.with.noEntries( Entry.ORDER );
		Class<Object> value = Object.class;
		m = m.insert( key( "deep..object" ), value );
		m = m.insert( key( "deep.percent" ), value );
		m = m.insert( key( "deep.flat.total" ), value );
		m = m.insert( key( "deep.flat.name" ), value );
		m = m.insert( key( "deep.flat..object" ), value );
		Map.Key key = key( "deep.flat." + Map.Key.PREFIX_TERMINATOR );
		Entry<Class<Object>> entry = Sequences.entry( key, value );
		assertThat( List.indexFor.insertBy( entry, m.order() ).in( m ), is( 4 ) );
	}
}
