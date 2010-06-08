package de.jbee.util;

/**
 * 
 * @see Fulfills
 * 
 * @author Jan Bernitt (jan.bernitt@gmx.de)
 * 
 * @param <T>
 */
public interface ICondition<T> {

	boolean fulfilledBy( T obj );

}
