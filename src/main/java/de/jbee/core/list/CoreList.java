package de.jbee.core.list;

import java.util.Iterator;

import de.jbee.core.IndexAccess;
import de.jbee.core.type.Enum;
import de.jbee.core.type.Enumerate;

public final class CoreList {

	public static final EnumeratorFactory LISTER_FACTORY = new StackEnumListerFactory();
	public static final Lister LISTER = new StackLister();
	static final List<Object> EMPTY = new EmptyStackList<Object>();

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

	final static class EmptyStackList<E>
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
			for ( int i = 0; i < size; i++ ) {
				b.append( ',' );
				b.append( String.valueOf( at( i ) ) );
			}
			return "[" + ( b.length() == 0
				? ""
				: b.substring( 1 ) ) + "]";
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
		public <E> List<E> elements( Iterable<E> elems ) {
			// FIXME liste neu aufbauen indem direkt die element arrays erzeugt werden
			// dann entfällt auch das reverse
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
		public List<E> stepwiseFromTo( E start, E end, int increment ) {
			//TODO make sure start and end are in range of type
			return ( increment != 1 )
				? fromTo( start, alignEndToStep( start, end, increment ), Enumerate.stepwise( type,
						start, increment ) )
				: fromTo( start, end, type );
		}

		private E alignEndToStep( E start, E end, int inc ) {
			int eo = type.toOrdinal( end );
			return type.toEnum( eo - ( ( eo - type.toOrdinal( start ) ) % inc ) );
		}

		//FIXME through prepanding start is violated not end in case of stepswise - change
		private List<E> fromTo( E start, E end, Enum<E> type ) {
			int si = type.toOrdinal( start );
			int ei = type.toOrdinal( end );
			if ( si == ei ) { // length 1
				return LISTER.element( start );
			}
			int length = Math.abs( si - ei ) + 1;
			if ( length == 2 ) {
				return LISTER.element( end ).prepand( start );
			}
			int capacity = 2;
			List<E> res = LISTER.noElements();
			E cur = end;
			final boolean asc = si < ei;
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

		@Override
		public List<E> concat( List<E> other ) {
			// TODO Auto-generated method stub
			return null;
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
		public List<E> concat( List<E> other ) {
			// TODO Auto-generated method stub
			return null;
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
		private final int startOrdinal;
		private final int endOrdinal;

		EnumList( Enum<E> type, E start, E end ) {
			this( type, type.toOrdinal( start ), type.toOrdinal( end ), List.with.<E> noElements() );
		}

		EnumList( Enum<E> type, int startOrdianl, int endOrdinal, List<E> tail ) {
			super();
			this.type = type;
			this.tail = tail;
			this.startOrdinal = startOrdianl;
			this.endOrdinal = endOrdinal;
		}

		@Override
		public List<E> append( E e ) {
			if ( tail.isEmpty() ) {
				final int nextOrdinal = endPlus( 1 );
				if ( type.toOrdinal( e ) == nextOrdinal ) {
					return list( startOrdinal, nextOrdinal );
				}
			}
			return thisWithTail( tail.append( e ) );
		}

		@Override
		public E at( int index ) {
			final int l = length();
			return index >= l
				? tail.at( index - l )
				: type.toEnum( startPlus( index ) );
		}

		@Override
		public List<E> concat( List<E> other ) {
			if ( !tail.isEmpty() ) {
				return tail.concat( other );
			}
			if ( other instanceof EnumList<?> ) {
				EnumList<E> o = (EnumList<E>) other;
				if ( ascending() == o.ascending() && o.startOrdinal == endPlus( 1 ) ) {
					return list( startOrdinal, o.endOrdinal );
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
					: list( startPlus( 1 ), endOrdinal );
			}
			if ( index == length - 1 ) { // last of this enum list
				return length == 1
					? tail
					: list( startOrdinal, endMinus( 1 ) );
			}
			// sadly: in between this list
			final int indexOrdinal = type.toOrdinal( at( index ) );
			return list( startOrdinal, ordinalMinus( indexOrdinal, 1 ), list( ordinalPlus(
					indexOrdinal, 1 ), endOrdinal, tail ) );
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
			return list( startPlus( beginning ), endOrdinal );
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
			return list( startOrdinal, endMinus( ending - tailSize ), empty() );
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
			int priorOrdinal = startMinus( 1 );
			final int eOrdinal = type.toOrdinal( e );
			if ( eOrdinal == priorOrdinal ) {
				return list( priorOrdinal, endOrdinal );
			}
			final int size = size();
			if ( size == 1 ) {
				priorOrdinal = startPlus( 1 );
				return priorOrdinal == eOrdinal
					? list( eOrdinal, endOrdinal )
					: primary( 1, stack( at( 0 ), 2 ), empty() ).prepand( e );
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
				? list( startOrdinal, startPlus( beginning - 1 ) )
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
			return list( endMinus( ( ending - 1 - tailSize ) ), endOrdinal );
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
			return "[" + String.valueOf( type.toEnum( startOrdinal ) ) + ".."
					+ String.valueOf( type.toEnum( endOrdinal ) ) + "]:" + tail.toString();
		}

		private boolean ascending() {
			return startOrdinal <= endOrdinal;
		}

		private int endMinus( int dec ) {
			return ordinalMinus( endOrdinal, dec );
		}

		private int endPlus( int inc ) {
			return ordinalPlus( endOrdinal, inc );
		}

		/**
		 * @return The amount of elements in this enumerated list (*not* considering the
		 *         {@link #tail}s size).
		 */
		private int length() {
			return Math.abs( endOrdinal - startOrdinal ) + 1;
		}

		private List<E> list( int startOrdinal, int endOrdinal ) {
			return new EnumList<E>( type, startOrdinal, endOrdinal, tail );
		}

		private List<E> list( int startOrdianl, int endOrdinal, List<E> tail ) {
			return new EnumList<E>( type, startOrdianl, endOrdinal, tail );
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

		private int startMinus( int dec ) {
			return ordinalMinus( startOrdinal, dec );
		}

		private int startPlus( int inc ) {
			return ordinalPlus( startOrdinal, inc );
		}

		private List<E> thisWithTail( List<E> tail ) {
			return new EnumList<E>( type, startOrdinal, endOrdinal, tail );
		}
	}
}
