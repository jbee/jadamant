package de.jbee.core;

public interface IndexAccessible<E> {

	E at( int index )
			throws IndexOutOfBoundsException;

}
