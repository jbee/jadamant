/**
 * 
 */
package de.jbee.lang.seq;

import static de.jbee.lang.Calculate.nextHighestPowerOf2;
import static java.lang.Math.min;
import de.jbee.lang.Array;
import de.jbee.lang.List;
import de.jbee.lang.Segment;
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

	/**
	 * The maximum count of elements contained in a single generation.
	 */
	static final int GENERATION_MAX_LENGTH = 1 << 16;

	static final Object LOCK = new Object();

	static <E> List<E> dominant( int length, Object[] elements ) {
		return dominant( length, elements, List.with.<E> noElements() );
	}

	static <E> List<E> dominant( int length, Object[] elements, List<E> tail ) {
		return new DominantList<E>( length, elements, tail );
	}

	static <E> List<E> growth( int length, int generationSize, Object[] elements, List<E> tail ) {
		return new GrowthList<E>( length, generationSize, elements, tail );
	}

	static int halfOf( int value ) {
		return value >> 1;
	}

	static <E> List<E> recessive( int length, int offset, Object[] elements, List<E> tail ) {
		return new RecessiveList<E>( length, offset, elements, tail );
	}

	static int twiceOf( int value ) {
		return value << 1;
	}

	/**
	 * list's size in total with {@link #tail}-list elements
	 */
	final int length;

	/**
	 * Is filled with elements from highest to lowest index using {@link #prepand(Object)}. The
	 * element used are forming the {@link #segmentLength()}.
	 */
	final Object[] elems;

	/**
	 * The next {@link #subsequent()} {@link Segment}.
	 */
	final List<E> tail;

	EVolutionList( int length, Object[] elements, List<E> tail ) {
		super();
		this.length = length;
		this.elems = elements;
		this.tail = tail;
	}

	@Override
	public List<E> append( E e ) {
		return thisWith( length + 1, tail.append( e ) );
	}

	@Override
	public final E at( int index ) {
		final int len = segmentLength();
		return index >= len
			? tail.at( index - len )
			: element( index );
	}

	@Override
	public final List<E> concat( List<E> other ) {
		return thisWith( length + other.length(), tail.concat( other ) );
	}

	@Override
	public final List<E> deleteAt( int index ) {
		if ( index < 0 ) {
			return this;
		}
		final int len = segmentLength();
		if ( index == 0 ) { // first of this elems
			return len == 1
				? tail
				: sectorWith( length - 1, tail );
		}
		if ( index >= len ) { // not in this elems
			return thisWith( length - 1, tail.deleteAt( index - len ) );
		}
		if ( index == len - 1 ) { // last of this elems
			return sectorWith( length - 1, +1, tail );
		}
		// somewhere in between our elems ;(
		return sectorWith( length - 1, len - index, // tail:
				sectorWith( length - 1 - index, tail ) );
	}

	@Override
	public final List<E> drop( int count ) {
		if ( count <= 0 ) {
			return this;
		}
		if ( count >= length ) {
			return empty();
		}
		final int len = segmentLength();
		return count >= len
			? tail.drop( count - len )
			: sectorWith( length - count, tail );
	}

	@Override
	public final void fill( int offset, Object[] dest, int start, int upToLen ) {
		final int len = segmentLength();
		if ( start < len ) {
			final int first = first() + start;
			final int copiedLength = min( upToLen, len - start );
			System.arraycopy( elems, first, dest, offset, copiedLength );
			if ( copiedLength < upToLen ) {
				tail.fill( offset + copiedLength, dest, 0, upToLen - copiedLength );
			}
		} else {
			tail.fill( offset, dest, start - len, upToLen );
		}
	}

	@Override
	public final List<E> insertAt( int index, E e ) {
		if ( index == 0 ) {
			return prepand( e );
		}
		final int len = segmentLength();
		if ( index == 1 ) { // avoid 'views' just using a single element (as first)
			return List.with.element( e ).prepand( at( 0 ) ).concat( drop( index ) );
		}
		if ( index >= len ) {
			return thisWith( length + 1, tail.insertAt( index - len, e ) );
		}
		//TODO try to avoid inappropriate reuse of this stack -> especially for the recessive list
		return take( index ).concat( drop( index ).prepand( e ) );
	}

	@Override
	public final boolean isEmpty() {
		return false; // otherwise it would be another class 
	}

	@Override
	public final int length() {
		return length;
	}

	@Override
	public final List<E> replaceAt( int index, E e ) {
		final int len = segmentLength();
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
	public List<E> subsequent() {
		return tail;
	}

	@Override
	public final List<E> take( int count ) {
		if ( count <= 0 ) {
			return empty();
		}
		if ( count >= length ) {
			return this;
		}
		final int len = segmentLength();
		if ( count == len ) { // took this elems without tail
			return thisWith( len, empty() );
		}
		if ( count < len ) { // took parts of this elems
			return sectorWith( count, len - count, empty() );
		}
		// took this hole elems and parts of the tails elements
		return thisWith( count, tail.take( count - len ) );
	}

	@Override
	public List<E> tidyUp() {
		final int len = segmentLength();
		if ( len == elems.length ) { // all available elements are occupied - this is always save
			return thisWith( tail.tidyUp() );
		}
		final int index = prepandIndex( len );
		if ( elems[index] == LOCK ) { // the next used element is the lock so we are sure nobody will occupy further cells with real data 
			return thisWith( tail.tidyUp() );
		}
		if ( length < 16 ) { // for short lists we are also contract the tail to a single tidy segment 
			Object[] tidy = new Object[length];
			fill( 0, tidy, 0, length );
			return growth( length, nextHighestPowerOf2( length ), tidy, empty() );
		}
		if ( len > 16 && len > halfOf( elems.length ) ) { // just try to reuse by locking if length is worth a try 
			synchronized ( elems ) {
				if ( canOccupy( index ) ) {
					elems[index] = LOCK;
					return thisWith( tail.tidyUp() );
				}
			}
		}
		return growth( length, elems.length, segmentElements(), tail.tidyUp() );
	}

	@Override
	public String toString() {
		StringBuilder b = new StringBuilder();
		final int len = segmentLength();
		for ( int i = 0; i < len; i++ ) {
			b.append( ',' );
			b.append( String.valueOf( at( i ) ) );
		}
		return "[" + b.substring( 1 ) + "]" + Sequence.CONCAT_OPERATOR_SYMBOL + tail.toString();
	}

	@Override
	public void traverse( int start, Traversal<? super E> traversal ) {
		final int len = segmentLength();
		int i = start;
		int inc = 0;
		while ( inc >= 0 && i < len ) {
			inc = traversal.incrementOn( element( i ) );
			i += inc;
		}
		if ( inc >= 0 ) {
			tail.traverse( i - len, traversal );
		}
	}

	final boolean canOccupy( int index ) {
		return elems[index] == null;
	}

	/**
	 * @return A copy of just the {@link #elems} that contribute to this lists
	 *         {@link #segmentLength()}
	 */
	final Object[] segmentElements() {
		return Array.segment( elems, first(), segmentLength() );
	}

	@SuppressWarnings ( "unchecked" )
	final E element( int index ) {
		return (E) elems[first() + index];
	}

	final List<E> empty() {
		return List.with.noElements();
	}

	final Object[] nextGeneration( E e ) {
		return nextGeneration( e, twiceOf( elems.length ) );
	}

	final Object[] nextGeneration( E e, int size ) {
		return Array.withLastElement( e, min( GENERATION_MAX_LENGTH, size ) ); // grow with power of 2 until 65536
	}

	final boolean prepandedOccupying( E e, int index ) {
		synchronized ( elems ) {
			if ( canOccupy( index ) ) {
				elems[index] = e;
				return true;
			}
			return false;
		}
	}

	/**
	 * @return The index at which the next element would be prepanded. Expects offset to be zero.
	 */
	final int prepandIndex( final int len ) {
		return elems.length - 1 - len;
	}

	/**
	 * @return The count of {@link #elems} that are used by this lists.
	 */
	final int segmentLength() {
		return length - tail.length();
	}

	final List<E> thisWith( List<E> maybeSameTail ) {
		return maybeSameTail == tail
			? this
			: thisWith( length, tail );
	}

	/**
	 * @return The index in {@link #elems} of the first occupied element.
	 */
	abstract int first();

	/**
	 * A 'view' upon the {@link #elems} of this list that just uses some of its elements. They
	 * should just be used as temporary objects since further (not used) elements are referred from
	 * the same array used for the segment.
	 */
	abstract List<E> sectorWith( int length, int additionalOffset, List<E> tail );

	/**
	 * Same as {@link #sectorWith(int, int, List)} but no additional offset given. This is just a
	 * optimization so that we save pushing a zero on the stack.
	 */
	abstract List<E> sectorWith( int length, List<E> tail );

	/**
	 * The usage of the elements in this segment/generation doesn't change but the tail does and
	 * therefore sometimes the overall length too.
	 * 
	 * keeps tidy or untidy since the change is in the tail. just replace the tail and reduce
	 * overall size by one. The elements and length of this elems are reused as before.
	 */
	abstract List<E> thisWith( int length, List<E> tail );

	/**
	 * The dominant {@link EVolutionList} tries to continue to use (fill) the element array
	 * {@link EVolutionList#elems} referred by a list.
	 * 
	 * @author Jan Bernitt (jan.bernitt@gmx.de)
	 */
	private static final class DominantList<E>
			extends EVolutionList<E> {

		DominantList( int length, Object[] elements, List<E> tail ) {
			super( length, elements, tail );
		}

		@Override
		public final List<E> prepand( E e ) {
			Nonnull.element( e );
			final int len = segmentLength();
			int index = prepandIndex( len );
			if ( index < 0 ) { // elems capacity exceeded
				return enlarge1( nextGeneration( e ), this );
			}
			if ( prepandedOccupying( e, index ) ) {
				return enlarge1( elems, tail );
			}
			// if more the halve of the elems is used do we recycle them as recessive tail and start a new clean head for the new list
			if ( len > halfOf( elems.length ) ) {
				return enlarge1( nextGeneration( e ), sectorWith( length, tail ) );
			}
			// otherwise we want to stay tidy so we copy our elements 
			Object[] copy = Array.clone( elems, index + 1, len );
			copy[index] = e;
			return enlarge1( copy, tail );
		}

		@Override
		int first() {
			return elems.length - segmentLength();
		}

		@Override
		List<E> sectorWith( int length, int additionalOffset, List<E> tail ) {
			return recessive( length, additionalOffset, elems, tail );
		}

		@Override
		List<E> sectorWith( int length, List<E> tail ) {
			return recessive( length, 0, elems, tail );
		}

		@Override
		List<E> thisWith( int length, List<E> tail ) {
			return dominant( length, elems, tail );
		}

		private List<E> enlarge1( Object[] elems, List<E> tail ) {
			return dominant( length + 1, elems, tail );
		}

	}

	private static final class GrowthList<E>
			extends EVolutionList<E> {

		/**
		 * The next generation will be created when the length of this elements reaches/exceeds this
		 * size. Until then this will grow by double the capacity of the elements and copy existing
		 * into the new.
		 * 
		 * The size is a power of 2.
		 */
		private final int generationSize;

		GrowthList( int length, int generationSize, Object[] elements, List<E> tail ) {
			super( length, elements, tail );
			this.generationSize = generationSize;
		}

		@Override
		public List<E> prepand( E e ) {
			Nonnull.element( e );
			final int len = segmentLength();
			int index = prepandIndex( len );
			if ( index < 0 ) { // elems capacity exceeded
				return enlarge1( e );
			}
			if ( prepandedOccupying( e, index ) ) {
				return thisWith( length + 1, tail );
			}
			return enlarge1( e );
		}

		@Override
		int first() {
			return elems.length - segmentLength();
		}

		@Override
		List<E> sectorWith( int length, int additionalOffset, List<E> tail ) {
			final int len = length - tail.length();
			Object[] segment = Array.segment( elems, elems.length - len - additionalOffset, len );
			return growth( length, elems.length, segment, tail );
		}

		@Override
		List<E> sectorWith( int length, List<E> tail ) {
			return sectorWith( length, 0, tail );
		}

		@Override
		List<E> thisWith( int length, List<E> tail ) {
			return growth( length, generationSize, elems, tail );
		}

		private List<E> enlarge1( E e ) {
			// this is a important case since tidy up will cause a full occupied list - instead of copying from that all the time we reuse it as tail if it is long enough
			if ( elems.length > halfOf( generationSize ) ) {
				final int size = elems.length < generationSize
					? generationSize
					: twiceOf( generationSize );
				return dominant( length + 1, nextGeneration( e, size ), this );
			}
			Object[] enlarged = Array.segment( elems, 0, nextHighestPowerOf2( elems.length ) );
			enlarged[enlarged.length - 1 - segmentLength()] = e;
			return growth( length + 1, generationSize, enlarged, tail );
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
	private static final class RecessiveList<E>
			extends EVolutionList<E> {

		private final int offset;

		RecessiveList( int length, int offset, Object[] elems, List<E> tail ) {
			super( length, elems, tail );
			this.offset = offset;
		}

		@Override
		public List<E> prepand( E e ) {
			final int len = segmentLength();
			int halfOfElems = halfOf( elems.length );
			if ( len < 8 || len < halfOfElems ) {
				final int size = nextHighestPowerOf2( len + 1 );
				Object[] elements = Array.segment( elems, first(), len, new Object[size] );
				elements[elements.length - len - 1] = e;
				return growth( length + 1, elems.length, elements, tail );
			}
			return dominant( length + 1, nextGeneration( e, elems.length ), this );
		}

		@Override
		public List<E> tidyUp() {
			return growth( length, elems.length, segmentElements(), tail.tidyUp() );
		}

		@Override
		int first() {
			return elems.length - segmentLength() - offset;
		}

		@Override
		List<E> sectorWith( int length, int additionalOffset, List<E> tail ) {
			return recessive( length, offset + additionalOffset, elems, tail );
		}

		@Override
		List<E> sectorWith( int length, List<E> tail ) {
			return recessive( length, offset, elems, tail );
		}

		@Override
		List<E> thisWith( int length, List<E> tail ) {
			return recessive( length, offset, elems, tail );
		}

	}

}