package de.jbee.core.list;

import de.jbee.core.Sequence;

public interface Lister {

	<E> List<E> noElements();

	<E> List<E> element( E e );

	<E> List<E> elements( E... elems );

	<E> List<E> elements( Sequence<E> elems );

}
