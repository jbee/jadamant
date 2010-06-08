package de.jbee.util;

import java.util.Comparator;

public interface ICollection<T>
		extends ICluster<T> {

	boolean any( T e );

	boolean any( ICondition<? super T> condition );

	/**
	 * returns True if all items in this collection fulfill the condition
	 */
	boolean all( ICondition<? super T> condition );

	/**
	 * returns True if all elements of this collection are equal to e
	 */
	boolean all( T e );

	/**
	 * returns True if this collection contains all elements of the other cluster
	 */
	boolean allOf( ICluster<T> other );

	boolean anyBy( IEquality<? super T> equality, T e );

	T find( ICondition<? super T> condition );

	T find( ICondition<? super T> condition, T noMatchValue );

	T maximumBy( Comparator<? super T> c );

	T minimumBy( Comparator<? super T> c );

}
