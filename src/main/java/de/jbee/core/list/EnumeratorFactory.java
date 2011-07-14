package de.jbee.core.list;

import de.jbee.core.type.Enum;

public interface EnumeratorFactory {

	<E> Enumerator<E> enumerate( Enum<E> type );
}
