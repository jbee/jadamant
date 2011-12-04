package de.jbee.lang;

public interface Arrayable {

	/**
	 * This object is told to fill its data values into the array starting at the
	 * <code>offset</code> position of the <code>array</code>. It should fill (up to)
	 * <code>length</code> data form its <code>start</code> index.
	 */
	void fill( int offset, Object[] array, int start, int length );

}
