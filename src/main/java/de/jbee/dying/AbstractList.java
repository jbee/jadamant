package de.jbee.dying;

import java.util.ArrayList;
import java.util.Iterator;

import de.jbee.lang.Eq;
import de.jbee.lang.Fulfills;
import de.jbee.lang.Predicate;

public abstract class AbstractList<T, M extends Iterable<T>>
		extends AbstractBag<T, List<T>, M>
		implements List<T> {

	private static final long serialVersionUID = 1L;

	/**
	 * Receives the element at the given index. That index is already validated so that access to a
	 * illegal index must not be checked further more.
	 */
	protected abstract T atValidated( int index );

	/**
	 * If there is a element at the given index that element is deleted. Otherwise this list was
	 * returned.
	 */
	protected final List<T> deleteInBounds( int index ) {
		return isValidIndex( index )
			? delete( index )
			: this;
	}

	/**
	 * A empty immutable List.
	 * 
	 * @see ListUtil#empty()
	 */
	@Override
	protected final List<T> empty() {
		return ListUtil.empty();
	}

	@Override
	protected List<T> self() {
		return this;
	}

	protected final boolean isValidIndex( int index ) {
		return index >= 0 && index < size();
	}

	/**
	 * A {@link Iterator} starts at the last element. A call to {@link Iterator#next()} steps to the
	 * previous element of the current element. The method {@link Iterator#hasNext()} determines if
	 * there is a previous element in front of the current one.
	 */
	protected abstract Iterator<T> reverseIterator();

	/**
	 * Returns a mutable sublist of this list with the given range.
	 */
	protected abstract M sublist( int fromInclusive, int toExclusive );

	protected final void validate( int index ) {
		if ( !isValidIndex( index ) ) {
			throw new IndexOutOfBoundsException( "Index has to be in range 0 to " + size()
					+ " but was: " + index );
		}
	}

	@Override
	public final T at( int index )
			throws IndexOutOfBoundsException {
		validate( index );
		return atValidated( index );
	}

	@Override
	public T at( int index, T outOffBoundsValue ) {
		return isValidIndex( index )
			? atValidated( index )
			: outOffBoundsValue;
	}

	@Override
	public List<T> concat( List<T> other ) {
		if ( other.isEmpty() ) {
			return this;
		}
		if ( isEmpty() ) {
			return other;
		}
		final M list = clone( other.size() );
		for ( final T e : other ) {
			add( list, e );
		}
		return readonly( list );
	}

	@Override
	public List<T> prepand( T e ) {
		throw new UnsupportedOperationException( "Not now" );
	}

	@Override
	public List<T> delete( int index ) {
		validate( index );
		return drop( Range.between( index, index + 1 ) );
	}

	@Override
	public List<T> delete( T e ) {
		return deleteInBounds( elemIndex( e ) );
	}

	@Override
	public List<T> deleteBy( Eq<? super T> equality, T e ) {
		return deleteInBounds( elemIndexBy( equality, e ) );
	}

	@Override
	public List<T> drop( IRange<? extends Number> indexRange ) {
		final int size = size();
		final int start = indexRange.start().intValue();
		if ( start >= size ) {
			return this;
		}
		final int end = indexRange.end().intValue();
		if ( start == 0 ) {
			return takeR( size - end );
		}
		if ( end >= size ) {
			return takeL( start );
		}
		return takeL( start ).concat( dropL( end + 1 ) );
	}

	@Override
	public List<T> dropL( int firstN ) {
		if ( size() <= firstN ) {
			return empty();
		}
		return readonly( sublist( firstN, size() ) );
	}

	@Override
	public List<T> dropR( int lastN ) {
		if ( size() <= lastN ) {
			return empty();
		}
		return readonly( sublist( 0, size() - lastN ) );
	}

	@Override
	public List<T> dropWhile( Predicate<? super T> stopCondition ) {
		final M list = empty( size() );
		for ( final T e : this ) {
			if ( !stopCondition.fulfilledBy( e ) ) {
				add( list, e );
			}
		}
		return readonly( list );
	}

	@Override
	public int elemIndex( T e ) {
		return elemIndexBy( Equal.equals(), e );
	}

	@Override
	public int elemIndexBy( Eq<? super T> equality, T e ) {
		int i = 0;
		for ( final T o : this ) {
			if ( equality.holds( o, e ) ) {
				return i;
			}
			i++;
		}
		return -1;
	}

	@Override
	public NumberList<Integer> elemIndicesBy( Eq<? super T> equality, T e ) {
		return findIndices( Fulfills.equality( equality, e ) );
	}

	@Override
	public boolean equals( Object obj ) {
		if ( obj instanceof List<?> ) {
			final Iterator<?> i = ( (List<?>) obj ).iterator();
			for ( final T e : this ) {
				final Object o = i.next();
				if ( ( e == null && o == null ) || ( e != null && e.equals( o ) ) ) {
					return false;
				}
			}
		}
		return true;
	}

	@Override
	public int findIndex( Predicate<? super T> condition ) {
		int i = 0;
		for ( final T e : this ) {
			if ( condition.fulfilledBy( e ) ) {
				return i;
			}
			i++;
		}
		return -1;
	}

	@Override
	public NumberList<Integer> findIndices( Predicate<? super T> condition ) {
		final java.util.List<Integer> indices = new ArrayList<Integer>( size() );
		int i = 0;
		for ( final T e : this ) {
			if ( condition.fulfilledBy( e ) ) {
				indices.add( i );
			}
			i++;
		}
		return ListUtil.readonly( indices );
	}

	@Override
	public <R> R foldL( IFunc1<R, T> foldFunction ) {
		R r = null;
		for ( final T e : this ) {
			r = foldFunction.exec( e );
		}
		return r;
	}

	@Override
	public <R> R foldL( IFunc2<R, R, T> foldFunction, R initial ) {
		R r = initial;
		for ( final T e : this ) {
			r = foldFunction.exec( r, e );
		}
		return r;
	}

	@Override
	public T foldL1( IFunc2<T, T, T> foldFunction ) {
		if ( size() <= 1 ) {
			return head();
		}
		final Iterator<T> i = iterator();
		T e = foldFunction.exec( i.next(), i.next() );
		while ( i.hasNext() ) {
			e = foldFunction.exec( e, i.next() );
		}
		return e;
	}

	@Override
	public <R> R foldR( IFunc1<R, T> foldFunction ) {
		R r = null;
		final Iterator<T> i = reverseIterator();
		while ( i.hasNext() ) {
			r = foldFunction.exec( i.next() );
		}
		return r;
	}

	@Override
	public <R> R foldR( IFunc2<R, R, T> foldFunction, R inital ) {
		R r = inital;
		final Iterator<T> i = reverseIterator();
		while ( i.hasNext() ) {
			r = foldFunction.exec( r, i.next() );
		}
		return r;
	}

	@Override
	public T foldR1( IFunc2<T, T, T> foldFunction ) {
		if ( size() <= 1 ) {
			return head();
		}
		final Iterator<T> i = reverseIterator();
		final T last = i.next();
		T e = foldFunction.exec( i.next(), last );
		while ( i.hasNext() ) {
			e = foldFunction.exec( i.next(), e );
		}
		return e;
	}

	@Override
	public T head() {
		return at( 0 );
	}

	@Override
	public final T head( T emptyValue ) {
		return isEmpty()
			? emptyValue
			: head();
	}

	@Override
	public List<T> intersperse( T e ) {
		if ( isEmpty() ) {
			return this;
		}
		final M list = empty( size() * 2 - 1 );
		final Iterator<T> i = iterator();
		while ( i.hasNext() ) {
			add( list, i.next() );
			if ( i.hasNext() ) {
				add( list, e );
			}
		}
		return readonly( list );
	}

	@Override
	public boolean isPrefixOf( List<T> other ) {
		return other.takeL( size() ).isEqual( this );
	}

	@Override
	public boolean isSuffixOf( List<T> other ) {
		return other.takeR( size() ).isEqual( this );
	}

	@Override
	public T last() {
		return at( size() - 1 );
	}

	@Override
	public final T last( T emptyValue ) {
		return isEmpty()
			? emptyValue
			: last();
	}

	@Override
	public <R> List<R> map( IFunc1<R, T> mapFunction ) {
		final java.util.List<R> list = new ArrayList<R>( size() );
		for ( final T e : this ) {
			list.add( mapFunction.exec( e ) );
		}
		return ListUtil.readonly( list );
	}

	@Override
	public List<T> replicate( int index, int length ) {
		validate( index );
		final M list = empty( length );
		final T e = at( index );
		for ( int i = 0; i < length; i++ ) {
			add( list, e );
		}
		return readonly( list );
	}

	@Override
	public List<T> reverse() {
		final M list = empty( size() );
		final Iterator<T> i = reverseIterator();
		while ( i.hasNext() ) {
			add( list, i.next() );
		}
		return readonly( list );
	}

	@Override
	public Conditional<List<T>> splitAt( int index ) {
		validate( index );
		final int size = size();
		final M positives = empty( index );
		final M negatives = empty( size - index );
		int i = 0;
		for ( final T e : this ) {
			if ( i < index ) {
				add( positives, e );
			} else {
				add( negatives, e );
			}
			i++;
		}
		return Conditional.<List<T>> of( readonly( positives ), readonly( negatives ) );
	}

	@Override
	public List<T> tail() {
		return dropL( 1 );
	}

	@Override
	public List<T> take( IRange<? extends Number> indexRange ) {
		final int start = indexRange.start().intValue();
		if ( indexRange.isEmpty() || start >= size() ) {
			return empty();
		}
		return readonly( sublist( start, Math.min( size(), indexRange.end().intValue() ) ) );
	}

	@Override
	public List<T> takeL( int firstN ) {
		if ( firstN <= 0 ) {
			return empty();
		}
		return readonly( sublist( 0, firstN ) );
	}

	@Override
	public List<T> takeR( int lastN ) {
		if ( lastN <= 0 ) {
			return empty();
		}
		final int size = size();
		return readonly( sublist( size - lastN, size ) );
	}

	@Override
	public List<T> takeWhile( Predicate<? super T> stopCondition ) {
		if ( isEmpty() ) {
			return this;
		}
		final M list = empty( size() );
		for ( final T e : this ) {
			if ( !stopCondition.fulfilledBy( e ) ) {
				add( list, e );
			}
		}
		return readonly( list );
	}

	@Override
	public List<T> zip( List<? extends T> other ) {
		if ( other.isEmpty() ) {
			return this;
		}
		if ( isEmpty() ) {
			@SuppressWarnings ( "unchecked" )
			final List<T> list = (List<T>) other;
			return list;
		}
		final Iterator<T> i = iterator();
		final Iterator<? extends T> j = other.iterator();
		final M list = empty( size() + other.size() );
		while ( i.hasNext() && j.hasNext() ) {
			add( list, i.next() );
			add( list, j.next() );
		}
		while ( i.hasNext() ) {
			add( list, i.next() );
		}
		while ( j.hasNext() ) {
			add( list, j.next() );
		}
		return readonly( list );
	}

	@Override
	public final T foldL1( IFunc2<T, T, T> foldFunction, T emptyValue ) {
		return isEmpty()
			? emptyValue
			: foldL1( foldFunction );
	}

	@Override
	public final T foldR1( IFunc2<T, T, T> foldFunction, T emptyValue ) {
		return isEmpty()
			? emptyValue
			: foldR1( foldFunction );
	}

	@Override
	public IMutableList<T> mutable() {
		return ListUtil.mutable( self() );
	}

	@Override
	public T[] toArray( Class<T> elementType ) {
		return Collection.asArray( this, elementType );
	}
}
