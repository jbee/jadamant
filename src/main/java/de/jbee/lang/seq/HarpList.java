/**
 * 
 */
package de.jbee.lang.seq;

import de.jbee.lang.Array;
import de.jbee.lang.List;
import de.jbee.lang.Traversal;
import de.jbee.lang.dev.Nonnull;

/**
 * The data-structure of the harp-list consists of a chain of partial lists (strings). The capacity
 * of each partial list is growing with the power of 2 starting with 1. If the capacity is exceeded
 * a new partial list is created having the current list as its tail list. The created list has
 * twice the capacity of the last partial list. Thereby the longest partial list is always first.
 * Through this the majority (>= 50%) of elements are always contained in the first or second string
 * (partial list). A average random access therefore is just a array index access.
 * 
 * <p>
 * This idea is based on the <a
 * href="http://infoscience.epfl.ch/record/64410/files/techlists.pdf">V-List data-structure by Phil
 * Bagwell</a>
 * </p>
 * 
 * @author Jan Bernitt (jan.bernitt@gmx.de)
 */
abstract class HarpList<E>
		implements List<E> {

	static <E> List<E> tidy( int size, Object[] string, List<E> tail ) {
		return new TidyHarpList<E>( size, string, tail );
	}

	static <E> List<E> untidy( int size, int offset, Object[] string, List<E> tail ) {
		return new UntidyHarpList<E>( size, offset, string, tail );
	}

	static <E> List<E> untidy( int size, Object[] string, List<E> tail ) {
		return untidy( size, 0, string, tail );
	}

	/**
	 * list's size in total with tail-list elements
	 */
	final int length;
	final Object[] string;
	final List<E> tail;

	HarpList( int size, Object[] string, List<E> tail ) {
		super();
		this.length = size;
		this.string = string;
		this.tail = tail;
	}

	@Override
	public void traverse( int start, Traversal<? super E> traversal ) {
		final int l = strlen();
		int i = start;
		int inc = 0;
		while ( inc >= 0 && i < l ) {
			inc = traversal.incrementOn( element( i, l ) );
			i += inc;
		}
		if ( inc >= 0 ) {
			tail.traverse( i - l, traversal );
		}
	}

	@Override
	public List<E> append( E e ) {
		//TODO improve - this works but covers no optimization case
		return thisWith( length + 1, tail.append( e ) );
	}

	@Override
	public final E at( int index ) {
		final int l = strlen();
		return index >= l
			? tail.at( index - l )
			: element( index, l );
	}

	@Override
	public List<E> concat( List<E> other ) {
		return thisWith( length + other.length(), tail.concat( other ) );
	}

	@Override
	public List<E> deleteAt( int index ) {
		if ( index < 0 ) {
			return this;
		}
		final int l = strlen();
		if ( index == 0 ) { // first of this string
			return l == 1
				? tail
				: untidy( length - 1, string, tail );
		}
		if ( index >= l ) { // not in this string
			return thisWith( length - 1, tail.deleteAt( index - l ) );
		}
		if ( index == l - 1 ) { // last of this string
			return untidy( length - 1, 1, string, tail );
		}
		// somewhere in between our string ;(
		return untidy( length - 1, l - index, string, untidy( length - 1 - index, string, tail ) );
	}

	@Override
	public List<E> drop( int count ) {
		if ( count <= 0 ) {
			return this;
		}
		if ( count >= length ) {
			return empty();
		}
		final int l = strlen();
		return count >= l
			? tail.drop( count - l )
			: untidy( length - count, string, tail );
	}

	@Override
	public void fill( int offset, Object[] array, int start, int len ) {
		final int l = strlen();
		if ( start < l ) {
			int srcPos = string.length - l - offset() + start;
			int copyLength = Math.min( len, l );
			System.arraycopy( string, srcPos, array, offset, copyLength );
			if ( start + len > l ) {
				tail.fill( offset + copyLength, array, 0, len - copyLength );
			}
		} else {
			tail.fill( offset, array, start - l, len );
		}
	}

	@Override
	public List<E> insertAt( int index, E e ) {
		if ( index == 0 ) {
			return prepand( e );
		}
		final int l = strlen();
		if ( index >= l ) {
			return thisWith( length + 1, tail.insertAt( index - l, e ) );
		}
		if ( index == l - 1 ) {
			return thisWith( length + 1, tail.prepand( e ) );
		}
		return take( index - 1 ).concat( drop( index + 1 ).prepand( e ) );
	}

	@Override
	public final boolean isEmpty() {
		return false; // otherwise you would have another class 
	}

	@Override
	public List<E> replaceAt( int index, E e ) {
		final int l = strlen();
		if ( index >= l ) {
			return thisWith( length, tail.replaceAt( index - l, e ) );
		}
		Nonnull.element( e );
		if ( index == 0 ) {
			return drop( 1 ).prepand( e );
		}
		if ( index == l - 1 ) {
			return take( l - 1 ).concat( tail.prepand( e ) );
		}
		return take( index - 1 ).concat( drop( index + 1 ).prepand( e ) );
	}

	@Override
	public final int length() {
		return length;
	}

	@Override
	public final List<E> take( int count ) {
		if ( count <= 0 ) {
			return empty();
		}
		if ( count >= length ) {
			return this;
		}
		final int l = strlen();
		if ( count == l ) { // took this string without tail
			return thisWith( l, empty() );
		}
		if ( count < l ) { // took parts of this string
			return untidy( count, ( l - count ) + offset(), string, empty() );
		}
		// took this hole string and parts of the tails elements
		return thisWith( count, tail.take( count - l ) );
	}

	@Override
	public String toString() {
		StringBuilder b = new StringBuilder();
		final int l = strlen();
		for ( int i = 0; i < l; i++ ) {
			b.append( ',' );
			b.append( String.valueOf( at( i ) ) );
		}
		return "[" + b.substring( 1 ) + "]" + List.CONCAT_OPERATOR_SYMBOL + tail.toString();
	}

	abstract E element( int index, int l );

	final List<E> empty() {
		return List.with.noElements();
	}

	/**
	 * @return The amount of elements dedicated to this lists string
	 */
	final int strlen() {
		return length - tail.length();
	}

	abstract int offset();

	/**
	 * keeps tidy or untidy since the change is in the tail. just replace the tail and reduce
	 * overall size by one. The elements and length of this string are reused as before.
	 */
	abstract List<E> thisWith( int size, List<E> tail );

	static final class TidyHarpList<E>
			extends HarpList<E> {

		TidyHarpList( int size, Object[] string, List<E> tail ) {
			super( size, string, tail );
		}

		public List<E> prepand( E e ) {
			Nonnull.element( e );
			final int l = strlen();
			int index = occupationIndex( l );
			if ( index < 0 ) { // string capacity exceeded
				return grow1( Array.withLastElement( e, string.length * 2 ), this );
			}
			if ( prepandedOccupying( e, index ) ) {
				return grow1( string, tail );
			}
			if ( l > string.length / 2 ) {
				return grow1( Array.withLastElement( e, string.length * 2 ), untidy( length,
						string, tail ) );
			}
			Object[] copy = Array.copy( string, index + 1, l );
			copy[index] = e;
			return grow1( copy, tail );
		}

		@Override
		public List<E> tidyUp() {
			List<E> tidyTail = tail.tidyUp();
			int l = strlen();
			synchronized ( string ) {
				if ( notOccupied( occupationIndex( l ) ) ) {
					return tidyTail == tail
						? this
						: tidy( length, string, tidyTail );
				}

			}
			return tidy( length, Array.copy( string, 0, l ), tail );
		}

		@SuppressWarnings ( "unchecked" )
		@Override
		final E element( int index, int l ) {
			return (E) string[string.length - l + index];
		}

		@Override
		final int offset() {
			return 0;
		}

		@Override
		final List<E> thisWith( int size, List<E> tail ) {
			return tidy( size, string, tail );
		}

		private List<E> grow1( Object[] string, List<E> tail ) {
			return tidy( length + 1, string, tail );
		}

		private boolean notOccupied( int index ) {
			return string[index] == null;
		}

		private int occupationIndex( final int len ) {
			return string.length - 1 - len;
		}

		private boolean prepandedOccupying( E e, int index ) {
			synchronized ( string ) {
				if ( notOccupied( index ) ) {
					string[index] = e;
					return true;
				}
				return false;
			}
		}

	}

	static final class UntidyHarpList<E>
			extends HarpList<E> {

		private final int offset;

		UntidyHarpList( int size, int offset, Object[] string, List<E> tail ) {
			super( size, string, tail );
			this.offset = offset;
		}

		@Override
		public List<E> prepand( E e ) {
			return tidy( length + 1, Array.withLastElement( e, string.length * 2 ), this );
		}

		@Override
		public List<E> tidyUp() {
			Object[] s = new Object[string.length];
			final int l = strlen();
			System.arraycopy( string, string.length - l - offset, s, string.length - l, l );
			return tidy( length, s, tail.tidyUp() );
		}

		@SuppressWarnings ( "unchecked" )
		@Override
		E element( int index, int l ) {
			return (E) string[string.length - l + index - offset];
		}

		@Override
		final int offset() {
			return offset;
		}

		@Override
		List<E> thisWith( int size, List<E> tail ) {
			return untidy( size, offset, string, tail );
		}

	}

}