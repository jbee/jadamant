package de.jbee.lang.dev;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * Only as a information to the developer, how a type (all its methods) will handle and expect
 * <code>null</code> references.
 * 
 * <b>If used: Do not except any methods from this contract! Behavior should be predictable
 * easily!</b>
 * 
 * In case a interface is annotated <b>all</b> implementation <b>has to</b> follow this contract!
 * 
 * @author Jan Bernitt (jan.bernitt@gmx.de)
 */
@Target ( value = { ElementType.TYPE } )
public @interface Nulls {

	/**
	 * Behavior of null as a return value of the methods of the annotated type
	 */
	Nullness returns();

	/**
	 * Expectation of *all* methods parameters of the annotated type
	 */
	Nullness parameters();
}
