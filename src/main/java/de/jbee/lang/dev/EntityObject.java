package de.jbee.lang.dev;

/**
 * <p>
 * Marker interface to mark all classes that represent *entity objects*.
 * </p>
 * <p>
 * A {@link EntityObject} consists of only {@link ValueObject}s. It has no logic or service
 * functionality like {@link ServiceObject}s have.
 * </p>
 * <ul>
 * <li>Entities are values with entity semantics</li>
 * <li>They have some kind of ID, and properly end up beeing stored.</li>
 * <li>Entities are equal if their ID is equal. Objects with same ID and different values represent
 * different states of the same object</li>
 * </ul>
 * 
 * @author Jan Bernitt (jan.bernitt@gmx.de)
 * 
 */
public interface EntityObject {
	// just a marker
}
