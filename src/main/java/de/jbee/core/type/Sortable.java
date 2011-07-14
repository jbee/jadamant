package de.jbee.core.type;

/**
 * Can be implemented by enums to easily support {@link Ord} interface by using
 * {@link Order#sortable}.
 * 
 * @author Jan Bernitt (jan.bernitt@gmx.de)
 */
public interface Sortable {

	int ordinal();
}
