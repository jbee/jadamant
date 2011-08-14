package de.jbee.util;

import java.util.Comparator;

import de.jbee.lang.Predicate;

public interface IBag<T, C extends IBag<T, C>>
		extends ICollection<T> {

	boolean isEqual( C other );

	boolean isEqualBy( IEquality<? super T> equality, C other );

	/**
	 * Counts the occurrences of the element e in this bag.
	 */
	int count( T e );

	/**
	 * Counts the occurrences of the element e in this bag.
	 */
	int countBy( IEquality<? super T> equality, T e );

	/**
	 * removes the first occurrence of the specified element equal to e
	 */
	C delete( T e );

	/**
	 * similar to {@link #delete(Object)}, but it allows the programmers to supply their own
	 * equality test
	 */
	C deleteBy( IEquality<? super T> equality, T e );

	C filter( Predicate<? super T> filterFunction );

	C intersect( ICluster<T> other );

	C intersectBy( IEquality<? super T> equality, ICluster<T> other );

	C union( ICluster<T> other );

	C unionBy( IEquality<? super T> equality, ICluster<T> other );

	C difference( ICluster<T> other );

	C differenceBy( IEquality<? super T> equality, ICluster<T> other );

	/**
	 * nub (meaning "essence") removes duplicates elements from a list.
	 */
	C nub();

	C nubBy( IEquality<? super T> equality );

	/**
	 * takes a {@link Predicate} and returns a pair of bags: those elements of this bag that do and
	 * do not satisfy the condition, respectively. If this bag has an order that order is kept in
	 * the result bags.
	 */
	Conditional<? extends C> partition( Predicate<? super T> condition );

	/**
	 * splits this bag at the the element e. The split-element e itself will be contained (if occur
	 * in bag) in the positive bag together with all elements less than e. The negative bag holds
	 * all elements larger than e.
	 */
	Conditional<? extends C> split( T e, Comparator<? super T> c );
}
