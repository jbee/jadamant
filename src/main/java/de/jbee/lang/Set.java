package de.jbee.lang;

import de.jbee.lang.ListTransition.SetTransition;
import de.jbee.lang.seq.UtileBagger;

/**
 * Sets are always {@link Sorted} and therefore a {@link List} that doesn't allow duplicates.
 * 
 * @author Jan Bernitt (jan.bernitt@gmx.de)
 */
public interface Set<E>
		extends Bag<E> {

	UtileBagger with = new UtileBagger();

	SetTransition derive = List.which.narrowsToSet();

	//OPEN how to make the ListIndex work with the Ord from this Set (Sorted) for Ord or Eq depended indexes 

	/**
	 * Inserts e at the correct position if not already contained in this set.
	 */
	Set<E> insert( E e );

	/*
	 * Overrides from List/Bag interface with Set return type
	 */

	@Override
	Set<E> deleteAt( int index );

	@Override
	Set<E> drop( int count );

	@Override
	Set<E> take( int count );

	@Override
	Set<E> tidyUp();

	//TODO some kind of int indexFor(E e) - more like a traverse ?
}
