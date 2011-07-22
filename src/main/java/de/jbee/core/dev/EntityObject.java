package de.jbee.core.dev;

/**
 * <p>
 * Marker interface to mark all classes that represent *entity objects*.
 * </p>
 * <p>
 * A {@link EntityObject} consists of only {@link ValueObject}s. It has no logic or service
 * functionality like {@link ServiceObject}s have.
 * </p>
 * 
 * @author Jan Bernitt (jan.bernitt@gmx.de)
 * 
 */
public interface EntityObject {
	// just a marker
}
