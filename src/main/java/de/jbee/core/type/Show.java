package de.jbee.core.type;

public interface Show<T> {

	/**
	 * equal to {@link #shows(Object, String)} with <code>shows(e, "");</code>
	 */
	String show( T e );

	String shows( T e, String accumulator );
}
