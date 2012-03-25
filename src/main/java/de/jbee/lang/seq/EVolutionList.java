/**
 * 
 */
package de.jbee.lang.seq;

import static de.jbee.lang.Calculate.nextHighestPowerOf2;
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

	static <E> List<E> recessive( int length, int offset, Object[] elements, List<E> tail ) {
		return new RecessiveList<E>( length, offset, elements, tail );
	}

	static <E> List<E> growth( int length, int generationSize, Object[] elements, List<E> tail ) {
		return new GrowthList<E>( length, generationSize, elements, tail );
	}

	/**
	 * list's size in total with {@link #tail}-list elements
	 */
	final int length;
	/**
	 * Is filled with elements from highest to lowest index using {@link #prepand(Object)}. The
	 * element used are forming the {@link #population()}.
	 */
	final Object[] elems;
	final List<E> tail;

	EVolutionList( int length, Object[] elements, List<E> tail ) {
		super();
		this.length = length;
		this.elems = elements;
		this.tail = tail;
	}

	@Override
	public List<E> append( E e ) {
		//TODO improve - this works but covers no optimization case
		return thisWith( length + 1, tail.append( e ) );
	}

	@Override
	public final E at( int index ) {
		final int len = population();
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
		final int len = population();
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
	public List<E> drop( int count ) {
		if ( count <= 0 ) {
			return this;
		}
		if ( count >= length ) {
			return empty();
		}
		final int len = population();
		return count >= len
			? tail.drop( count - len )
			: sectorWith( length - count, tail );
	}

	@Override
	public void fill( int offset, Object[] array, int start, int upToLen ) {
		final int len = population();
		if ( start < len ) {
			final int srcPos = elems.length - len - offset() + start;
			final int copiedLength = min( upToLen, len - start );
			System.arraycopy( elems, srcPos, array, offset, copiedLength );
			if ( copiedLength < upToLen ) {
				tail.fill( offset + copiedLength, array, 0, upToLen - copiedLength );
			}
		} else {
			tail.fill( offset, array, start - len, upToLen );
		}
	}

	@Override
	public List<E> insertAt( int index, E e ) {
		if ( index == 0 ) {
			return prepand( e );
		}
		final int len = population();
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
	public final int length() {
		return length;
	}

	@Override
	public List<E> replaceAt( int index, E e ) {
		final int len = population();
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
	public List<E> take( int count ) {
		if ( count <= 0 ) {
			return empty();
		}
		if ( count >= length ) {
			return this;
		}
		final int len = population();
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
	public String toString() {
		StringBuilder b = new StringBuilder();
		final int len = population();
		for ( int i = 0; i < len; i++ ) {
			b.append( ',' );
			b.append( String.valueOf( at( i ) ) );
		}
		return "[" + b.substring( 1 ) + "]" + Sequence.CONCAT_OPERATOR_SYMBOL + tail.toString();
	}

	@Override
	public void traverse( int start, Traversal<? super E> traversal ) {
		final int len = population();
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

	final boolean canOccupy( int index ) {
		return elems[index] == null;
	}

	abstract E element( int index, int len );

	final List<E> empty() {
		return List.with.noElements();
	}

	final Object[] nextGeneration( E e ) {
		return Array.withLastElement( e, min( GENERATION_MAX_LENGTH, elems.length << 1 ) ); // grow with power of 2 until 65536
	}

	abstract int offset();

	/**
	 * @return The count of {@link #elems} that are used by this lists.
	 */
	final int population() {
		return length - tail.length();
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

	abstract Object[] copyTailEnd( int length );

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
	private static final class DominantList<E>
			extends EVolutionList<E> {

		DominantList( int length, Object[] elements, List<E> tail ) {
			super( length, elements, tail );
		}

		@Override
		public final List<E> prepand( E e ) {
			Nonnull.element( e );
			final int len = population();
			int index = prepandIndex( len );
			if ( index < 0 ) { // elems capacity exceeded
				return enlarge1( nextGeneration( e ), this );
			}
			if ( prepandedOccupying( e, index ) ) {
				return enlarge1( elems, tail );
			}
			// if more the halve of the elems is used do we recycle them as recessive tail and start a new clean head for the new list
			if ( len > elems.length / 2 ) {
				return enlarge1( nextGeneration( e ), sectorWith( length, tail ) );
			}
			// otherwise we want to stay tidy so we copy our elements 
			Object[] copy = Array.clone( elems, index + 1, len );
			copy[index] = e;
			return enlarge1( copy, tail );
		}

		@Override
		public List<E> tidyUp() {
			final int len = population();
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
			if ( len > 16 && len > elems.length / 2 ) { // just try to reuse if length is worth a try 
				synchronized ( elems ) {
					if ( canOccupy( index ) ) {
						elems[index] = LOCK;
						return thisWith( tail.tidyUp() );
					}
				}
			}
			return growth( length, elems.length, copyTailEnd( len ), tail.tidyUp() );
		}

		@Override
		Object[] copyTailEnd( int length ) {
			return Array.segment( elems, elems.length - length, length );
		}

		@SuppressWarnings ( "unchecked" )
		@Override
		E element( int index, int len ) {
			return (E) elems[elems.length - len + index];
		}

		@Override
		int offset() {
			return 0;
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

		private List<E> thisWith( List<E> maybeSameTail ) {
			return maybeSameTail == tail
				? this
				: dominant( length, elems, maybeSameTail );
		}

	}

	private static final class GrowthList<E>
			extends EVolutionList<E> {

		/**
		 * The next generation will be created when the length of this reaches/exceeds this size.
		 * The size is a power of 2.
		 */
		private final int generationSize;

		GrowthList( int length, int generationSize, Object[] elements, List<E> tail ) {
			super( length, elements, tail );
			this.generationSize = generationSize;
		}

		@Override
		public List<E> prepand( E e ) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public List<E> tidyUp() {
			// TODO Auto-generated method stub
			return null;
		}

		@SuppressWarnings ( "unchecked" )
		@Override
		E element( int index, int len ) {
			return (E) elems[elems.length - len + index];
		}

		@Override
		int offset() {
			return 0;
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

		@Override
		Object[] copyTailEnd( int length ) {
			return Array.segment( elems, elems.length - length, length );
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
			//OPEN better get tidy again by copying the sector part ?
			return dominant( length + 1, Array.withLastElement( e, elems.length ), this );
		}

		@Override
		public List<E> tidyUp() {
			return growth( length, elems.length, copyTailEnd( population() ), tail.tidyUp() );
		}

		@SuppressWarnings ( "unchecked" )
		@Override
		E element( int index, int len ) {
			return (E) elems[elems.length - len + index - offset];
		}

		@Override
		int offset() {
			return offset;
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

		@Override
		Object[] copyTailEnd( int length ) {
			return Array.segment( elems, elems.length - length - offset, length );
		}

	}

}