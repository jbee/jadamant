package de.jbee.lang;

/**
 * A general binary operator interface.
 * 
 * @author Jan Bernitt (jan.bernitt@gmx.de)
 */
public interface Op<T> {

	T operate( T left, T right );
}
