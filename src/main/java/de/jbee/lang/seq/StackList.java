/**
 * 
 */
package de.jbee.lang.seq;

import de.jbee.lang.Array;
import de.jbee.lang.Enum;
import de.jbee.lang.Enumerate;
import de.jbee.lang.Enumerator;
import de.jbee.lang.EnumeratorFactory;
import de.jbee.lang.Lang;
import de.jbee.lang.List;
import de.jbee.lang.Lister;
import de.jbee.lang.Sequence;
import de.jbee.lang.Traversal;
import de.jbee.lang.dev.Nonnull;

/**
 * The data-structure of the core list consists of a chain of partial lists. The capacity of each
 * partial list is growing with the power of 2 starting with 1. If the capacity is exceeded a new
 * partial list is created having the current list as its tail list. The created list has twice the
 * capacity of the last partial list. Thereby the longest partial list is always first. Through this
 * the majority (>= 50%) of elements are always contained in the first or second partial list. A
 * random access therefore is just a array index access.
 * 
 * <p>
 * This idea is based on the <a
 * href="http://infoscience.epfl.ch/record/64410/files/techlists.pdf">V-List data-structure by Phil
 * Bagwell</a>
 * </p>
 * 
 * @author Jan Bernitt (jan.bernitt@gmx.de)
 */
