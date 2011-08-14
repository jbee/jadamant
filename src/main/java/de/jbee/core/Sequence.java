package de.jbee.core;

import de.jbee.core.list.List;

/**
 * 
 * @author Jan Bernitt (jan.bernitt@gmx.de)
 * 
 * @see List
 */
public interface Sequence<E>
		extends IndexAccessible<E> {

	// Guarantee both interfaces

	int size();

	boolean isEmpty();
}
