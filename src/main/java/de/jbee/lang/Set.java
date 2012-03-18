package de.jbee.lang;

import de.jbee.lang.ListAlteration.SetAlteration;
import de.jbee.lang.Lister.SetLister;
import de.jbee.lang.seq.Sequences;

/**
 * Sets are always {@link Ordered} and therefore a {@link List} that doesn't allow duplicates.
 * 
 * @author Jan Bernitt (jan.bernitt@gmx.de)
 * 
 * @see Bag
 * @see List
 */
public interface Set<E>
		extends Bag<E> {

	SetLister with = Sequences.SET_LISTER;
	SetAlteration refine = List.alterBy.refineToSet();

	Set<Object> empty = Set.with.noElements();

	//OPEN how to make the ListIndex work with the Ord from this Set (Sorted) for Ord or Eq depended indexes 
	// idea: Oder.inherent means a Sorted object uses its order instead of the inherent one.

	/**
	 * Inserts e at the correct position if not already contained in this set.
	 */
	Set<E> insert( E e );

	/*
	 * Covariant return type overrides from List/Bag interface with Set return type
	 */

	@Override
	Set<E> subsequent();

	@Override
	Set<E> deleteAt( int index );

	@Override
	Set<E> drop( int count );

	@Override
	Set<E> take( int count );

	@Override
	Set<E> tidyUp();

	//OPEN union/intersection as methods or SetTransition ? 
}
