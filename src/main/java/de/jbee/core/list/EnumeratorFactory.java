package de.jbee.core.list;

import de.jbee.core.type.Enum;

public interface EnumeratorFactory {

	<E> RichEnumerator<E> enumerates( Enum<E> type );
}
