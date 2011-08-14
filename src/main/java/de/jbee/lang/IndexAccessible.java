package de.jbee.lang;

public interface IndexAccessible<E> {

	E at( int index )
			throws IndexOutOfBoundsException;

}
