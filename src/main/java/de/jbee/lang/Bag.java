package de.jbee.lang;

/**
 * {@linkplain Bag}s are {@link Sorted} {@link List}s as well as {@link Set}. In contrast to sets
 * they accept multiple equal elements.
 * 
 * All equal elements will be positioned at successive indexes.
 * 
 * @author Jan Bernitt (jan.bernitt@gmx.de)
 */
public interface Bag<E>
		extends Sorted, List<E> {

	/**
	 * Adds e at the correct index in this bag. If this bag contains a element equal to e this will
	 * be added ahead of it.
	 */
	Bag<E> add( E e );

	/**
	 * @return The amount of elements in this bag equal to <code>e</code>. Thereby equality is
	 *         derived from this bag's {@link Ord}er. All elements having {@link Ordering#EQ} when
	 *         compared to <code>e</code> are considered to be equal.
	 */
	int count( E e );

	/*
	 * Overrides from List interface with Set return type
	 */

	@Override
	Bag<E> deleteAt( int index );

	@Override
	Bag<E> drop( int count );

	@Override
	Bag<E> take( int count );

	@Override
	Bag<E> tidyUp();
}
