package de.jbee.core;

import java.util.Arrays;
import java.util.Random;

/**
 * My array util.
 * 
 * @author Jan Bernitt (jan.bernitt@gmx.de)
 */
public final class Array {

	private static Random rnd;

	private Array() {
		throw new UnsupportedOperationException( "util" );
	}

	public static void fill( Object[] a, Object value ) {
		fill( a, value, 0, a.length );
	}

	/**
	 * A more effective way to fill long arrays.
	 * <p>
	 * The algorithm doubles the already filled cells in each step by copying all already filled to
	 * the positions after them via {@link System#arraycopy(Object, int, Object, int, int)}. Thereby
	 * the already filled part grows times 2 every loop. This is way faster then to iterate (as the
	 * {@link Arrays#fill(Object[], int, int, Object) is doing it)}.
	 * </p>
	 */
	public static void fill( Object[] a, Object value, int start, int length ) {
		if ( length <= 0 ) {
			return;
		}
		//TODO check if a is big enough
		a[start] = value;
		if ( length == 1 ) {
			return;
		}
		a[start + 1] = value;
		if ( length == 2 ) {
			return;
		}
		int src = start;
		int len = 2;
		int dest = src + len;
		final int end = start + length;
		while ( dest + len < end ) {
			System.arraycopy( a, src, a, dest, len );
			len += len;
			dest = src + len;
		}
		if ( dest < end ) {
			System.arraycopy( a, src, a, dest, end - dest );
		}
	}

	public static void shuffle( Object[] a ) {
		if ( rnd == null ) {
			rnd = new Random();
		}
		for ( int i = a.length; i > 1; i-- ) {
			swap( a, i - 1, rnd.nextInt( i ) );
		}
	}

	private static void swap( Object[] a, int i, int j ) {
		Object tmp = a[i];
		a[i] = a[j];
		a[j] = tmp;
	}

	public static <E> Object[] withLastElement( E e, int length ) {
		Object[] stack = new Object[length];
		stack[length - 1] = e;
		return stack;
	}

}
