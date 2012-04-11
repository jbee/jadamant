package de.jbee.lang;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class TestArray {

	@Test
	public void testFill() {
		int size = 160;
		for ( int s = 0; s < size; s++ ) {
			for ( int len = 1; s + len <= size; len++ ) {
				Object[] a = new Object[size];
				Array.fill( a, "x", s, len );
				for ( int j = 0; j < s; j++ ) {
					assertThat( a[j], nullValue() );
				}
				for ( int j = s; j < s + len; j++ ) {
					assertThat( a[j], notNullValue() );
				}
				for ( int j = s + len; j < size; j++ ) {
					assertThat( a[j], nullValue() );
				}
			}
		}
	}

	@Test
	public void testSegment() {
		Object[] a = { 3, 4 };
		assertThat( Array.segment( a, 0, 0 ), is( new Object[] {} ) );
		assertThat( Array.segment( a, 0, 1 ), is( new Object[] { 3 } ) );
		assertThat( Array.segment( a, 0, 2 ), is( new Object[] { 3, 4 } ) );
		assertThat( Array.segment( a, 0, 3 ), is( new Object[] { null, 3, 4 } ) );
		assertThat( Array.segment( a, 0, 4 ), is( new Object[] { null, null, 3, 4 } ) );
	}

}
