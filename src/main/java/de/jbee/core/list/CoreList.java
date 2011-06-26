package de.jbee.core.list;

import java.util.Iterator;

import de.jbee.core.IndexAccess;
import de.jbee.core.type.Enum;
import de.jbee.core.type.Enumerate;
import de.jbee.util.ICluster;

public final class CoreList {

	public static final EnumeratorFactory LISTER_FACTORY = new StackEnumListerFactory();
	public static final Lister LISTER = new StackLister();
	static final List<Object> EMPTY = new EmptyList<Object>();

	static void checkNonnull( Object e ) {
		if ( e == null ) {
			throw new NullPointerException( "null is not supported as an element of this list" );
		}
	}

	static <E> List<E> primary( int size, Object[] stack, List<E> tail ) {
		return new PrimaryStackList<E>( size, stack, tail );
	}

	static <E> List<E> secondary( int size, int offset, Object[] stack, List<E> tail ) {
		return new SecondaryStackList<E>( size, offset, stack, tail );
	}

	static <E> List<E> secondary( int size, Object[] stack, List<E> tail ) {
		return secondary( size, 0, stack, tail );
	}

	static <E> Object[] stack( E initial, int length ) {
		Object[] stack = new Object[length];
		stack[length - 1] = initial;
		return stack;
	}

	private CoreList() {
		throw new UnsupportedOperationException( "util" );
	}

	final static class EmptyList<E>
			implements List<E> {

		@Override
		public List<E> append( E e ) {
			return prepand( e );
		}

		@Override
		public E at( int index )
				throws IndexOutOfBoundsException {
			throw outOfBounds( index );
		}

		@Override
		public List<E> concat( List<E> other ) {
			return other;
		}

		@Override
		public List<E> deleteAt( int index ) {
			throw outOfBounds( index );
		}

		@Override
		public List<E> dropL( int beginning ) {
			return this;
		}

		@Override
		public List<E> dropR( int ending ) {
			return this;
		}

		@Override
		public List<E> insertAt( int index, E e ) {
			if ( index == 0 ) {
				return prepand( e );
			}
			throw outOfBounds( index );
		}

		@Override
		public boolean isEmpty() {
			return true;
		}

		@Override
		public Iterator<E> iterator() {
			throw new UnsupportedOperationException( "No elements to iterate" );
		}

		@Override
		public List<E> prepand( E e ) {
			checkNonnull( e );
			if ( e instanceof Integer ) {
				return integerList( e );
			}
			if ( e.getClass().isEnum() ) {
				return enumList( e );
			}
			return primary( 1, stack( e, 2 ), this );
		}

		@SuppressWarnings ( "unchecked" )
		private <T> List<E> enumList( E e ) {
			java.lang.Enum<?> v = (java.lang.Enum<?>) e;
			return (List<E>) enumList( v );
		}

		@SuppressWarnings ( "unchecked" )
		private <T extends java.lang.Enum<?>> List<T> enumList( T e ) {
			Class<? extends java.lang.Enum<?>> c = e.getDeclaringClass();
			return new EnumList<T>( (Enum<T>) Enumerate.type( c ), e, e );
		}

		@SuppressWarnings ( "unchecked" )
		private List<E> integerList( E e ) {
			Integer i = (Integer) e;
			return (List<E>) new EnumList<Integer>( Enumerate.INTEGERS, i, i );
		}

		@Override
		public List<E> replaceAt( int index, E e ) {
			throw outOfBounds( index );
		}

		@Override
		public int size() {
			return 0;
		}

		@Override
		public List<E> takeL( int beginning ) {
			return this;
		}

		@Override
		public List<E> takeR( int ending ) {
			return this;
		}

		@Override
		public List<E> tidyUp() {
			return this;
		}

		@Override
		public String toString() {
			return "[]";
		}

		private IndexOutOfBoundsException outOfBounds( int index ) {
			return new IndexOutOfBoundsException( "No such element: " + index );
		}
	}

