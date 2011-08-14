package de.jbee.dying;

public interface IFoldable<T> {

	/**
	 * it takes the first 2 items of the list and applies the function to them, then feeds the
	 * function with this result and the third argument and so on.
	 */
	T foldL1( IFunc2<T, T, T> foldFunction )
			throws IndexOutOfBoundsException;

	T foldL1( IFunc2<T, T, T> foldFunction, T emptyValue );

	/**
	 * it takes the last two items of the list and applies the function, then it takes the third
	 * item from the end and the result, and so on.
	 */
	T foldR1( IFunc2<T, T, T> foldFunction )
			throws IndexOutOfBoundsException;

	T foldR1( IFunc2<T, T, T> foldFunction, T emptyValue );

	<R> R foldL( IFunc1<R, T> foldFunction );

	<R> R foldR( IFunc1<R, T> foldFunction );

	/**
	 * it takes the second argument and the first item of the list and applies the function to them,
	 * then feeds the function with this result and the second argument and so on.
	 */
	<R> R foldL( IFunc2<R, R, T> foldFunction, R inital );

	<R> R foldR( IFunc2<R, R, T> foldFunction, R inital );

}
