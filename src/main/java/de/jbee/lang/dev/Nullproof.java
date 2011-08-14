package de.jbee.lang.dev;

/**
 * <p>
 * Can be implemented if a implementation can be {@link Nullsave} depending on the constructor
 * arguments to tell weather or not a concrete instance is null-save.
 * </p>
 * <p>
 * Therefore {@link Nullproof} is the <i>per instance</i> equivalent to {@linkplain Nullsave}.
 * </p>
 * 
 * @see Nullsave
 * 
 * @author Jan Bernitt (jan.bernitt@gmx.de)
 */
public interface Nullproof {

	boolean isNullsave();
}
