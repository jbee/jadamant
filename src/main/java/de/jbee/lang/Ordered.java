package de.jbee.lang;

/**
 * Indicates that a sequence is sorted (in order).
 * 
 * It provides access to the {@link Ord} instance that designates the order of that specific
 * instance via the {@link #order()} method. Thereby it is e.g. possible to say whether or not two
 * {@linkplain Ordered} {@link Sequence}s are using the same strategy to sort and are therefore fully
 * comparable and interoperable with each other.
 * 
 * @author Jan Bernitt (jan.bernitt@gmx.de)
 */
public interface Ordered {

	/**
	 * returns the instance controlling the order of the sequence. this might be a computed object.
	 */
	Ord<Object> order();
}
