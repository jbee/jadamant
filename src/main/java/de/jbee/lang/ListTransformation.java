package de.jbee.lang;

public interface ListTransformation<E, T> {

	T on( List<E> list );
}
