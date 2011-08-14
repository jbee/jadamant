package de.jbee.dying;

import java.util.Comparator;

import de.jbee.lang.Eq;
import de.jbee.lang.Predicate;

public interface ICollection<T>
		extends ICluster<T> {

	boolean any( T e );

	boolean any( Predicate<? super T> condition );

	/**
	 * returns True if all items in this collection fulfill the condition
	 */
	boolean all( Predicate<? super T> condition );

	/**
	 * returns True if all elements of this collection are equal to e
	 */
	boolean all( T e );

	/**
	 * returns True if this collection contains all elements of the other cluster
	 */
	boolean allOf( ICluster<T> other );

	boolean anyBy( Eq<? super T> equality, T e );

	T find( Predicate<? super T> condition );

	T find( Predicate<? super T> condition, T noMatchValue );

	T maximumBy( Comparator<? super T> c );

	T minimumBy( Comparator<? super T> c );

}
