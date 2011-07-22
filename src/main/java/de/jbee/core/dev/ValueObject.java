package de.jbee.core.dev;

/**
 * <p>
 * Marker interface to mark all classes that represent *value objects*.
 * </p>
 * <p>
 * A {@link ValueObject} is a simple immutable object like a date, an amount of money and so on.
 * There is never a specific {@linkplain ValueObject} interface. Just this one final immutable
 * class.
 * </p>
 * 
 * @author Jan Bernitt (jan.bernitt@gmx.de)
 * 
 */
public interface ValueObject {
	// just a marker
}
