package de.jbee.lang.seq;

import de.jbee.lang.Map;
import de.jbee.lang.Ord;
import de.jbee.lang.Set;

public class UtileMapLister
		implements Map.MapLister {

	@Override
	public <E> Map<E> noEntries( Ord<Object> order ) {
		return OrderedList.mapOf( Set.with.<Map.Entry<E>> noElements( order ) );
	}

}
