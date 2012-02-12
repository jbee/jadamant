package de.jbee.lang;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class TestOrder {

	@Test
	public void testBinarySearch_NoElementMatchesCase() {
		assertThat( search( 'A', new Character[] { 'a', 'c', 'x' } ), is( -1 ) );
	}

	@Test
	public void testBinarySearch_FirstElementMatchesCase() {
		assertThat( search( 'a', new Character[] { 'a', 'c', 'x' } ), is( 0 ) );
	}

	@Test
	public void testBinarySearch_SecondElementMatchesCase() {
		assertThat( search( 'c', new Character[] { 'a', 'c', 'x' } ), is( 1 ) );
	}

	@Test
	public void testBinarySearch_LastElementMatchesCase() {
		assertThat( search( 'x', new Character[] { 'a', 'c', 'x' } ), is( 2 ) );
	}

	@Test
	public void testBinarySearch_PreviousErrorCase1() {
		Ord<Object> order = Order.typeaware( Order.alphabetical, CharSequence.class );
		Set<String> elems = Set.with.elements( order, List.with.elements( "deep..object",
				"deep.percent", "deep.flat.total", "deep.flat.name", "deep.flat..object" ) );
		assertThat( search( "deep.flat..object", elems ), is( 1 ) );
		assertThat( search( "deep.flat." + Map.Key.PREFIX_TERMINATOR, elems ), is( -5 ) );
	}

	@Test
	public void testInherentOrder_AscendingNumbersCase() {
		assertThat( Order.inherent.ord( 1, 2 ), is( Ordering.LT ) );
	}

	private <T> int search( T value, T[] values ) {
		return search( value, Array.sequence( values ), Order.inherent );
	}

	private <E, T extends Sequence<E> & Ordered> int search( E value, T values ) {
		return search( value, values, values.order() );
	}

	private <T> int search( T value, Sequence<T> values, Ord<Object> order ) {
		return Order.binarySearch( values, 0, values.length(), value, order );
	}
}
