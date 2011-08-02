package de.jbee.core.list;

import static de.jbee.core.list.ListElement.NOT_CONTAINED;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import de.jbee.core.type.Ord;
import de.jbee.core.type.Order;

public class TestListElement {

	@Test
	public void testAtListElement() {
		List<Integer> l = List.with.elements( 5, 6, 7 );
		assertThat( List.element.at( 2 ).indexIn( l ), is( 2 ) );
		assertThat( List.element.at( 0 ).indexIn( l ), is( 0 ) );
		assertThat( List.element.at( -1 ).indexIn( l ), is( NOT_CONTAINED ) );
		assertThat( List.element.at( -5 ).indexIn( l ), is( NOT_CONTAINED ) );
		assertThat( List.element.at( 3 ).indexIn( l ), is( NOT_CONTAINED ) );
	}

	@Test
	public void testPosListElement() {
		List<Integer> l = List.with.elements( 5, 6, 7 );
		assertThat( List.element.on( 2 ).indexIn( l ), is( 2 ) );
		assertThat( List.element.on( 0 ).indexIn( l ), is( 0 ) );
		assertThat( List.element.on( -1 ).indexIn( l ), is( 2 ) );
		assertThat( List.element.on( -5 ).indexIn( l ), is( NOT_CONTAINED ) );
		assertThat( List.element.on( 3 ).indexIn( l ), is( NOT_CONTAINED ) );
	}

	@Test
	public void testNthListElement() {
		List<Integer> l = List.with.elements( 3, 4, 3, 4, 3 );
		assertThat( List.element.nthEq( 1, 3 ).indexIn( l ), is( 0 ) );
		assertThat( List.element.nthEq( 2, 3 ).indexIn( l ), is( 2 ) );
		assertThat( List.element.nthEq( 3, 3 ).indexIn( l ), is( 4 ) );
		assertThat( List.element.nthEq( 1, 5 ).indexIn( l ), is( NOT_CONTAINED ) );
	}

	@Test
	public void testHeadListElement() {
		assertThat( List.element.head().indexIn( List.with.elements( 42, 3, 5 ) ), is( 0 ) );
		assertThat( List.element.head().indexIn( List.with.elements( 42, 3 ) ), is( 0 ) );
		assertThat( List.element.head().indexIn( List.with.elements( 42 ) ), is( 0 ) );
		assertThat( List.element.head().indexIn( List.with.noElements() ), is( NOT_CONTAINED ) );
	}

	@Test
	public void testDuplicateListElement() {
		List<Integer> l = List.with.elements( 1, 2, 3, 4, 2, 5, 3, 6 );
		assertThat( List.element.duplicate( 0 ).indexIn( l ), is( 4 ) );
		assertThat( List.element.duplicate( 2 ).indexIn( l ), is( 6 ) );
		assertThat( List.element.duplicate( 3 ).indexIn( l ), is( NOT_CONTAINED ) );
	}

	@Test
	public void testMaximumByListElement() {
		List<Integer> l = List.with.elements( 2, 5, 1, 3, 8, 4 );
		Ord<Object> ord = Order.typesave( Integer.class, Order.numerical );
		assertThat( List.element.maximumBy( ord ).indexIn( l ), is( 4 ) );
	}

	@Test
	public void testMaximumByListElementMistypedOrder() {
		List<Character> l = List.with.charactersIn( "Hello" );
		Ord<Object> ord = Order.typesave( Integer.class, Order.numerical );
		assertThat( List.element.maximumBy( ord ).indexIn( l ), is( NOT_CONTAINED ) );
		// just check that a proper order would do it:
		ord = Order.typesave( Character.class, Order.abecedarian );
		assertThat( List.element.maximumBy( ord ).indexIn( l ), is( 4 ) );
	}

	@Test
	public void testMaximumByListElementMixed() {
		List<Object> l = List.with.<Object> elements( 1, 2, 'c', "blue", 'd', "e" );
		Ord<Object> intOrd = Order.typesave( Integer.class, Order.numerical );
		assertThat( List.element.maximumBy( intOrd ).indexIn( l ), is( 1 ) );
		Ord<Object> charOrd = Order.typesave( Character.class, Order.abecedarian );
		assertThat( List.element.maximumBy( charOrd ).indexIn( l ), is( 4 ) );
	}

	@Test
	public void testMinimumByListElement() {
		List<Integer> l = List.with.elements( 2, 5, 1, 3, 8, 4 );
		Ord<Object> ord = Order.typesave( Integer.class, Order.numerical );
		assertThat( List.element.minimumBy( ord ).indexIn( l ), is( 2 ) );
	}

}
