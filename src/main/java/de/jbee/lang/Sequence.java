package de.jbee.lang;

/**
 * 
 * @author Jan Bernitt (jan.bernitt@gmx.de)
 * 
 * @see List
 */
public interface Sequence<E>
		extends IndexAccessible<E> {

	int length();

	boolean isEmpty();
}
