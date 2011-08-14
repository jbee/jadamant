package de.jbee.core;

/**
 * A general relational operator interface.
 * 
 * A relational operation return true or false, depending on whether the conditional relationship
 * between the two operands holds or not.
 * 
 * @author Jan Bernitt (jan.bernitt@gmx.de)
 * 
 */
public interface RelationalOp<T> {

	boolean holds( T left, T right );
}
