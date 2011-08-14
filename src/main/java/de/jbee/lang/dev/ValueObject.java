package de.jbee.lang.dev;

/**
 * <p>
 * Marker interface to mark all classes that represent *value objects*.
 * </p>
 * <p>
 * A {@link ValueObject} is a simple immutable object like a date, an amount of money and so on.
 * There is never a specific {@linkplain ValueObject} interface. Just this one final immutable
 * class.
 * </p>
 * <ul>
 * <li>Values are transient</li>
 * <li>The equality of two objects depends on the value they represent</li>
 * <li>They are lightweight enough to be created when needed and thrown away when no longer needed</li>
 * </ul>
 * 
 * @author Jan Bernitt (jan.bernitt@gmx.de)
 * 
 */
public interface ValueObject {
	// just a marker
}
