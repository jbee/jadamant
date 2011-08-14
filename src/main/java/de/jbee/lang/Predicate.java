package de.jbee.lang;

/**
 * 
 * @see Fulfills
 * 
 * @author Jan Bernitt (jan.bernitt@gmx.de)
 * 
 *         OPEN rename to Bool/True ?
 */
public interface Predicate<T> {

	boolean fulfilledBy( T obj );

}
