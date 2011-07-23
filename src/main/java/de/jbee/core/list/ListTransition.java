package de.jbee.core.list;

/**
 * {@link ListTransition}s are manipulations on a {@link List} that doesn't change the type of
 * elements - just the elements contained in the resulting list.
 * 
 * A ListTransition doesn't take any additional state. It has no internal state that can change
 * whereby same input always causes same output. Nevertheless they might have a fix internal state
 * they controls their algorithm.
 * 
 * @author Jan Bernitt (jan.bernitt@gmx.de)
 */
public interface ListTransition {

	ListTransition reverse = UtileListTransition.reverse;
	ListTransition tail = UtileListTransition.tail;

	<E> List<E> from( List<E> list );

}
