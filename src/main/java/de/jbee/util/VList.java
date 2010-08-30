package de.jbee.util;

/**
 * 
 * <h3>Disadvantages</h3>
 * <ul>
 * <li>As most manipulations requires creation of a single new instance of {@link VList} the needed
 * passing of 4 constructor arguments might cost a notable part of these operations, because in
 * every case the 4 references/values has to be pushed onto the stack which costs 4 Bytecode
 * instructions.</li>
 * </ul>
 * 
 * @author Jan Bernitt (jan.bernitt@gmx.de)
 * 
 * @param <T>
 */
public final class VList<T> {

	/**
	 * Marks <code>null</code> references placed in list so even <code>null</code> can be detected
	 * as already used cell in the {@link #elements}.
	 */
	private static final Object NULL = new Object();

	/**
	 * The empty {@link VList}. It contains a single null element so that the {@link #elements} stay
	 * unchanged since they seamed to be filled with data (the {@link #NULL} reference).
	 */
	private static final VList<?> EMPTY = new VList<Object>( 0, new Object[] { NULL }, null, 0, 0 );

	/**
	 * The total count of the elements contains in the list (including all those of all tail-lists)
	 */
	final int count;
	/**
	 * The elements at the beginning of the hole list. This must not use all of them. Only
	 * {@link #elementsUsed()} elements used by this list. Rest used by list sharing elements with
	 * this one.
	 */
	final Object[] elements;
	/**
	 * List contains the elements after {@link #elements}.
	 */
	final VList<T> tail;
	/**
	 * The amount of elements used from the direct {@link #tail} list {@link #elements}.
	 */
	final int tailUse;
	/**
	 * The offset from index 0 of the {@link #tail}s {@link #elements} where used by this start of a
	 * list.
	 */
	final int tailOffset;

	VList( int count, Object[] elements, VList<T> tail, int tailOffset, int tailUse ) {
		super();
		this.tail = tail;
		this.tailUse = tailUse;
		this.tailOffset = tailOffset;
		this.count = count;
		this.elements = elements;
	}

	public VList<T> tail() {
		return elementsUsed() > 1
			? new VList<T>( count - 1, elements, tail, tailOffset, tailUse )
			: tail;
	}

	private int elementsUsed() {
		return count == 0
			? 0
			: count - tail.count;
	}

	private int maxLevelElements() {
		int res = 1;
		VList<T> cur = tail;
		while ( cur != null ) {
			res <<= 1;
			cur = cur.tail;
		}
		return res;
	}

	public T head() {
		return at( elementsUsed() - 1 );
	}

	public T at( int i ) {
		// TODO
		return null;
	}

	public VList<T> prepand( T e ) {
		return prepand( elementsUsed(), e );
	}

	private VList<T> prepand( int index, Object e ) {
		if ( e == null ) {
			e = NULL;
		}
		if ( index < elements.length ) {
			return share( index, e );
		}
		return grow( index, e );
	}

	private VList<T> share( int index, Object e ) {
		if ( setElement( index, e ) ) {
			return extended( elements, tail, tailUse );
		}
		return grow( index, e );
	}

	private VList<T> grow( int index, Object e ) {
		final int max = maxLevelElements();
		final int halveMax = max >> 1;
		final int twiceMax = max << 1;
		int length = elements.length >> 2; // same as div 4
		if ( index >= 15 || index >= halveMax ) { // more than 16 or at least half the subtree is reused 
			// than do not copy
			return growLevel( index, e, Math.max( length, Math.min( 8, twiceMax ) ) );
		}
		return extended( copyAndSet( index, e, max ), tail, tailUse );
	}

	private VList<T> growLevel( int index, Object e, int length ) {
		Object[] elems = new Object[length];
		elems[0] = e;
		return extended( elems, this, index );
	}

	private VList<T> extended( Object[] elements, VList<T> tail, int tailUsed ) {
		return new VList<T>( count + 1, elements, tail, tailOffset, tailUsed );
	}

	private Object[] copyAndSet( int index, Object e, int length ) {
		Object[] copy = new Object[length];
		System.arraycopy( elements, 0, copy, 0, index );
		copy[index] = e;
		return copy;
	}

	private synchronized boolean setElement( int index, Object e ) {
		Object cur = elements[index];
		if ( cur == null ) {
			elements[index] = e;
			return true;
		}
		return cur == e;
	}

	@Override
	public String toString() {
		StringBuilder b = new StringBuilder();
		toString( this, b );
		return b.toString();
	}

	private static <T> void toString( VList<T> block, StringBuilder b ) {
		if ( block == null ) {
			return;
		}
		b.append( '[' );
		int used = block.elementsUsed();
		for ( int i = 0; i < block.elements.length; i++ ) {
			if ( i < used ) {
				b.append( block.elements[i] );
			} else {
				b.append( '?' );
			}
			if ( i < block.elements.length - 1 ) {
				b.append( ',' );
				b.append( ' ' );
			}
		}
		b.append( ']' );
		b.append( '\n' );
		toString( block.tail, b );
	}

	public static void main( String[] args ) {
		VList<Integer> l = (VList<Integer>) EMPTY;
		for ( int i = 0; i < 100; i++ ) {
			System.out.println( l );
			l = l.prepand( i );
		}
	}
}