	static abstract class StackList<E>
			implements List<E> {

		final int size; // in total with tail-lists
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
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public final E at( int index ) {
			final int length = length();
			return index >= length
				? tail.at( index - length )
				: element( index, length );
		}

		@Override
		public List<E> concat( List<E> other ) {
			return of( size + other.size(), tail.concat( other ) );
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
			return secondary( size - 1, length - index, stack, secondary( size - 1 - index, stack,
					tail ) );
		}

		@Override
		public List<E> dropL( int beginning ) {
			return takeR( size - beginning );
		}

		@Override
		public List<E> dropR( int ending ) {
			return takeL( size - ending );
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
		public final List<E> takeL( int beginning ) {
			if ( beginning <= 0 ) {
				return empty();
			}
			if ( beginning >= size ) {
				return this;
			}
			return nontrivialTakeL( beginning );
		}

		@Override
		public final List<E> takeR( int ending ) {
			if ( ending <= 0 ) {
				return empty();
			}
			if ( ending >= size ) {
				return this;
			}
			return nontrivialTakeR( ending );
		}

		@Override
		public String toString() {
			StringBuilder b = new StringBuilder();
			final int l = length();
			for ( int i = 0; i < l; i++ ) {
				b.append( ',' );
				b.append( String.valueOf( at( i ) ) );
			}
			return "[" + b.substring( 1 ) + "]:" + tail.toString();
		}

		abstract E element( int index, int l );

		final List<E> empty() {
			return LISTER.noElements();
		}

		/**
		 * @return The amount of elements dedicated to this lists stack
		 */
		final int length() {
			return size - tail.size();
		}

		abstract List<E> of( int size, List<E> tail );

		abstract int offset();

		/**
		 * keeps master when master and branch when branch list - change is in the tail. just
		 * replace the tail and reduce overall size by one. The elements in this stack remains since
		 * tail size is shrinking 1.
		 */
		final List<E> shrink1( List<E> tail ) {
			return of( size - 1, tail );
		}

		private List<E> nontrivialTakeL( int beginning ) {
			final int length = length();
			if ( beginning == length ) { // took this stack without tail
				return of( length, empty() );
			}
			if ( beginning < length ) { // took parts of this stack
				return secondary( beginning, ( length - beginning ) + offset(), stack, empty() );
			}
			// took this hole stack and parts of the tails elements
			return of( beginning, tail.takeL( beginning - length ) );
		}

		private List<E> nontrivialTakeR( int ending ) {
			final int tailSize = tail.size();
			return ending > tailSize
				? secondary( ending, stack, tail )
				: tail.takeR( ending );
		}
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
			// FIXME liste neu aufbauen indem direkt die element arrays erzeugt werden
			// dann entfällt auch das reverse - dazu muss man aber die size kennen
			List<E> res = List.reverse.from( this.<E> noElements() );
			for ( E e : elems ) {
				res = res.append( e );
			}
			return res;
		}

		@SuppressWarnings ( "unchecked" )
		@Override
		public <E> List<E> noElements() {
			return (List<E>) EMPTY;
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
			implements Enumerator<E> {

		private final Enum<E> type;

		StackEnumLister( Enum<E> type ) {
			super();
			this.type = type;
		}

		@Override
		public List<E> stepwiseFromTo( E first, E last, int increment ) {
			//TODO make sure start and end are in range of type
			return ( increment != 1 )
				? fromTo( first, alignLastToStep( first, last, increment ), Enumerate.stepwise(
						type, first, increment ) )
				: fromTo( first, last, type );
		}

		private E alignLastToStep( E first, E last, int inc ) {
			int lo = type.toOrdinal( last );
			return type.toEnum( lo - ( ( lo - type.toOrdinal( first ) ) % inc ) );
		}

		private List<E> fromTo( E first, E last, Enum<E> type ) {
			int fo = type.toOrdinal( first );
			int lo = type.toOrdinal( last );
			if ( fo == lo ) { // length 1
				return LISTER.element( first );
			}
			int length = Math.abs( fo - lo ) + 1;
			if ( length == 2 ) {
				return LISTER.element( last ).prepand( first );
			}
			int capacity = 2;
			List<E> res = LISTER.noElements();
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
			checkNonnull( e );
			final int length = length();
			int index = stack.length - 1 - length;
			if ( index < 0 ) { // stack capacity exceeded
				return grow1( stack( e, stack.length * 2 ), this );
			}
			if ( prepandedOccupying( e, index ) ) {
				return grow1( stack, tail );
			}
			if ( length > stack.length / 2 ) {
				return grow1( stack( e, stack.length * 2 ), secondary( size, stack, tail ) );
			}
			Object[] copy = stackCopyFrom( index + 1, length );
			copy[index] = e;
			return grow1( copy, tail );
		}

		@Override
		public List<E> replaceAt( int index, E e ) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public List<E> tidyUp() {
			List<E> tidyTail = tail.tidyUp();
			// FIXME ensure that next prepand index is still unused
			return tidyTail == tail
				? this
				: primary( size, stack, tidyTail );
		}

		@SuppressWarnings ( "unchecked" )
		@Override
		final E element( int index, int l ) {
			return (E) stack[stack.length - l + index];
		}

		@Override
		final List<E> of( int size, List<E> tail ) {
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
				if ( stack[index] == null ) {
					stack[index] = e;
					return true;
				}
				return false;
			}
		}

		private Object[] stackCopyFrom( int start, int length ) {
			Object[] copy = new Object[stack.length];
			System.arraycopy( stack, start, copy, start, length );
			return copy;
		}

	}

	private static final class SecondaryStackList<E>
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
		List<E> of( int size, List<E> tail ) {
			return secondary( size, offset, stack, tail );
		}

		@Override
		final int offset() {
			return offset;
		}

	}

