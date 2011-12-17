package de.jbee.lang;

/**
 * The ability to fill a object's 'internal data' into an array which is passed as argument.
 * <p>
 * This is kind of replacement for the 'asking' <code>toArray()</code> functionality. A object
 * instead is 'told' to set its data into an arrays cells. Thereby the creation of the array is
 * moved to the caller. This has the additional benefit that it can be avoided to create temporary
 * arrays just to 'get out' the data before copying multiple parts together into a larger one.
 * </p>
 * 
 * @author Jan Bernitt (jan.bernitt@gmx.de)
 */
public interface Arrayable {

	/**
	 * This object is told to fill its data values into the array starting at the
	 * <code>offset</code> position of the <code>array</code>. It should fill (up to)
	 * <code>length</code> data form its <code>start</code> index.
	 */
	void fill( int offset, Object[] array, int start, int length );

}
