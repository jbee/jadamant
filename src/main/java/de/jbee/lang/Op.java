package de.jbee.lang;

/**
 * A general binary operator interface.
 * 
 * @author Jan Bernitt (jan.bernitt@gmx.de)
 * 
 * @see Operator for utility methods
 */
public interface Op<T> {

	T operate( T left, T right );

}
