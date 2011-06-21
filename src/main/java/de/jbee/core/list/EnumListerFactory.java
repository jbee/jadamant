package de.jbee.core.list;

import de.jbee.core.type.Enum;

public interface EnumListerFactory {

	<E> RichLister<E> enumerates( Enum<E> type );
}
