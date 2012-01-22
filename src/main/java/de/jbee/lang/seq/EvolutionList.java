/**
 * 
 */
package de.jbee.lang.seq;

import de.jbee.lang.Array;
import de.jbee.lang.List;
import de.jbee.lang.Traversal;
import de.jbee.lang.dev.Nonnull;

/**
 * The data-structure of the {@link EvolutionList} consists of a chain of partial lists (stored in
 * an array {@link #elems}). The capacity of each partial list is growing with the power of 2
 * starting with 1. If the capacity is exceeded a new partial list is created having the current
 * list as its {@link #tail} list. The created list has twice the capacity of the last partial list.
 * Through this the majority (>= 50%) of elements are always contained in the first or second
 * partial list. A average random access therefore is just a array index access.
 * 
 * <p>
 * This idea is based on the <a
 * href="http://infoscience.epfl.ch/record/64410/files/techlists.pdf">V-List data-structure by Phil
 * Bagwell</a>
 * </p>
 * 
 * @author Jan Bernitt (jan.bernitt@gmx.de)
 */
abstract class EvolutionList<E>
		implements List<E> {

	static <E> List<E> dominant( int size, Object[] elements ) {
		return dominant( size, elements, List.with.<E> noElements() );
	}

	static <E> List<E> dominant( int size, Object[] elements, List<E> tail ) {
		return new DominantEvolutionList<E>( size, elements, tail );
	}

	static <E> List<E> recessive( int size, int offset, Object[] elements, List<E> tail ) {
		return new RecessiveEvolutionList<E>( size, offset, elements, tail );
	}

	static <E> List<E> recessive( int size, Object[] elements, List<E> tail ) {
		return recessive( size, 0, elements, tail );
	}

	/**
	 * list's size in total with {@link #tail}-list elements
	 */
	final int length;
	/**
	 * Is filled with elements from highest to lowest index using {@link #prepand(Object)}.
	 */
	final Object[] elems;
	final List<E> tail;

	EvolutionList( int size, Object[] elements, List<E> tail ) {
		super();
		this.length = size;
		this.elems = elements;
		this.tail = tail;
	}

	@Override
	public List<E> subsequent() {
		return tail;
	}

	@Override
	public void traverse( int start, Traversal<? super E> traversal ) {
		final int l = elemsLength();
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
		final int l = elemsLength();
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
		final int l = elemsLength();
		if ( index == 0 ) { // first of this elems
			return l == 1
				? tail
				: recessive( length - 1, elems, tail );
		}
		if ( index >= l ) { // not in this elems
			return thisWith( length - 1, tail.deleteAt( index - l ) );
		}
		if ( index == l - 1 ) { // last of this elems
			return recessive( length - 1, 1, elems, tail );
		}
		// somewhere in between our elems ;(
		return recessive( length - 1, l - index, elems, recessive( length - 1 - index, elems, tail ) );
	}

	@Override
	public List<E> drop( int count ) {
		if ( count <= 0 ) {
			return this;
		}
		if ( count >= length ) {
			return empty();
		}
		final int l = elemsLength();
		return count >= l
			? tail.drop( count - l )
			: recessive( length - count, elems, tail );
	}

	@Override
	public void fill( int offset, Object[] array, int start, int len ) {
		final int l = elemsLength();
		if ( start < l ) {
			int srcPos = elems.length - l - offset() + start;
			int copyLength = Math.min( len, l );
			System.arraycopy( elems, srcPos, array, offset, copyLength );
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
		final int l = elemsLength();
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
		final int l = elemsLength();
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
		final int l = elemsLength();
		if ( count == l ) { // took this elems without tail
			return thisWith( l, empty() );
		}
		if ( count < l ) { // took parts of this elems
			return recessive( count, ( l - count ) + offset(), elems, empty() );
		}
		// took this hole elems and parts of the tails elements
		return thisWith( count, tail.take( count - l ) );
	}

	@Override
	public String toString() {
		StringBuilder b = new StringBuilder();
		final int l = elemsLength();
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
	 * @return The count of {@link #elems} that are attributed to this lists.
	 */
	final int elemsLength() {
		return length - tail.length();
	}

	abstract int offset();

	/**
	 * keeps tidy or untidy since the change is in the tail. just replace the tail and reduce
	 * overall size by one. The elements and length of this elems are reused as before.
	 */
	abstract List<E> thisWith( int size, List<E> tail );

	/**
	 * The dominant {@link EvolutionList} tries to continue to use (fill) the element array
	 * {@link EvolutionList#elems} referred by a list.
	 * 
	 * <h3>Tidiness</h3>
	 * <p>
	 * In case the {@link #prepandIndex(int)} is unused (<code>null</code>) this list can consider
	 * itself to be tidy and it is not necessary to {@link #tidyUp()}. In this state a second
	 * reference to the same list might occupy that index whereby the list becomes untidy as soon as
	 * a element is prepanded.
	 * </p>
	 * 
	 * @author Jan Bernitt (jan.bernitt@gmx.de)
	 */
	static final class DominantEvolutionList<E>
			extends EvolutionList<E> {

		DominantEvolutionList( int size, Object[] elements, List<E> tail ) {
			super( size, elements, tail );
		}

		public List<E> prepand( E e ) {
			Nonnull.element( e );
			final int l = elemsLength();
			int index = prepandIndex( l );
			if ( index < 0 ) { // elems capacity exceeded
				return grow1( Array.withLastElement( e, elems.length * 2 ), this );
			}
			if ( prepandedOccupying( e, index ) ) {
				return grow1( elems, tail );
			}
			if ( l > elems.length / 2 ) {
				return grow1( Array.withLastElement( e, elems.length * 2 ), recessive( length,
						elems, tail ) );
			}
			Object[] copy = Array.copy( elems, index + 1, l );
			copy[index] = e;
			return grow1( copy, tail );
		}

		@Override
		public List<E> tidyUp() {
			List<E> tidyTail = tail.tidyUp();
			int l = elemsLength();
			synchronized ( elems ) {
				if ( notOccupied( prepandIndex( l ) ) ) {
					return tidyTail == tail
						? this
						: dominant( length, elems, tidyTail );
				}

			}
			return dominant( length, Array.copy( elems, 0, l ), tail );
		}

		@SuppressWarnings ( "unchecked" )
		@Override
		final E element( int index, int l ) {
			return (E) elems[elems.length - l + index];
		}

		@Override
		final int offset() {
			return 0;
		}

		@Override
		final List<E> thisWith( int size, List<E> tail ) {
			return dominant( size, elems, tail );
		}

		private List<E> grow1( Object[] elems, List<E> tail ) {
			return dominant( length + 1, elems, tail );
		}

		private boolean notOccupied( int index ) {
			return elems[index] == null;
		}

		private int prepandIndex( final int len ) {
			return elems.length - 1 - len;
		}

		private boolean prepandedOccupying( E e, int index ) {
			synchronized ( elems ) {
				if ( notOccupied( index ) ) {
					elems[index] = e;
					return true;
				}
				return false;
			}
		}

	}

	/**
	 * A recessive {@link EvolutionList} already knew that it cannot occupy further elements from
	 * the {@link EvolutionList#elems}. It is a *view* to a section of those elements.
	 * 
	 * <h3>Tidiness</h3>
	 * <p>
	 * This list is always untidy. To {@link #tidyUp()} it is necessary to copy the section of
	 * elements contained in a lists view.
	 * </p>
	 * 
	 * @author Jan Bernitt (jan.bernitt@gmx.de)
	 * 
	 */
	static final class RecessiveEvolutionList<E>
			extends EvolutionList<E> {

		private final int offset;

		RecessiveEvolutionList( int size, int offset, Object[] elems, List<E> tail ) {
			super( size, elems, tail );
			this.offset = offset;
		}

		@Override
		public List<E> prepand( E e ) {
			return dominant( length + 1, Array.withLastElement( e, elems.length * 2 ), this );
		}

		@Override
		public List<E> tidyUp() {
			Object[] s = new Object[elems.length];
			final int l = elemsLength();
			System.arraycopy( elems, elems.length - l - offset, s, elems.length - l, l );
			return dominant( length, s, tail.tidyUp() );
		}

		@SuppressWarnings ( "unchecked" )
		@Override
		E element( int index, int l ) {
			return (E) elems[elems.length - l + index - offset];
		}

		@Override
		final int offset() {
			return offset;
		}

		@Override
		List<E> thisWith( int size, List<E> tail ) {
			return recessive( size, offset, elems, tail );
		}

	}

}