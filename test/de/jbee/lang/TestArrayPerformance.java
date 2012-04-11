package de.jbee.lang;

import java.util.Arrays;

import org.junit.Test;

public class TestArrayPerformance {

	private static final int FILL_PERFORMANCE_LOOPS = 1000;
	private static final int FILL_PERFORMANCE_SIZE = 100000;

	@Test
	public void testFill() {
		int size = FILL_PERFORMANCE_SIZE;
		Object[] a = new Object[size];
		for ( int i = 0; i < FILL_PERFORMANCE_LOOPS; i++ ) {
			Array.fill( a, "x", 0, size );
		}
	}

	@Test
	public void testReferenceFill() {
		int size = FILL_PERFORMANCE_SIZE;
		Object[] a = new Object[size];
		int toIndex = size - 1;
		for ( int i = 0; i < FILL_PERFORMANCE_LOOPS; i++ ) {
			Arrays.fill( a, 0, toIndex, "x" );
		}
	}
}
