package de.jbee.util;

import java.util.Comparator;

import de.jbee.lang.Eq;
import de.jbee.lang.Predicate;

public abstract class ExtendingList<T, E extends IList<T>>
		extends ExtendingBag<T, IList<T>, E>
		implements IList<T> {

	private static final long serialVersionUID = 1L;

	private final IList<T> list;

	protected ExtendingList( IList<T> list ) {
		this.list = list;
	}

	@Override
	protected final IList<T> getCollection() {
		return list;
	}

	@Override
	public T at( int index )
			throws IndexOutOfBoundsException {
		return getCollection().at( index );
	}

	@Override
	public T at( int index, T outOffBoundsValue ) {
		return getCollection().at( index, outOffBoundsValue );
	}

	@Override
	public E concat( IList<T> other ) {
		return extend( list );
	}

	@Override
	public IList<T> prepand( T e ) {
		return extend( list.prepand( e ) );
	}

	@Override
	public E delete( int index )
			throws IndexOutOfBoundsException {
		return extend( getCollection().delete( index ) );
	}

	@Override
	public E drop( IRange<? extends Number> indexRange ) {
		return extend( drop( indexRange ) );
	}

	@Override
	public E dropL( int firstN ) {
		return extend( dropL( firstN ) );
	}

	@Override
	public E dropR( int lastN ) {
		return extend( dropR( lastN ) );
	}

	@Override
	public E dropWhile( Predicate<? super T> stopCondition ) {
		return extend( dropWhile( stopCondition ) );
	}

	@Override
	public int elemIndex( T e ) {
		return getCollection().elemIndex( e );
	}

	@Override
	public int elemIndexBy( Eq<? super T> equality, T e ) {
		return getCollection().elemIndexBy( equality, e );
	}

	@Override
	public NumberList<Integer> elemIndicesBy( Eq<? super T> equality, T e ) {
		return getCollection().elemIndicesBy( equality, e );
	}

	@Override
	public int findIndex( Predicate<? super T> condition ) {
		return getCollection().findIndex( condition );
	}

	@Override
	public NumberList<Integer> findIndices( Predicate<? super T> condition ) {
		return getCollection().findIndices( condition );
	}

	@Override
	public <R> R foldL( IFunc1<R, T> foldFunction ) {
		return getCollection().foldL( foldFunction );
	}

	@Override
	public <R> R foldL( IFunc2<R, R, T> foldFunction, R inital ) {
		return getCollection().foldL( foldFunction, inital );
	}

	@Override
	public T foldL1( IFunc2<T, T, T> foldFunction )
			throws IndexOutOfBoundsException {
		return getCollection().foldL1( foldFunction );
	}

	@Override
	public T foldL1( IFunc2<T, T, T> foldFunction, T emptyValue ) {
		return getCollection().foldL1( foldFunction, emptyValue );
	}

	@Override
	public <R> R foldR( IFunc1<R, T> foldFunction ) {
		return getCollection().foldR( foldFunction );
	}

	@Override
	public <R> R foldR( IFunc2<R, R, T> foldFunction, R inital ) {
		return getCollection().foldR( foldFunction, inital );
	}

	@Override
	public T foldR1( IFunc2<T, T, T> foldFunction )
			throws IndexOutOfBoundsException {
		return getCollection().foldR1( foldFunction );
	}

	@Override
	public T foldR1( IFunc2<T, T, T> foldFunction, T emptyValue ) {
		return getCollection().foldR1( foldFunction, emptyValue );
	}

	@Override
	public T head()
			throws IndexOutOfBoundsException {
		return getCollection().head();
	}

	@Override
	public T head( T emptyValue ) {
		return getCollection().head( emptyValue );
	}

	@Override
	public E intersperse( T e ) {
		return extend( getCollection().intersperse( e ) );
	}

	@Override
	public boolean isPrefixOf( IList<T> other ) {
		return getCollection().isPrefixOf( other );
	}

	@Override
	public boolean isSuffixOf( IList<T> other ) {
		return getCollection().isSuffixOf( other );
	}

	@Override
	public T last()
			throws IndexOutOfBoundsException {
		return getCollection().last();
	}

	@Override
	public T last( T emptyValue ) {
		return getCollection().last( emptyValue );
	}

	@Override
	public <R> IList<R> map( IFunc1<R, T> mapFunction ) {
		return getCollection().map( mapFunction );
	}

	@Override
	public E replicate( int index, int length )
			throws IndexOutOfBoundsException {
		return extend( getCollection().replicate( index, length ) );
	}

	@Override
	public E reverse() {
		return extend( getCollection().reverse() );
	}

	@Override
	public E sort( Comparator<? super T> c ) {
		return extend( getCollection().sort( c ) );
	}

	@Override
	public Conditional<E> splitAt( int index )
			throws IndexOutOfBoundsException {
		return extend( getCollection().splitAt( index ) );
	}

	@Override
	public E tail() {
		return extend( getCollection().tail() );
	}

	@Override
	public E take( IRange<? extends Number> indexRange ) {
		return extend( getCollection().take( indexRange ) );
	}

	@Override
	public E takeL( int firstN ) {
		return extend( getCollection().takeL( firstN ) );
	}

	@Override
	public E takeR( int lastN ) {
		return extend( getCollection().takeR( lastN ) );
	}

	@Override
	public E takeWhile( Predicate<? super T> stopCondition ) {
		return extend( getCollection().takeWhile( stopCondition ) );
	}

	@Override
	public E zip( IList<? extends T> other ) {
		return extend( getCollection().zip( other ) );
	}

	@Override
	public IMutableList<T> mutable() {
		return List.mutable( list );
	}

	@Override
	public T[] toArray( Class<T> elementType ) {
		return Collection.asArray( this, elementType );
	}
}
