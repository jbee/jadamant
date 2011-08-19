package de.jbee.lang;

/**
 * A indicator (marker interface) that bear and order and are actually (not may be) sorted (in
 * order).
 * 
 * It provides access to the {@link Ord} instance that designates the order of that specific
 * instance via the {@link #order()} method. Thereby it is e.g. possible to say whether or not two
 * {@linkplain Sorted} {@link Sequence}s are using the same strategy to sort and are therefore fully
 * comparable and interoperable with each other.
 * 
 * @author Jan Bernitt (jan.bernitt@gmx.de)
 */
public interface Sorted {

	/**
	 * returns the instance controlling the order of the sequence. this might be a computed object.
	 */
	Ord<?> order();
}
