package de.jbee.lang.dev;

/**
 * <p>
 * Marker interface to indicate that an implementation may or will break when passing null to its
 * methods. This is the counterpart to the {@link Nullsave} marker.
 * </p>
 * 
 * <h5>Isn't it a bug to be fragile to <code>null</code> ?<h5>
 * <p>
 * Shot: No its not. If you ban <code>null</code> in general the majority of the code doesn't have
 * to consider nulls. That' why we pay all the attention on nulls during the concepts.
 * </p>
 * 
 * @author Jan Bernitt (jan.bernitt@gmx.de)
 */
public interface Nullfragile {
	// Marker interface
}
