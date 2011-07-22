/**
 * 
 */
package de.jbee.core.list;

import java.util.Iterator;

import de.jbee.core.IndexAccess;
import de.jbee.core.dev.Nonnull;
import de.jbee.core.type.Enum;
import de.jbee.core.type.Enumerate;
import de.jbee.util.ICluster;

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

	static <E> List<E> primary( int size, Object[] stack, List<E> tail ) {
		return new PrimaryStackList<E>( size, stack, tail );
	}

	// TODO hide secondary methods since only primary and secondary itself should build them

	static <E> List<E> secondary( int size, int offset, Object[] stack, List<E> tail ) {
		return new SecondaryStackList<E>( size, offset, stack, tail );
	}

	static <E> List<E> secondary( int size, Object[] stack, List<E> tail ) {
		return secondary( size, 0, stack, tail );
	}

	/**
	 * list's size in total with tail-list elements
	 */
	final int size;
	final Object[] stack;

	final List<E> tail;

	StackList( int size, Object[] stack, List<E> tail ) {
		super();
		this.size = size;
		this.stack = stack;
		this.tail = tail;
	}

	@Override
	public List<E> append( E e ) {
		//TODO improve - this works but covers no optimization case
		return thisWith( size + 1, tail.append( e ) );
	}

	@Override
	public final E at( int index ) {
		final int l = length();
		return index >= l
			? tail.at( index - l )
			: element( index, l );
	}

	@Override
	public void fill( int offset, Object[] array, int start, int length ) {
		final int l = length();
		if ( start < l ) {
			int srcPos = stack.length - l - offset() + start;
			int copyLength = Math.min( length, l );
			System.arraycopy( stack, srcPos, array, offset, copyLength );
			if ( start + length > l ) {
				tail.fill( offset + copyLength, array, 0, length - copyLength );
			}
		} else {
			tail.fill( offset, array, start - l, length );
		}
	}

	@Override
	public List<E> concat( List<E> other ) {
		return thisWith( size + other.size(), tail.concat( other ) );
	}

	@Override
	public List<E> deleteAt( int index ) {
		//FIXME negative index ?
		final int length = length();
		if ( index == 0 ) { // first of this stack
			return length == 1
				? tail
				: secondary( size - 1, stack, tail );
		}
		if ( index >= length ) { // not in this stack
			return shrink1( tail.deleteAt( index - length ) );
		}
		if ( index == length - 1 ) { // last of this stack
			return secondary( size - 1, 1, stack, tail );
		}
		// somewhere in between our stack ;(
		return secondary( size - 1, length - index, stack,
				secondary( size - 1 - index, stack, tail ) );
	}

	@Override
	public List<E> drop( int count ) {
		if ( count <= 0 ) {
			return this;
		}
		if ( count >= size ) {
			return empty();
		}
		final int length = length();
		return count >= length
			? tail.drop( count - length )
			: secondary( size - count, stack, tail );
	}

	@Override
	public List<E> insertAt( int index, E e ) {
		if ( index == 0 ) {
			return prepand( e );
		}
		final int length = length();
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public final boolean isEmpty() {
		return false; // otherwise you would have another class 
	}

	@Override
	public final Iterator<E> iterator() {
		return IndexAccess.iterator( this, 0, size );
	}

	@Override
	public final int size() {
		return size;
	}

	@Override
	public final List<E> take( int count ) {
		if ( count <= 0 ) {
			return empty();
		}
		if ( count >= size ) {
			return this;
		}
		final int length = length();
		if ( count == length ) { // took this stack without tail
			return thisWith( length, empty() );
		}
		if ( count < length ) { // took parts of this stack
			return secondary( count, ( length - count ) + offset(), stack, empty() );
		}
		// took this hole stack and parts of the tails elements
		return thisWith( count, tail.take( count - length ) );
	}

	@Override
	public String toString() {
		StringBuilder b = new StringBuilder();
		final int l = length();
		for ( int i = 0; i < l; i++ ) {
			b.append( ',' );
			b.append( String.valueOf( at( i ) ) );
		}
		return "[" + b.substring( 1 ) + "]" + List.CONCAT_OPERATOR_SYMBOL + tail.toString();
	}

	abstract E element( int index, int l );

	final List<E> empty() {
		return InitList.LISTER.noElements();
	}

	/**
	 * @return The amount of elements dedicated to this lists stack
	 */
	final int length() {
		return size - tail.size();
	}

	abstract List<E> thisWith( int size, List<E> tail );

	abstract int offset();

	/**
	 * keeps master when master and branch when branch list - change is in the tail. just replace
	 * the tail and reduce overall size by one. The elements in this stack remains since tail size
	 * is shrinking 1.
	 */
	final List<E> shrink1( List<E> tail ) {
		return thisWith( size - 1, tail );
	}

	public static int nextHighestPowerOf2( int v ) {
		v--;
		v |= v >> 1;
		v |= v >> 2;
		v |= v >> 4;
		v |= v >> 8;
		v |= v >> 16;
		v++;
		return v;
	}

	static <E> Object[] stack( E initial, int length ) {
		Object[] stack = new Object[length];
		stack[length - 1] = initial;
		return stack;
	}

	static class StackLister
			implements Lister {

		@Override
		public <E> List<E> element( E e ) {
			return this.<E> noElements().prepand( e );
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
		public <E> List<E> elements( ICluster<E> elems ) {
			if ( elems.isEmpty() ) {
				return noElements();
			}
			final int size = elems.size();
			if ( size == 1 ) {
				return element( elems.iterator().next() );
			}
			Object[] stack = new Object[StackList.nextHighestPowerOf2( size )];
			int index = stack.length - size;
			for ( E e : elems ) {
				stack[index++] = e;
			}
			return primary( size, stack, this.<E> noElements() );
		}

		@Override
		public <E> List<E> noElements() {
			return EmptyList.instance();
		}

	}

	final static class StackEnumListerFactory
			implements EnumeratorFactory {

		@Override
		public <E> Enumerator<E> enumerates( Enum<E> type ) {
			return new StackEnumLister<E>( type );
		}

	}

	static final class StackEnumLister<E>
			extends Enumerate.StepwiseEnumerator<E> {

		StackEnumLister( Enum<E> type ) {
			super( type );
		}

		@Override
		protected List<E> fromTo( E first, E last, Enum<E> type ) {
			int fo = type.toOrdinal( first );
			int lo = type.toOrdinal( last );
			if ( fo == lo ) { // length 1
				return InitList.LISTER.element( first );
			}
			int length = Math.abs( fo - lo ) + 1;
			if ( length == 2 ) {
				return InitList.LISTER.element( last ).prepand( first );
			}
			int capacity = 2;
			List<E> res = InitList.LISTER.noElements();
			E cur = last;
			final boolean asc = fo < lo;
			int size = 0;
			while ( size < length ) { // length will be > 2
				Object[] stack = new Object[capacity];
				int min = size + capacity < length
					? 0
					: capacity - ( length - size );
				for ( int i = capacity - 1; i >= min; i-- ) {
					stack[i] = cur;
					size++;
					if ( size < length ) {
						cur = asc
							? type.pred( cur )
							: type.succ( cur );
					}
				}
				res = new PrimaryStackList<E>( size, stack, res );
				capacity += capacity;
			}
			return res;
		}

	}

	private static final class PrimaryStackList<E>
			extends StackList<E> {

		PrimaryStackList( int size, Object[] stack, List<E> tail ) {
			super( size, stack, tail );
		}

		public List<E> prepand( E e ) {
			Nonnull.element( e );
			final int length = length();
			int index = occupationIndex( length );
			if ( index < 0 ) { // stack capacity exceeded
				return grow1( StackList.stack( e, stack.length * 2 ), this );
			}
			if ( prepandedOccupying( e, index ) ) {
				return grow1( stack, tail );
			}
			if ( length > stack.length / 2 ) {
				return grow1( StackList.stack( e, stack.length * 2 ), secondary( size, stack, tail ) );
			}
			Object[] copy = stackCopyFrom( index + 1, length );
			copy[index] = e;
			return grow1( copy, tail );
		}

		private int occupationIndex( final int length ) {
			return stack.length - 1 - length;
		}

		@Override
		public List<E> replaceAt( int index, E e ) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public List<E> tidyUp() {
			List<E> tidyTail = tail.tidyUp();
			int length = length();
			synchronized ( stack ) {
				if ( notOccupied( occupationIndex( length ) ) ) {
					return tidyTail == tail
						? this
						: primary( size, stack, tidyTail );
				}

			}
			return primary( size, stackCopyFrom( 0, length ), tail );
		}

		@SuppressWarnings ( "unchecked" )
		@Override
		final E element( int index, int l ) {
			return (E) stack[stack.length - l + index];
		}

		@Override
		final List<E> thisWith( int size, List<E> tail ) {
			return primary( size, stack, tail );
		}

		@Override
		final int offset() {
			return 0;
		}

		private List<E> grow1( Object[] stack, List<E> tail ) {
			return primary( size + 1, stack, tail );
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

		private boolean notOccupied( int index ) {
			return stack[index] == null;
		}

		private Object[] stackCopyFrom( int start, int length ) {
			Object[] copy = new Object[stack.length];
			System.arraycopy( stack, start, copy, start, length );
			return copy;
		}

	}

	static final class SecondaryStackList<E>
			extends StackList<E> {

		private final int offset;

		SecondaryStackList( int size, int offset, Object[] stack, List<E> tail ) {
			super( size, stack, tail );
			this.offset = offset;
		}

		@Override
		public List<E> deleteAt( int index ) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public List<E> prepand( E e ) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public List<E> replaceAt( int index, E e ) {
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
		E element( int index, int l ) {
			return (E) stack[stack.length - l + index - offset];
		}

		@Override
		List<E> thisWith( int size, List<E> tail ) {
			return secondary( size, offset, stack, tail );
		}

		@Override
		final int offset() {
			return offset;
		}

	}

}