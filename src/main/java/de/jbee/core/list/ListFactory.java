package de.jbee.core.list;


public interface ListFactory {

	<E> List<E> noElements();

	<E> List<E> element( E e );

	<E> List<E> elements( E... elems );

	<E> List<E> elements( Iterable<E> elems );

}
