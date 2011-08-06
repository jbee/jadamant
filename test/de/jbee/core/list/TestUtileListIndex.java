package de.jbee.core.list;

import static de.jbee.core.list.ListIndex.NOT_CONTAINED;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import de.jbee.core.type.Ord;
import de.jbee.core.type.Order;

public class TestUtileListIndex {

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
		assertThat( List.indexFor.head().in( List.with.elements( 42, 3, 5 ) ), is( 0 ) );
		assertThat( List.indexFor.head().in( List.with.elements( 42, 3 ) ), is( 0 ) );
		assertThat( List.indexFor.head().in( List.with.elements( 42 ) ), is( 0 ) );
		assertThat( List.indexFor.head().in( List.with.noElements() ), is( NOT_CONTAINED ) );
	}

	@Test
	public void testDuplicateForIndexListIndex() {
		List<Integer> l = List.with.elements( 1, 2, 3, 4, 2, 5, 3, 6 );
		assertThat( List.indexFor.duplicateFor( 0 ).in( l ), is( NOT_CONTAINED ) );
		assertThat( List.indexFor.duplicateFor( 1 ).in( l ), is( 4 ) );
		assertThat( List.indexFor.duplicateFor( 2 ).in( l ), is( 6 ) );
		assertThat( List.indexFor.duplicateFor( 3 ).in( l ), is( NOT_CONTAINED ) );
		assertThat( List.indexFor.duplicateFor( 4 ).in( l ), is( NOT_CONTAINED ) );
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
		Ord<Object> ord = Order.typesave( Order.numerical, Integer.class );
		assertThat( List.indexFor.maximum().in( l ), is( 4 ) );
		assertThat( List.indexFor.maximumBy( ord ).in( l ), is( 4 ) );
	}

	@Test
	public void testMaximumByListIndexMistypedOrder() {
		List<Character> l = List.with.charactersIn( "Hello" );
		Ord<Object> ord = Order.typesave( Order.numerical, Integer.class );
		assertThat( List.indexFor.maximumBy( ord ).in( l ), is( NOT_CONTAINED ) );
		// just check that a proper order would do it:
		ord = Order.typesave( Order.abecedarian, Character.class );
		assertThat( List.indexFor.maximumBy( ord ).in( l ), is( 4 ) );
	}

	@Test
	public void testMaximumByListIndexMixed() {
		List<Object> l = List.with.<Object> elements( 1, 2, 'c', "blue", 'd', "e" );
		Ord<Object> intOrd = Order.typesave( Order.numerical, Integer.class );
		assertThat( List.indexFor.maximumBy( intOrd ).in( l ), is( 1 ) );
		Ord<Object> charOrd = Order.typesave( Order.abecedarian, Character.class );
		assertThat( List.indexFor.maximumBy( charOrd ).in( l ), is( 4 ) );
	}

	@Test
	public void testMinimumByListIndex() {
		List<Integer> l = List.with.elements( 2, 5, 1, 3, 8, 4 );
		Ord<Object> ord = Order.typesave( Order.numerical, Integer.class );
		assertThat( List.indexFor.minimum().in( l ), is( 2 ) );
		assertThat( List.indexFor.minimumBy( ord ).in( l ), is( 2 ) );
	}

	@Test
	public void testNthAfterListIndex() {
		List<Integer> l = List.with.elements( 2, 5, 1, 3, 8, 4 );
		assertThat( List.indexFor.nthAfter( 1, List.indexFor.head() ).in( l ), is( 1 ) );
		assertThat( List.indexFor.nthAfter( 2, List.indexFor.minimum() ).in( l ), is( 4 ) );
		assertThat( List.indexFor.nthAfter( 1, List.indexFor.maximum() ).in( l ), is( 5 ) );
		assertThat( List.indexFor.nthAfter( 2, List.indexFor.maximum() ).in( l ),
				is( NOT_CONTAINED ) );
	}

	@Test
	public void testNthBeforeListIndex() {
		List<Integer> l = List.with.elements( 2, 5, 1, 3, 8, 4 );
		assertThat( List.indexFor.nthBefore( 1, List.indexFor.head() ).in( l ), is( NOT_CONTAINED ) );
		assertThat( List.indexFor.nthBefore( 2, List.indexFor.minimum() ).in( l ), is( 0 ) );
		assertThat( List.indexFor.nthBefore( 1, List.indexFor.maximum() ).in( l ), is( 3 ) );
		assertThat( List.indexFor.nthBefore( 2, List.indexFor.maximum() ).in( l ), is( 2 ) );
	}
}