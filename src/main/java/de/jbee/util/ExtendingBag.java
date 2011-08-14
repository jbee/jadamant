package de.jbee.util;

import java.util.Comparator;

import de.jbee.lang.Predicate;

public abstract class ExtendingBag<T, C extends IBag<T, C>, E extends C>
		extends ExtendingCollection<T, C, E>
		implements IBag<T, C> {

	@Override
	public boolean isEqual( C other ) {
		return getCollection().isEqual( other );
	}

	@Override
	public boolean isEqualBy( IEquality<? super T> equality, C other ) {
		return getCollection().isEqualBy( equality, other );
	}

	@Override
	public int count( T e ) {
		return getCollection().count( e );
	}

	@Override
	public int countBy( IEquality<? super T> equality, T e ) {
		return getCollection().countBy( equality, e );
	}

	@Override
	public E delete( T e ) {
		return extend( getCollection().delete( e ) );
	}

	@Override
	public E deleteBy( IEquality<? super T> equality, T e ) {
		return extend( getCollection().deleteBy( equality, e ) );
	}

	@Override
	public E difference( ICluster<T> other ) {
		return extend( getCollection().difference( other ) );
	}

	@Override
	public E differenceBy( IEquality<? super T> equality, ICluster<T> other ) {
		return extend( getCollection().differenceBy( equality, other ) );
	}

	@Override
	public E filter( Predicate<? super T> filterFunction ) {
		return extend( getCollection().filter( filterFunction ) );
	}

	@Override
	public E intersect( ICluster<T> other ) {
		return extend( getCollection().intersect( other ) );
	}

	@Override
	public E intersectBy( IEquality<? super T> equality, ICluster<T> other ) {
		return extend( getCollection().intersectBy( equality, other ) );
	}

	@Override
	public E union( ICluster<T> other ) {
		return extend( getCollection().union( other ) );
	}

	@Override
	public E unionBy( IEquality<? super T> equality, ICluster<T> other ) {
		return extend( getCollection().unionBy( equality, other ) );
	}

	@Override
	public E nub() {
		return extend( getCollection().nub() );
	}

	@Override
	public E nubBy( IEquality<? super T> equality ) {
		return extend( getCollection().nubBy( equality ) );
	}

	@Override
	public Conditional<E> partition( Predicate<? super T> condition ) {
		return extend( getCollection().partition( condition ) );
	}

	@Override
	public Conditional<E> split( T e, Comparator<? super T> c ) {
		return extend( getCollection().split( e, c ) );
	}
}
