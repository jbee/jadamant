package de.jbee.util;

import java.io.Serializable;
import java.util.Comparator;

import de.jbee.core.Sequence;

/**
 * Interface of an immutable type-save functional inspired list.
 * 
 * Each manipulation will return a new list instance includes the changes. The original list stays
 * unchanged.
 * 
 * @author Jan Bernitt (jan.bernitt@gmx.de)
 * 
 * @param <T>
 *            Element-Type
 */
public interface IList<T>
		extends ISequence<T>, Sequence<T>, IBag<T, IList<T>>, IFoldable<T>, Serializable {

	boolean isPrefixOf( IList<T> other );

	boolean isSuffixOf( IList<T> other );

	int elemIndex( T e );

	int elemIndexBy( IEquality<? super T> equality, T e );

	int findIndex( ICondition<? super T> condition );

	NumberList<Integer> findIndices( ICondition<? super T> condition );

	/**
	 * returns an in-order list of indices
	 */
	NumberList<Integer> elemIndicesBy( IEquality<? super T> equality, T e );

	IList<T> concat( IList<T> other );

	IList<T> delete( int index )
			throws IndexOutOfBoundsException;

	IList<T> zip( IList<? extends T> other );

	IList<T> drop( IRange<? extends Number> indexRange );

	IList<T> dropL( int firstN );

	IList<T> dropR( int lastN );

	/**
	 * creates a list from another one, it inspects the original list and takes from it its elements
	 * from the moment when the condition fails for the first time till the end of the list
	 */
	IList<T> dropWhile( ICondition<? super T> stopCondition );

	IList<T> take( IRange<? extends Number> indexRange );

	IList<T> takeL( int firstN );

	IList<T> takeR( int lastN );

	/**
	 * creates a list from another one, it inspects the original list and takes from it its elements
	 * to the moment when the condition fails, then it stops processing
	 */
	IList<T> takeWhile( ICondition<? super T> stopCondition );

	@Override
	IList<T> tail();

	@Override
	IList<T> prepand( T e );

	/**
	 * inserts separator between the elements of its list argument
	 * 
	 * <pre>
	 * Input: intersperse("-") on "Hello"
	 * Output: "H-e-l-l-o"
	 * </pre>
	 */
	IList<T> intersperse( T e );

	/**
	 * creates a list of length given by the second argument and the items having value of the
	 * element at the first argument index of this list
	 */
	IList<T> replicate( int index, int length )
			throws IndexOutOfBoundsException;

	IList<T> reverse();

	IList<T> sort( Comparator<? super T> c );

	/**
	 * returns a list constructed by appling a function (the first argument) to all items in a list
	 * passed as the second argument
	 */
	<R> IList<R> map( IFunc1<R, T> mapFunction );

	Conditional<? extends IList<T>> splitAt( int index )
			throws IndexOutOfBoundsException;

	IMutableList<T> mutable();

	T[] toArray( Class<T> elementType );

	ICompareableList<T> comparedBy( Comparator<T> comparator );
}
