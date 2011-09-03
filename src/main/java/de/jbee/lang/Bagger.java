package de.jbee.lang;

public interface Bagger {

	<E> Set<E> noElements();

	<E> Set<E> elements( Ord<Object> order, List<E> elems );
}
