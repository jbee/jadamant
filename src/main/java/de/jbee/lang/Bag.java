package de.jbee.lang;

/**
 * {@linkplain Bag}s are {@link Sorted} {@link List}s as well as {@link Set}s are. In contrast to
 * sets they allow multiple equal elements.
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
	 * @return A list of all elements that are equal (by {@link Ordering#EQ} to the element at
	 *         <code>index</code>. If there is no such element a empty list is returned.
	 */
	List<E> entriesAt( int index );

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
