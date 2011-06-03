package de.jbee.core;

public interface IIndexAccessible<E> {

	E at( int index )
			throws IndexOutOfBoundsException;

}
