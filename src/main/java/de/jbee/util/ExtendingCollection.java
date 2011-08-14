package de.jbee.util;

import java.util.Comparator;
import java.util.Iterator;

import de.jbee.lang.Predicate;

public abstract class ExtendingCollection<T, C extends ICollection<T>, E extends C>
		implements ICollection<T> {

	protected abstract E extend( C list );

	protected final Conditional<E> extend( Conditional<? extends C> c ) {
		return Conditional.<E> of( extend( c.positive ), extend( c.negative ) );
	}

	protected abstract C getCollection();

	@Override
	public boolean all( Predicate<? super T> condition ) {
		return getCollection().all( condition );
	}

	@Override
	public boolean all( T e ) {
		return getCollection().all( e );
	}

	@Override
	public boolean allOf( ICluster<T> other ) {
		return getCollection().allOf( other );
	}

	@Override
	public boolean any( Predicate<? super T> condition ) {
		return getCollection().any( condition );
	}

	@Override
	public boolean any( T e ) {
		return getCollection().any( e );
	}

	@Override
	public boolean anyBy( IEquality<? super T> equality, T e ) {
		return getCollection().anyBy( equality, e );
	}

	@Override
	public T find( Predicate<? super T> condition ) {
		return getCollection().find( condition );
	}

	@Override
	public T find( Predicate<? super T> condition, T noMatchValue ) {
		return getCollection().find( condition, noMatchValue );
	}

	@Override
	public T maximumBy( Comparator<? super T> c ) {
		return getCollection().maximumBy( c );
	}

	@Override
	public T minimumBy( Comparator<? super T> c ) {
		return getCollection().minimumBy( c );
	}

	@Override
	public int size() {
		return getCollection().size();
	}

	@Override
	public boolean isEmpty() {
		return getCollection().isEmpty();
	}

	@Override
	public Iterator<T> iterator() {
		return getCollection().iterator();
	}

	@Override
	public String toString() {
		return getCollection().toString();
	}
}
