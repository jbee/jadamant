package de.jbee.lang;


/**
 * A {@link ListIndex} is a function that resolves the index of a specific element in a
 * {@link Sequence} like a {@link List}.
 * 
 * @author Jan Bernitt (jan.bernitt@gmx.de)
 * 
 */
public interface ListIndex {

	/**
	 * The result indicates, that a element is not contained in the list.
	 */
	int NOT_CONTAINED = -1;

	/**
	 * @return the index of the element found or {@link #NOT_CONTAINED} if no element matched.
	 */
	<E> int in( Sequence<E> list );

}
