package de.jbee.core;

public interface Arrayable {

	/**
	 * This object is told to fill its data values into the array starting at the
	 * <code>offset</code> position or the <code>array</code>. It should fill <code>length</code>
	 * data form its <code>start</code> index
	 */
	void fill( Object[] array, int start, int length, int offset );
}