package de.jbee.core.list;

import de.jbee.core.type.Enum;

public interface ListFactory {

	<E> List<E> noElements();

	<E> List<E> element( E e );

	<E> List<E> elements( E... elems );

	<E> List<E> elements( Iterable<E> elems );

	<E> List<E> from( E start, Enum<E> type );

	<E> List<E> fromTo( E start, E end, Enum<E> type );

}
