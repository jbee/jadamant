/**
 * 
 */
package de.jbee.lang.seq;

import static java.lang.Math.min;
import de.jbee.lang.Array;
import de.jbee.lang.List;
import de.jbee.lang.Sequence;
import de.jbee.lang.Traversal;
import de.jbee.lang.dev.Nonnull;

/**
 * The data-structure of the {@link EVolutionList} consists of a chain of partial lists (stored in
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
abstract class EVolutionList<E>
		implements List<E> {

	static <E> List<E> dominant( int size, Object[] elements ) {
		return dominant( size, elements, List.with.<E> noElements() );
	}

	static <E> List<E> dominant( int size, Object[] elements, List<E> tail ) {
		return new DominantEVolutionList<E>( size, elements, tail );
	}

	static <E> List<E> recessive( int size, int offset, Object[] elements, List<E> tail ) {
		return new RecessiveEVolutionList<E>( size, offset, elements, tail );
	}

	static <E> List<E> recessive( int size, Object[] elements, List<E> tail ) {
		return recessive( size, 0, elements, tail );
	}

	//TODO a variant that has a max len value and grows with powers of 2 till that until a new part in prepanded- it always copies on growth and also when its not tidy 

	/**
	 * list's size in total with {@link #tail}-list elements
	 */
	final int length;
	/**
	 * Is filled with elements from highest to lowest index using {@link #prepand(Object)}.
	 */
	final Object[] elems;
	final List<E> tail;

	EVolutionList( int size, Object[] elements, List<E> tail ) {
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
		final int len = elemsLength();
		int i = start;
		int inc = 0;
		while ( inc >= 0 && i < len ) {
			inc = traversal.incrementOn( element( i, len ) );
			i += inc;
		}
		if ( inc >= 0 ) {
			tail.traverse( i - len, traversal );
		}
	}

	@Override
	public List<E> append( E e ) {
		//TODO improve - this works but covers no optimization case
		return thisWith( length + 1, tail.append( e ) );
	}

	@Override
	public final E at( int index ) {
		final int len = elemsLength();
		return index >= len
			? tail.at( index - len )
			: element( index, len );
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
		final int len = elemsLength();
		if ( index == 0 ) { // first of this elems
			return len == 1
				? tail
				: recessive( length - 1, offset(), elems, tail );
		}
		if ( index >= len ) { // not in this elems
			return thisWith( length - 1, tail.deleteAt( index - len ) );
		}
		if ( index == len - 1 ) { // last of this elems
			return recessive( length - 1, offset() + 1, elems, tail );
		}
		// somewhere in between our elems ;(
		return recessive( length - 1, len - index, elems, recessive( length - 1 - index, offset(),
				elems, tail ) );
	}

	@Override
	public List<E> drop( int count ) {
		if ( count <= 0 ) {
			return this;
		}
		if ( count >= length ) {
			return empty();
		}
		final int len = elemsLength();
		return count >= len
			? tail.drop( count - len )
			: recessive( length - count, offset(), elems, tail );
	}

	@Override
	public void fill( int offset, Object[] array, int start, int upTolen ) {
		final int len = elemsLength();
		if ( start < len ) {
			int srcPos = elems.length - len - offset() + start;
			int copyedLength = min( upTolen, len );
			System.arraycopy( elems, srcPos, array, offset, copyedLength );
			if ( start + upTolen > len ) {
				tail.fill( offset + copyedLength, array, 0, upTolen - copyedLength );
			}
		} else {
			tail.fill( offset, array, start - len, upTolen );
		}
	}

	@Override
	public List<E> insertAt( int index, E e ) {
		if ( index == 0 ) {
			return prepand( e );
		}
		final int len = elemsLength();
		if ( index == 1 ) { // avoid 'views' just using a single element (as first)
			return List.with.element( e ).prepand( at( 0 ) ).concat( drop( index ) );
		}
		//TODO try to avoid inappropriate reuse of this stack -> especially for the recessive list
		if ( index >= len ) {
			// TODO this might end up appending single elements all the time - check that - prevent it
			return thisWith( length + 1, tail.insertAt( index - len, e ) );
		}
		return take( index ).concat( drop( index ).prepand( e ) );
	}

	@Override
	public final boolean isEmpty() {
		return false; // otherwise it would be another class 
	}

	@Override
	public List<E> replaceAt( int index, E e ) {
		final int len = elemsLength();
		if ( index >= len ) {
			return thisWith( length, tail.replaceAt( index - len, e ) );
		}
		Nonnull.element( e );
		if ( index == 0 ) {
			return drop( 1 ).prepand( e );
		}
		if ( index == len - 1 ) {
			return take( len - 1 ).concat( tail.prepand( e ) );
		}
		return take( index ).concat( drop( index + 1 ).prepand( e ) );
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
		final int len = elemsLength();
		if ( count == len ) { // took this elems without tail
			return thisWith( len, empty() );
		}
		if ( count < len ) { // took parts of this elems
			return recessive( count, ( len - count ) + offset(), elems, empty() );
		}
		// took this hole elems and parts of the tails elements
		return thisWith( count, tail.take( count - len ) );
	}

	@Override
	public String toString() {
		StringBuilder b = new StringBuilder();
		final int len = elemsLength();
		for ( int i = 0; i < len; i++ ) {
			b.append( ',' );
			b.append( String.valueOf( at( i ) ) );
		}
		return "[" + b.substring( 1 ) + "]" + Sequence.CONCAT_OPERATOR_SYMBOL + tail.toString();
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
	 * The dominant {@link EVolutionList} tries to continue to use (fill) the element array
	 * {@link EVolutionList#elems} referred by a list.
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
	private static final class DominantEVolutionList<E>
			extends EVolutionList<E> {

		private static final int GENERATION_MAX_LENGTH = 1 << 17;

		DominantEVolutionList( int size, Object[] elements, List<E> tail ) {
			super( size, elements, tail );
		}

		public final List<E> prepand( E e ) {
			Nonnull.element( e );
			final int len = elemsLength();
			int index = prepandIndex( len );
			if ( index < 0 ) { // elems capacity exceeded
				return grow1( newGeneration( e ), this );
			}
			if ( prepandedOccupying( e, index ) ) {
				return grow1( elems, tail );
			}
			// if more the halve of the elems is used do we recycle them as recessive tail and start a new clean head for the new list
			if ( len > elems.length / 2 ) {
				return grow1( newGeneration( e ), recessive( length, elems, tail ) );
			}
			// otherwise we want to stay tidy so we copy our elements 
			Object[] copy = Array.copy( elems, index + 1, len );
			copy[index] = e;
			return grow1( copy, tail );
		}

		private Object[] newGeneration( E e ) {
			return Array.withLastElement( e, min( GENERATION_MAX_LENGTH, elems.length << 1 ) ); // grow with power of 2 until 65536
		}

		@Override
		public List<E> tidyUp() {
			List<E> tidyTail = tail.tidyUp();
			int len = elemsLength();
			if ( len == elems.length ) {
				return thisWithChecked( tidyTail );
			}
			synchronized ( elems ) {
				if ( notOccupied( prepandIndex( len ) ) ) {
					return thisWithChecked( tidyTail );
				}

			}
			return dominant( length, Array.copy( elems, elems.length - len, len ), tidyTail );
		}

		private List<E> thisWithChecked( List<E> sameTail ) {
			return sameTail == tail
				? this
				: dominant( length, elems, sameTail );
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
	 * A recessive {@link EVolutionList} already knew that it cannot occupy further elements from
	 * the {@link EVolutionList#elems}. It is a *view* to a section of those elements.
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
	private static final class RecessiveEVolutionList<E>
			extends EVolutionList<E> {

		private final int offset;

		RecessiveEVolutionList( int size, int offset, Object[] elems, List<E> tail ) {
			super( size, elems, tail );
			this.offset = offset;
		}

		@Override
		public List<E> prepand( E e ) {
			return dominant( length + 1, Array.withLastElement( e, elems.length ), this );
		}

		@Override
		public List<E> tidyUp() {
			Object[] s = new Object[elems.length];
			final int len = elemsLength();
			System.arraycopy( elems, elems.length - len - offset, s, elems.length - len, len );
			return dominant( length, s, tail.tidyUp() );
		}

		@SuppressWarnings ( "unchecked" )
		@Override
		E element( int index, int len ) {
			return (E) elems[elems.length - len + index - offset];
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