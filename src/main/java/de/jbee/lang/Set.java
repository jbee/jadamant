package de.jbee.lang;

/**
 * Sets are always sorted and therefore a {@link List} that doesn't allow duplicates.
 * 
 * @author Jan Bernitt (jan.bernitt@gmx.de)
 */
public interface Set<E>
		extends Sorted, List<E> {

	/**
	 * Inserts e at the correct position if not already contained in this set.
	 */
	Set<E> insert( E e );

	/*
	 * Overrides from List interface with Set return type
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
