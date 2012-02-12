package de.jbee.lang;

import de.jbee.lang.Map.Key;

/**
 * A {@linkplain Table} is a minimal interface for any data-structure that is {@link Searchable}
 * using a {@link Map.Key} to find the {@link #indexFor(Key)} a specific element.
 * 
 * @author Jan Bernitt (jan.bernitt@gmx.de)
 */
public interface Table<E>
		extends Searchable, Ordered, Sequence<E> {

	//for now we just name those two interfaces

}
