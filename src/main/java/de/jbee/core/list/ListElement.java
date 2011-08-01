package de.jbee.core.list;

public interface ListElement {

	/**
	 * The result indicates, that a element is not contained in the list.
	 */
	int NOT_CONTAINED = -1;

	ListElement head = UtileListElement.HEAD;

	/**
	 * TODO change List to a more general type
	 * 
	 * @return the index of the element found or {@link #NOT_CONTAINED} if no element matched.
	 */
	<E> int indexIn( List<E> list );

}
