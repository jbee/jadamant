package de.jbee.lang;

/**
 * The ability to determine the index of a specific element (in a {@link Sequence}).
 * 
 * @author Jan Bernitt (jan.bernitt@gmx.de)
 * 
 * @see Sequence
 */
public interface IndexDeterminable<E> {

	/**
	 * Searches for the element <code>e</code> and returns its index in this sequence.
	 * 
	 * In contrast to a index lookup using {@link ListIndex}-functions this index search should be
	 * faster since it can make use of the order of elements in this sequence.
	 * 
	 * This algorithm has (very likely) a O(log(n)) runtime complexity instead of the usual O(n).
	 * 
	 * In case of a {@link Bag} the index of the first equal element is returned.
	 */
	public abstract int indexFor( E e );

}