	public static final class EnumList<E>
			implements List<E> {

		private final List<E> tail;
		private final Enum<E> type;
		private final int firstOrdinal;
		private final int lastOrdinal;

		EnumList( Enum<E> type, E first, E last ) {
			this( type, type.toOrdinal( first ), type.toOrdinal( last ), LISTER.<E> noElements() );
		}

		EnumList( Enum<E> type, int firstOrdianl, int lastOrdinal, List<E> tail ) {
			super();
			this.type = type;
			this.tail = tail;
			this.firstOrdinal = firstOrdianl;
			this.lastOrdinal = lastOrdinal;
		}

		@Override
		public List<E> append( E e ) {
			if ( tail.isEmpty() ) {
				final int nextOrdinal = lastPlus( 1 );
				if ( type.toOrdinal( e ) == nextOrdinal ) {
					return list( firstOrdinal, nextOrdinal );
				}
			}
			return thisWithTail( tail.append( e ) );
		}

		@Override
		public E at( int index ) {
			final int l = length();
			return index >= l
				? tail.at( index - l )
				: type.toEnum( firstPlus( index ) );
		}

		@Override
		public List<E> concat( List<E> other ) {
			if ( !tail.isEmpty() ) {
				return tail.concat( other );
			}
			if ( other instanceof EnumList<?> ) {
				EnumList<E> o = (EnumList<E>) other;
				if ( ascending() == o.ascending() && o.firstOrdinal == lastPlus( 1 ) ) {
					return list( firstOrdinal, o.lastOrdinal );
				}
			}
			return thisWithTail( other );
		}

		@Override
		public List<E> deleteAt( int index ) {
			final int length = length();
			if ( index >= length ) { // its in the tail
				return thisWithTail( tail.deleteAt( index - length ) );
			}
			if ( index == 0 ) { // first of this enum list
				return length == 1
					? tail
					: list( firstPlus( 1 ), lastOrdinal );
			}
			if ( index == length - 1 ) { // last of this enum list
				return length == 1
					? tail
					: list( firstOrdinal, lastMinus( 1 ) );
			}
			// sadly: in between this list
			final int indexOrdinal = type.toOrdinal( at( index ) );
			return list( firstOrdinal, ordinalMinus( indexOrdinal, 1 ), list( ordinalPlus(
					indexOrdinal, 1 ), lastOrdinal, tail ) );
		}

		@Override
		public List<E> dropL( int beginning ) {
			final int size = size();
			if ( beginning >= size ) {
				return empty();
			}
			final int length = length();
			if ( beginning == length ) {
				return tail;
			}
			if ( beginning > length ) {
				return tail.dropL( beginning - length );
			}
			return list( firstPlus( beginning ), lastOrdinal );
		}

		@Override
		public List<E> dropR( int ending ) {
			final int size = size();
			if ( ending >= size ) {
				return empty();
			}
			final int length = length();
			final int tailSize = size - length;
			if ( tailSize == ending ) {
				return thisWithTail( empty() );
			}
			if ( ending < tailSize ) {
				return thisWithTail( tail.dropR( ending ) );
			}
			return list( firstOrdinal, lastMinus( ending - tailSize ), empty() );
		}

		@Override
		public List<E> insertAt( int index, E e ) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public boolean isEmpty() {
			return false;
		}

		@Override
		public Iterator<E> iterator() {
			return IndexAccess.iterator( this, 0, size() );
		}

		@Override
		public List<E> prepand( E e ) {
			int priorOrdinal = firstMinus( 1 );
			final int eOrdinal = type.toOrdinal( e );
			if ( eOrdinal == priorOrdinal ) {
				return list( priorOrdinal, lastOrdinal );
			}
			final int size = size();
			if ( size == 1 ) {
				priorOrdinal = firstPlus( 1 );
				return priorOrdinal == eOrdinal
					? list( eOrdinal, lastOrdinal )
					: primary( 1, stack( at( 0 ), 2 ), empty() ).prepand( e );
			}
			if ( length() == 1 ) {
				return primary( 1 + tail.size(), stack( at( 0 ), 2 ), tail ).prepand( e );
			}
			return list( eOrdinal, eOrdinal, this );
		}

		@Override
		public List<E> replaceAt( int index, E e ) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public int size() {
			return length() + tail.size();
		}

		@Override
		public List<E> takeL( int beginning ) {
			if ( beginning >= size() ) {
				return this;
			}
			final int length = length();
			if ( beginning == length ) {
				return thisWithTail( empty() );
			}
			return beginning <= length
				? list( firstOrdinal, firstPlus( beginning - 1 ), empty() )
				: thisWithTail( tail.takeL( beginning - length ) );
		}

		@Override
		public List<E> takeR( int ending ) {
			final int size = size();
			if ( ending >= size ) {
				return this;
			}
			final int tailSize = size - length();
			if ( tailSize == ending ) {
				return tail;
			}
			return tailSize > ending
				? tail.takeR( ending )
				: list( lastMinus( ( ending - 1 - tailSize ) ), lastOrdinal );
		}

		@Override
		public List<E> tidyUp() {
			List<E> tidyTail = tail.tidyUp();
			return tidyTail == tail
				? this
				: thisWithTail( tidyTail );
		}

		@Override
		public String toString() {
			return "[" + String.valueOf( type.toEnum( firstOrdinal ) ) + ".."
					+ String.valueOf( type.toEnum( lastOrdinal ) ) + "]:" + tail.toString();
		}

		private boolean ascending() {
			return firstOrdinal <= lastOrdinal;
		}

		private int lastMinus( int dec ) {
			return ordinalMinus( lastOrdinal, dec );
		}

		private int lastPlus( int inc ) {
			return ordinalPlus( lastOrdinal, inc );
		}

		/**
		 * @return The amount of elements in this enumerated list (*not* considering the
		 *         {@link #tail}s size).
		 */
		private int length() {
			return Math.abs( lastOrdinal - firstOrdinal ) + 1;
		}

		private List<E> list( int firstOrdinal, int lastOrdinal ) {
			return new EnumList<E>( type, firstOrdinal, lastOrdinal, tail );
		}

		private List<E> list( int firstOrdianl, int lastOrdinal, List<E> tail ) {
			return new EnumList<E>( type, firstOrdianl, lastOrdinal, tail );
		}

		private List<E> empty() {
			return LISTER.noElements();
		}

		private int ordinalMinus( int ordinal, int dec ) {
			return ordinalPlus( ordinal, -dec );
		}

		private int ordinalPlus( int ordinal, int inc ) {
			return ascending()
				? ordinal + inc
				: ordinal - inc;
		}

		private int firstMinus( int dec ) {
			return ordinalMinus( firstOrdinal, dec );
		}

		private int firstPlus( int inc ) {
			return ordinalPlus( firstOrdinal, inc );
		}

		private List<E> thisWithTail( List<E> tail ) {
			return new EnumList<E>( type, firstOrdinal, lastOrdinal, tail );
		}
	}
}