abstract class StackList<E>
		implements List<E> {

	static final Lister LISTER = new StackLister();
	static final EnumeratorFactory ENUMERATOR_FACTORY = new StackListEnumeratorFactory();

	static <E> List<E> tidy( int size, Object[] stack, List<E> tail ) {
		return new TidyStackList<E>( size, stack, tail );
	}

	static <E> List<E> untidy( int size, int offset, Object[] stack, List<E> tail ) {
		return new UntidyStackList<E>( size, offset, stack, tail );
	}

	static <E> List<E> untidy( int size, Object[] stack, List<E> tail ) {
		return untidy( size, 0, stack, tail );
	}

	//TODO move to a array util and method
	final Object[] stackCopyFrom( int start, int len ) {
		Object[] copy = new Object[stack.length];
		System.arraycopy( stack, start, copy, start, len );
		return copy;
	}

	/**
	 * list's size in total with tail-list elements
	 */
	final int length;
	final Object[] stack;
	final List<E> tail;

	StackList( int size, Object[] stack, List<E> tail ) {
		super();
		this.length = size;
		this.stack = stack;
		this.tail = tail;
	}

	@Override
	public void traverse( int start, Traversal<? super E> traversal ) {
		final int l = len();
		int i = start;
		int inc = 0;
		while ( inc >= 0 && i < l ) {
			inc = traversal.incrementOn( element( i, l ) );
			i += inc;
		}
		if ( inc > 0 ) {
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
		final int l = len();
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
		//FIXME negative index ?
		final int l = len();
		if ( index == 0 ) { // first of this stack
			return l == 1
				? tail
				: untidy( length - 1, stack, tail );
		}
		if ( index >= l ) { // not in this stack
			return thisWith( length - 1, tail.deleteAt( index - l ) );
		}
		if ( index == l - 1 ) { // last of this stack
			return untidy( length - 1, 1, stack, tail );
		}
		// somewhere in between our stack ;(
		return untidy( length - 1, l - index, stack, untidy( length - 1 - index, stack, tail ) );
	}

	@Override
	public List<E> drop( int count ) {
		if ( count <= 0 ) {
			return this;
		}
		if ( count >= length ) {
			return empty();
		}
		final int l = len();
		return count >= l
			? tail.drop( count - l )
			: untidy( length - count, stack, tail );
	}

	@Override
	public void fill( int offset, Object[] array, int start, int len ) {
		final int l = len();
		if ( start < l ) {
			int srcPos = stack.length - l - offset() + start;
			int copyLength = Math.min( len, l );
			System.arraycopy( stack, srcPos, array, offset, copyLength );
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
		final int l = len();
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
		final int l = len();
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
		final int l = len();
		if ( count == l ) { // took this stack without tail
			return thisWith( l, empty() );
		}
		if ( count < l ) { // took parts of this stack
			return untidy( count, ( l - count ) + offset(), stack, empty() );
		}
		// took this hole stack and parts of the tails elements
		return thisWith( count, tail.take( count - l ) );
	}

	@Override
	public String toString() {
		StringBuilder b = new StringBuilder();
		final int l = len();
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
	 * @return The amount of elements dedicated to this lists stack
	 */
	final int len() {
		return length - tail.length();
	}

	abstract int offset();

	/**
	 * keeps tidy or untidy since the change is in the tail. just replace the tail and reduce
	 * overall size by one. The elements and length of this stack are reused as before.
	 */
	abstract List<E> thisWith( int size, List<E> tail );

	static final class TidyStackList<E>
			extends StackList<E> {

		TidyStackList( int size, Object[] stack, List<E> tail ) {
			super( size, stack, tail );
		}

		public List<E> prepand( E e ) {
			Nonnull.element( e );
			final int l = len();
			int index = occupationIndex( l );
			if ( index < 0 ) { // stack capacity exceeded
				return grow1( Array.withLastElement( e, stack.length * 2 ), this );
			}
			if ( prepandedOccupying( e, index ) ) {
				return grow1( stack, tail );
			}
			if ( l > stack.length / 2 ) {
				return grow1( Array.withLastElement( e, stack.length * 2 ), untidy( length, stack,
						tail ) );
			}
			Object[] copy = stackCopyFrom( index + 1, l );
			copy[index] = e;
			return grow1( copy, tail );
		}

		@Override
		public List<E> tidyUp() {
			List<E> tidyTail = tail.tidyUp();
			int l = len();
			synchronized ( stack ) {
				if ( notOccupied( occupationIndex( l ) ) ) {
					return tidyTail == tail
						? this
						: tidy( length, stack, tidyTail );
				}

			}
			return tidy( length, stackCopyFrom( 0, l ), tail );
		}

		@SuppressWarnings ( "unchecked" )
		@Override
		final E element( int index, int l ) {
			return (E) stack[stack.length - l + index];
		}

		@Override
		final int offset() {
			return 0;
		}

		@Override
		final List<E> thisWith( int size, List<E> tail ) {
			return tidy( size, stack, tail );
		}

		private List<E> grow1( Object[] stack, List<E> tail ) {
			return tidy( length + 1, stack, tail );
		}

		private boolean notOccupied( int index ) {
			return stack[index] == null;
		}

		private int occupationIndex( final int len ) {
			return stack.length - 1 - len;
		}

		private boolean prepandedOccupying( E e, int index ) {
			synchronized ( stack ) {
				if ( notOccupied( index ) ) {
					stack[index] = e;
					return true;
				}
				return false;
			}
		}

	}

	static final class UntidyStackList<E>
			extends StackList<E> {

		private final int offset;

		UntidyStackList( int size, int offset, Object[] stack, List<E> tail ) {
			super( size, stack, tail );
			this.offset = offset;
		}

		@Override
		public List<E> prepand( E e ) {
			return tidy( length + 1, Array.withLastElement( e, stack.length * 2 ), this );
		}

		@Override
		public List<E> tidyUp() {
			Object[] s = new Object[stack.length];
			final int l = len();
			System.arraycopy( stack, stack.length - l - offset, s, stack.length - l, l );
			return tidy( length, s, tail.tidyUp() );
		}

		@SuppressWarnings ( "unchecked" )
		@Override
		E element( int index, int l ) {
			return (E) stack[stack.length - l + index - offset];
		}

		@Override
		final int offset() {
			return offset;
		}

		@Override
		List<E> thisWith( int size, List<E> tail ) {
			return untidy( size, offset, stack, tail );
		}

	}

	static final class StackListEnumerator<E>
			extends Enumerate.StepwiseEnumerator<E> {

		StackListEnumerator( Enum<E> type ) {
			super( type );
		}

		@Override
		protected List<E> fromTo( E first, E last, Enum<E> type ) {
			int fo = type.toOrdinal( first );
			int lo = type.toOrdinal( last );
			if ( fo == lo ) { // length 1
				return LISTER.element( first );
			}
			int l = Math.abs( fo - lo ) + 1;
			if ( l == 2 ) {
				return LISTER.element( last ).prepand( first );
			}
			int capacity = 2;
			List<E> res = LISTER.noElements();
			E cur = last;
			final boolean asc = fo < lo;
			int size = 0;
			while ( size < l ) { // length will be > 2
				Object[] stack = new Object[capacity];
				int min = size + capacity < l
					? 0
					: capacity - ( l - size );
				for ( int i = capacity - 1; i >= min; i-- ) {
					stack[i] = cur;
					size++;
					if ( size < l ) {
						cur = asc
							? type.pred( cur )
							: type.succ( cur );
					}
				}
				res = new TidyStackList<E>( size, stack, res );
				capacity += capacity;
			}
			return res;
		}

	}

	static final class StackListEnumeratorFactory
			implements EnumeratorFactory {

		@Override
		public <E> Enumerator<E> enumerates( Enum<E> type ) {
			return new StackListEnumerator<E>( type );
		}

	}

	// TODO this is not exactly the Lister for stack list - its the one using all the different default core List impl. - move and rename proper
	static class StackLister
			implements Lister {

		@Override
		public <E> List<E> element( E e ) {
			return SingleElementList.with( e );
		}

		@Override
		public <E> List<E> elements( E... elems ) {
			List<E> res = noElements();
			for ( int i = elems.length - 1; i >= 0; i-- ) {
				res = res.prepand( elems[i] );
			}
			return res;
		}

		@Override
		public <E> List<E> elements( Sequence<E> elems ) {
			if ( elems.isEmpty() ) {
				return noElements();
			}
			final int size = elems.length();
			if ( size == 1 ) {
				return element( elems.at( 0 ) );
			}
			Object[] stack = new Object[Lang.nextHighestPowerOf2( size )];
			int index = stack.length - size;
			for ( int i = 0; i < size; i++ ) {
				stack[index++] = elems.at( i );
			}
			return tidy( size, stack, EmptyList.<E> instance() );
		}

		@Override
		public <E> List<E> noElements() {
			return EmptyList.instance();
		}

	}

}