package de.jbee.dying;

import java.io.Serializable;
import java.util.Comparator;

import de.jbee.lang.Eq;
import de.jbee.lang.Predicate;
import de.jbee.lang.Sequence;

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
interface List<T>
		extends ISequence<T>, Sequence<T>, IBag<T, List<T>>, IFoldable<T>, Serializable {

	boolean isPrefixOf( List<T> other );

	boolean isSuffixOf( List<T> other );

	int elemIndex( T e );

	int elemIndexBy( Eq<? super T> equality, T e );

	int findIndex( Predicate<? super T> condition );

	NumberList<Integer> findIndices( Predicate<? super T> condition );

	/**
	 * returns an in-order list of indices
	 */
	NumberList<Integer> elemIndicesBy( Eq<? super T> equality, T e );

	List<T> concat( List<T> other );

	List<T> delete( int index )
			throws IndexOutOfBoundsException;

	List<T> zip( List<? extends T> other );

	List<T> drop( IRange<? extends Number> indexRange );

	List<T> dropL( int firstN );

	List<T> dropR( int lastN );

	/**
	 * creates a list from another one, it inspects the original list and takes from it its elements
	 * from the moment when the condition fails for the first time till the end of the list
	 */
	List<T> dropWhile( Predicate<? super T> stopCondition );

	List<T> take( IRange<? extends Number> indexRange );

	List<T> takeL( int firstN );

	List<T> takeR( int lastN );

	/**
	 * creates a list from another one, it inspects the original list and takes from it its elements
	 * to the moment when the condition fails, then it stops processing
	 */
	List<T> takeWhile( Predicate<? super T> stopCondition );

	@Override
	List<T> tail();

	@Override
	List<T> prepand( T e );

	/**
	 * inserts separator between the elements of its list argument
	 * 
	 * <pre>
	 * Input: intersperse("-") on "Hello"
	 * Output: "H-e-l-l-o"
	 * </pre>
	 */
	List<T> intersperse( T e );

	/**
	 * creates a list of length given by the second argument and the items having value of the
	 * element at the first argument index of this list
	 */
	List<T> replicate( int index, int length )
			throws IndexOutOfBoundsException;

	List<T> reverse();

	List<T> sort( Comparator<? super T> c );

	/**
	 * returns a list constructed by appling a function (the first argument) to all items in a list
	 * passed as the second argument
	 */
	<R> List<R> map( IFunc1<R, T> mapFunction );

	Conditional<? extends List<T>> splitAt( int index )
			throws IndexOutOfBoundsException;

	IMutableList<T> mutable();

	T[] toArray( Class<T> elementType );

	ICompareableList<T> comparedBy( Comparator<T> comparator );
}
