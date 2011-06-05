package de.jbee.core;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import java.util.Arrays;

import org.junit.Test;

public class TestArray {

	private static final int FILL_PERFORMANCE_LOOPS = 100;
	private static final int FILL_PERFORMANCE_SIZE = 100000;

	@Test
	public void testFill() {
		int size = 500;
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
	public void testPerformanceFill() {
		int size = FILL_PERFORMANCE_SIZE;
		Object[] a = new Object[size];
		for ( int i = 0; i < FILL_PERFORMANCE_LOOPS; i++ ) {
			Array.fill( a, "x", 0, size );
		}
	}

	@Test
	public void testPerformanceReferenceFill() {
		int size = FILL_PERFORMANCE_SIZE;
		Object[] a = new Object[size];
		int toIndex = size - 1;
		for ( int i = 0; i < FILL_PERFORMANCE_LOOPS; i++ ) {
			Arrays.fill( a, 0, toIndex, "x" );
		}
	}
}
