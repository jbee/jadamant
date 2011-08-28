package de.jbee.lang;

/**
 * A general unary operator interface.
 * 
 * @author Jan Bernitt (jan.bernitt@gmx.de)
 */
public interface UnaryOp<T> {

	T operate( T value );
}
