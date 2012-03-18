package de.jbee.lang;

import de.jbee.lang.ListAlteration.BagAlteration;
import de.jbee.lang.Lister.BagLister;
import de.jbee.lang.seq.Sequences;

/**
 * {@linkplain Bag}s are {@link Ordered} {@link List}s as well as {@link Set}s are. In contrast to
 * sets they allow multiple equal elements.
 * 
 * All equal elements will be positioned at successive indexes.
 * 
 * @author Jan Bernitt (jan.bernitt@gmx.de)
 */
public interface Bag<E>
		extends IndexDeterminable<E>, Ordered, List<E> {

	BagAlteration refine = List.alterBy.refineToBag();
	BagLister with = Sequences.BAG_LISTER;

	Bag<Object> empty = Bag.with.noElements();

	/**
	 * Adds e at the correct index in this bag. The exact position a duplicate is inserted depends
	 * on the {@link Ord}er used by this sorted list.
	 */
	Bag<E> add( E e );

	/**
	 * Can be used together with {@link #indexFor(Object)} to receive a {@link Bag} of all elements
	 * equal to a sample.
	 * 
	 * @return A list of all elements that are equal (by {@link Ordering#EQ} to the element at
	 *         <code>index</code>. If there is no such element a empty list is returned.
	 */
	Bag<E> entriesAt( int index );

	/*
	 * Covariant return type overrides from List interface with Set return type
	 */

	@Override
	Bag<E> subsequent();

	@Override
	Bag<E> deleteAt( int index );

	@Override
	Bag<E> drop( int count );

	@Override
	Bag<E> take( int count );

	@Override
	Bag<E> tidyUp();
}
