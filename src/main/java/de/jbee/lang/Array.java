package de.jbee.lang;

import java.util.Arrays;
import java.util.Random;

import de.jbee.lang.dev.Nonnull;
import de.jbee.lang.seq.Sequences;

/**
 * My array util.
 * 
 * TODO the name Array should be a interface for objects that represent logical arrays of something.
 * OPEN maybe have a Copy-class that is about Object[] that are copied ?
 * 
 * @author Jan Bernitt (jan.bernitt@gmx.de)
 */
public final class Array {

	private static Random rnd;

	/**
	 * @return Does a 'partial' clone of the source array. The result has the same length but
	 *         contains just the elements between start and end (exclusive) given by start +length.
	 *         All other cells of the result will be <code>null</code>.
	 */
	public static Object[] clone( Object[] src, int start, int length ) {
		Object[] clone = new Object[src.length];
		System.arraycopy( src, start, clone, start, length );
		return clone;
	}

	/**
	 * @return Copies a segment of length starting at the given start from the src array. This
	 *         segment will have a length equal to the given length. If the length given is larger
	 *         than the length of the source all its elements will be copied to the end of the
	 *         result array.
	 */
	public static Object[] segment( Object[] src, int start, int length ) {
		return segment( src, start, length, new Object[length] );
	}

	public static Object[] segment( Object[] src, int start, int length, Object[] dest ) {
		final int copyLength = Math.min( length, src.length - start );
		final int destStart = dest.length - copyLength;
		if ( destStart < 0 ) {
			// dest is shorter than the copied segment - we copy as much as possible into dest from start
			System.arraycopy( src, start, dest, 0, dest.length );
		} else {
			// copy from length elements from start into end of dest
			System.arraycopy( src, start, dest, destStart, copyLength );
		}
		return dest;
	}

	/**
	 * Fills the hole array with the <code>value</code>.
	 * 
	 * @see #fill(Object[], Object, int, int)
	 */
	public static void fill( Object[] a, Object value ) {
		fill( a, value, 0, a.length );
	}

