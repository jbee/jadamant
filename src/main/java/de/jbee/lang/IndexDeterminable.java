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
	 * In case duplicates are allowed the index of the first equal element is returned. All
	 * duplicates will directly follow this index.
	 * 
	 * In case the element is not found a insert position is returned. That is the negated index
	 * subtracted by one. E.g. for zero it is -1.
	 */
	int indexFor( E e );

	int indexFor( E e, int start, int end );

}