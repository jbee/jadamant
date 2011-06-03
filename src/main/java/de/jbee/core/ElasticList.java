package de.jbee.core;

import java.util.Arrays;
import java.util.Iterator;

public final class ElasticList {

	private static final IElasticList<Object> EMPTY = new EmptyList<Object>();

	@SuppressWarnings ( "unchecked" )
	public static <E> IElasticList<E> emptyForPrepanding() {
		return (IElasticList<E>) EMPTY;
	}

	public static <E> IElasticList<E> emptyForAppending() {
		return reverse( ElasticList.<E> emptyForPrepanding() );
	}

	public static <E> IElasticList<E> reverse( IElasticList<E> beingReversed ) {
		return new ReversingList<E>( beingReversed );
	}

	static <E> Object[] stackHaving( E first, int length ) {
		Object[] s = new Object[length];
		s[length - 1] = first;
		return s;
	}

	private ElasticList() {
		throw new UnsupportedOperationException( "util" );
	}

	private static final class BranchVList<E>
			extends VList<E> {

		private final int offset;

		BranchVList( int size, Object[] stack, IElasticList<E> tail ) {
			this( size, 0, stack, tail );
		}

		BranchVList( int size, int offset, Object[] stack, IElasticList<E> tail ) {
			super( size, stack, tail );
			this.offset = offset;
		}

		@Override
		public IElasticList<E> prepand( E e ) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public IElasticList<E> takeL( int beginning ) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public IElasticList<E> takeR( int ending ) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public IElasticList<E> tidyUp() {
			// TODO Auto-generated method stub
			return null;
		}

	}

	private final static class EmptyList<E>
			implements IElasticList<E> {

		@Override
		public IElasticList<E> append( E e ) {
			return prepand( e );
		}

		@Override
		public IElasticList<E> takeL( int beginning ) {
			return this;
		}

		@Override
		public IElasticList<E> takeR( int ending ) {
			return this;
		}

		@Override
		public E at( int index )
				throws IndexOutOfBoundsException {
			throw outOfBounds( index );
		}

		@Override
		public IElasticList<E> delete( int index ) {
			throw outOfBounds( index );
		}

		@Override
		public IElasticList<E> insert( E e, int index ) {
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
		public IElasticList<E> prepand( E e ) {
			return new MasterVList<E>( 1, stackHaving( e, 2 ), this );
		}

		@Override
		public int size() {
			return 0;
		}

		@Override
		public IElasticList<E> tidyUp() {
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

	private static class MasterVList<E>
			extends VList<E> {

		MasterVList( int size, Object[] stack, IElasticList<E> tail ) {
			super( size, stack, tail );
		}

		@Override
		public IElasticList<E> takeL( int beginning ) {
			if ( beginning >= size ) {
				return this;
			}
			final int length = length();
			if ( beginning == length ) {
				return new MasterVList<E>( length, stack, empty() );
			}
			if ( beginning < length ) {
				return new BranchVList<E>( beginning, 0, stack, empty() );
			}
			return new MasterVList<E>( beginning, stack, tail.takeL( beginning - length ) );
		}

		@Override
		public IElasticList<E> takeR( int ending ) {
			if ( ending >= size ) {
				return this;
			}
			final int tailSize = tail.size();
			if ( ending > tailSize ) {
				return new BranchVList<E>( size - ( ending - tailSize ), stack, tail );
			}
			return tail.takeR( ending );
		}

		public IElasticList<E> prepand( E e ) {
			final int length = length();
			int index = stack.length - 1 - length;
			if ( index < 0 ) { // full stack
				return grownStack( e, this );
			}
			if ( prepandedOccupying( e, index ) ) {
				return grown1( stack );
			}
			if ( length > stack.length / 2 ) {
				return grownStack( e, new BranchVList<E>( size, stack, tail ) );
			}
			Object[] copy = stackCopyFrom( index + 1, length );
			copy[index] = e;
			return grown1( copy );
		}

		private Object[] stackCopyFrom( int start, int length ) {
			Object[] copy = new Object[stack.length];
			System.arraycopy( stack, start, copy, start, length );
			return copy;
		}

		@Override
		public IElasticList<E> tidyUp() {
			IElasticList<E> tidyTail = tail.tidyUp();
			return tidyTail == tail
				? this
				: new MasterVList<E>( size, stack, tidyTail );
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

		private IElasticList<E> grownStack( E e, IElasticList<E> tail ) {
			return new MasterVList<E>( size + 1, stackHaving( e, stack.length * 2 ), tail );
		}

	}

	final static class ReversingList<E>
			implements IElasticList<E> {

		private final IElasticList<E> list;

		ReversingList( IElasticList<E> beingReversed ) {
			super();
			this.list = beingReversed;
		}

		@Override
		public IElasticList<E> append( E e ) {
			return reverseViewOf( list.prepand( e ) );
		}

		@Override
		public E at( int index )
				throws IndexOutOfBoundsException {
			return list.at( reverseIndexOf( index ) );
		}

		@Override
		public IElasticList<E> delete( int index ) {
			return reverseViewOf( list.delete( reverseIndexOf( index ) ) );
		}

		@Override
		public IElasticList<E> insert( E e, int index ) {
			return reverseViewOf( list.insert( e, reverseIndexOf( index ) ) );
		}

		@Override
		public boolean isEmpty() {
			return list.isEmpty();
		}

		@Override
		public Iterator<E> iterator() {
			return IndexAccess.reverseIterator( list, 0, size() );
		}

		@Override
		public IElasticList<E> prepand( E e ) {
			return reverseViewOf( list.append( e ) );
		}

		@Override
		public IElasticList<E> takeL( int beginning ) {
			return reverseViewOf( list.takeR( beginning ) );
		}

		@Override
		public IElasticList<E> takeR( int ending ) {
			return reverseViewOf( list.takeL( ending ) );
		}

		@Override
		public int size() {
			return list.size();
		}

		@Override
		public IElasticList<E> tidyUp() {
			return reverseViewOf( list.tidyUp() );
		}

		private ReversingList<E> reverseViewOf( IElasticList<E> l ) {
			return l == list
				? this
				: new ReversingList<E>( l );
		}

		private int reverseIndexOf( int index ) {
			return list.size() - 1 - index;
		}

	}

	private static abstract class VList<E>
			implements IElasticList<E> {

		final int size; // in total with tail-lists
		final Object[] stack;
		final IElasticList<E> tail;

		VList( int size, Object[] stack, IElasticList<E> tail ) {
			super();
			this.size = size;
			this.stack = stack;
			this.tail = tail;
		}

		@Override
		public IElasticList<E> append( E e ) {
			return null;
		}

		final IElasticList<E> empty() {
			return ElasticList.emptyForPrepanding();
		}

		@Override
		public final E at( int index )
				throws IndexOutOfBoundsException {
			final int length = length();
			if ( index >= length ) {
				return tail.at( index - length );
			}
			return element( index, length );
		}

		@Override
		public IElasticList<E> delete( int index ) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public IElasticList<E> insert( E e, int index ) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public final boolean isEmpty() {
			return size == 0;
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
		public String toString() {
			return Arrays.toString( stack ) + "..." + tail.toString();
		}

		/**
		 * @return The amount of elements dedicated to this lists stack
		 */
		final int length() {
			return size - tail.size();
		}

		@SuppressWarnings ( "unchecked" )
		private E element( int index, int l ) {
			return (E) stack[stack.length - l + index];
		}

		protected final IElasticList<E> grown1( Object[] stack ) {
			return new MasterVList<E>( size + 1, stack, tail );
		}
	}
}
