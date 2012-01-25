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
	public void testInherentOrder_AscendingNumbersCase() {
		assertThat( Order.inherent.ord( 1, 2 ), is( Ordering.LT ) );
	}

	private <T> int search( T value, T[] values ) {
		return Order.binarySearch( Array.sequence( values ), 0, values.length, value,
				Order.inherent );
	}
}
