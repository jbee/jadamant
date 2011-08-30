package de.jbee.lang;

/**
 * 
 * @see Is
 * 
 * @author Jan Bernitt (jan.bernitt@gmx.de)
 * 
 */
public interface Predicate<T> {

	boolean is( T obj );

	//OPEN how to support isn't negation ? just for readability
}
