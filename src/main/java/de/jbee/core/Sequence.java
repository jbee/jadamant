package de.jbee.core;

import de.jbee.core.list.List;
import de.jbee.util.ICluster;

/**
 * 
 * @author Jan Bernitt (jan.bernitt@gmx.de)
 * 
 * @see List
 */
public interface Sequence<E>
		extends IndexAccessible<E>, ICluster<E> {

	// Guarantee both interfaces
}
