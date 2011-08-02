package de.jbee.core.list;

/**
 * A {@link ListElement} is a function that resolves the index of a specific element in a
 * {@link List}.
 * 
 * @author Jan Bernitt (jan.bernitt@gmx.de)
 * 
 */
public interface ListElement {

	/**
	 * The result indicates, that a element is not contained in the list.
	 */
	int NOT_CONTAINED = -1;

	/**
	 * TODO change List to a more general type
	 * 
	 * @return the index of the element found or {@link #NOT_CONTAINED} if no element matched.
	 */
	<E> int indexIn( List<E> list );

}