	/**
	 * A more effective way to fill long arrays.
	 * <p>
	 * The array <code>a</code> is filled with the <code>value</code> from the <code>start</code>
	 * index up to <code>length</code> elements. If <code>start + length</code> is larger than the
	 * length of the array the array is filled up to its last element.
	 * </p>
	 * <p>
	 * The algorithm doubles the already filled cells in each step by copying all already filled to
	 * the positions after them via {@link System#arraycopy(Object, int, Object, int, int)}. Thereby
	 * the already filled part grows times 2 every loop. This is way faster (for large arrays) then
	 * to iterate (as the {@link Arrays#fill(Object[], int, int, Object)}-method is doing it).
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
		final int end = Math.min( start + length, a.length );
		while ( dest + len < end ) {
			System.arraycopy( a, src, a, dest, len );
			len += len;
			dest = src + len;
		}
		if ( dest < end ) {
			System.arraycopy( a, src, a, dest, end - dest );
		}
	}

	public static void push( int indices, Object[] a ) {
		if ( indices > 0 ) {
			System.arraycopy( a, 0, a, indices, a.length - indices );
		}
	}

	public static boolean isEmpty( Object[] a ) {
		return a == null || a.length == 0;
	}

	@SuppressWarnings ( "unchecked" )
	public static <T> T[] newInstance( Class<T> elementType, int length ) {
		return (T[]) java.lang.reflect.Array.newInstance( elementType, length );
	}

	public static <E> List<E> sequence( E e1 ) {
		Nonnull.element( e1 );
		return new ArrayList<E>( new Object[] { e1 } );
	}

	public static <E> List<E> sequence( E e1, E e2 ) {
		Nonnull.element( e1 );
		Nonnull.element( e2 );
		return new ArrayList<E>( new Object[] { e1, e2 } );
	}

	public static <E> List<E> sequence( E e1, E e2, E e3 ) {
		Nonnull.element( e1 );
		Nonnull.element( e2 );
		Nonnull.element( e3 );
		return new ArrayList<E>( new Object[] { e1, e2, e3 } );
	}

	public static <E> List<E> sequence( E[] elems ) {
		//FIXME check for null elements 
		// OPEN do a copy ? -> to be save it would be needed
		return new ArrayList<E>( elems );
	}

	public static void shuffle( Object[] a ) {
		if ( rnd == null ) {
			rnd = new Random();
		}
		for ( int i = a.length; i > 1; i-- ) {
			swap( a, i - 1, rnd.nextInt( i ) );
		}
	}

	public static <E> Object[] withLastElement( E e, int length ) {
		Object[] arr = new Object[length];
		arr[length - 1] = e;
		return arr;
	}

	private static void swap( Object[] a, int i, int j ) {
		Object tmp = a[i];
		a[i] = a[j];
		a[j] = tmp;
	}

	private Array() {
		throw new UnsupportedOperationException( "util" );
	}

	/**
	 * A simple wrapper to be able to handle arrays as {@link List} s.
	 * 
	 * @author Jan Bernitt (jan.bernitt@gmx.de)
	 */
	private static final class ArrayList<E>
			implements List<E> {

		private final Object[] elems;

		ArrayList( Object[] elems ) {
			super();
			this.elems = elems;
		}

		@SuppressWarnings ( "unchecked" )
		@Override
		public E at( int index )
				throws ArrayIndexOutOfBoundsException {
			return (E) elems[index];
		}

		@Override
		public void fill( int offset, Object[] dest, int start, int length ) {
			System.arraycopy( elems, start, dest, offset, Math.min( length, elems.length - start ) );
		}

		@Override
		public boolean isEmpty() {
			return length() == 0;
		}

		@Override
		public int length() {
			return elems.length;
		}

		@Override
		public boolean equals( Object obj ) {
			if ( obj instanceof ArrayList<?> ) {
				return Arrays.equals( elems, ( (ArrayList<?>) obj ).elems );
			}
			if ( obj instanceof Sequence<?> ) {
				return Sequences.equal( this, (Sequence<?>) obj );
			}
			return false;
		}

		@Override
		public int hashCode() {
			return Arrays.hashCode( elems );
		}

		@Override
		public String toString() {
			return Arrays.toString( elems );
		}

		@Override
		public List<E> append( E e ) {
			Nonnull.element( e );
			final int len = elems.length;
			Object[] res = new Object[len + 1];
			System.arraycopy( elems, 0, res, 0, len );
			res[len] = e;
			return arraylist( res );
		}

		@Override
		public List<E> concat( List<E> other ) {
			if ( other.isEmpty() ) {
				return this;
			}
			Object[] res = new Object[elems.length + other.length()];
			System.arraycopy( elems, 0, res, 0, elems.length );
			other.fill( elems.length, res, 0, other.length() );
			return arraylist( res );
		}

		@Override
		public List<E> deleteAt( int index ) {
			checkIndex( index );
			int len = elems.length;
			if ( len == 1 ) {
				return subsequent();
			}
			if ( index == 0 ) {
				return drop( 1 );
			}
			int shortenedBy1 = len - 1;
			if ( index == shortenedBy1 ) {
				return take( shortenedBy1 );
			}
			Object[] res = new Object[shortenedBy1];
			System.arraycopy( elems, 0, res, 0, index );
			System.arraycopy( elems, index + 1, res, index, elems.length - index );
			return arraylist( res );
		}

		@Override
		public List<E> drop( int count ) {
			if ( count <= 0 ) {
				return this;
			}
			if ( count >= elems.length ) {
				return subsequent();
			}
			Object[] res = new Object[elems.length - count];
			System.arraycopy( elems, count, res, 0, res.length );
			return arraylist( res );
		}

		@Override
		public List<E> insertAt( int index, E e ) {
			checkIndexExclusive( index );
			if ( index == 0 ) {
				return prepand( e );
			}
			if ( index == elems.length ) {
				return append( e );
			}
			Object[] res = new Object[elems.length + 1];
			System.arraycopy( elems, 0, res, 0, index );
			res[index] = e;
			System.arraycopy( elems, index, res, index + 1, elems.length - index );
			return arraylist( res );
		}

		@Override
		public List<E> prepand( E e ) {
			Nonnull.element( e );
			final int len = elems.length;
			Object[] res = new Object[len + 1];
			System.arraycopy( elems, 0, res, 1, len );
			res[0] = e;
			return arraylist( res );
		}

		@Override
		public List<E> replaceAt( int index, E e ) {
			checkIndex( index );
			Object[] res = elems.clone();
			res[index] = e;
			return arraylist( res );
		}

		private void checkIndex( int index ) {
			if ( index < 0 || index >= elems.length ) {
				throw outOfBounds( index );
			}
		}

		private void checkIndexExclusive( int index ) {
			if ( index < 0 || index > elems.length ) {
				throw outOfBounds( index );
			}
		}

		@Override
		public List<E> subsequent() {
			return List.with.noElements();
		}

		@Override
		public List<E> take( int count ) {
			if ( count <= 0 ) {
				return subsequent();
			}
			if ( count >= elems.length ) {
				return this;
			}
			Object[] res = new Object[count];
			System.arraycopy( elems, 0, res, 0, count );
			return arraylist( res );
		}

		@Override
		public List<E> tidyUp() {
			return this;
		}

		@Override
		public void traverse( int start, Traversal<? super E> traversal ) {
			int inc = 0;
			while ( inc >= 0 && start < elems.length ) {
				inc = traversal.incrementOn( at( start ) );
				start += inc;
			}
		}

		private static <E> ArrayList<E> arraylist( Object[] res ) {
			return new ArrayList<E>( res );
		}
	}

	static IndexOutOfBoundsException outOfBounds( int index ) {
		return new IndexOutOfBoundsException( "No such element: " + index );
	}
}
