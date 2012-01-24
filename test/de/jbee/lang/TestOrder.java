package de.jbee.lang;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class TestOrder {

	@Test
	public void testBinarySearch_NoElementMatchesCase() {
		assertThat( search( 0, new Integer[] { 1, 2, 3 } ), is( -1 ) );
	}

	@Test
	public void testBinarySearch_FirstElementMatchesCase() {
		assertThat( search( 1, new Integer[] { 1, 2, 3 } ), is( 0 ) );
	}

	@Test
	public void testBinarySearch_SecondElementMatchesCase() {
		assertThat( search( 1, new Integer[] { 0, 1, 2, 3 } ), is( 1 ) );
	}

	private int search( Integer value, Integer[] values ) {
		return Order.binarySearch( Array.sequence( values ), 0, values.length, value,
				Order.typeaware( Order.numerical, Integer.class ) );
	}
}
