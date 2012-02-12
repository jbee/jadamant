package de.jbee.lang.seq;

import de.jbee.lang.Lister;
import de.jbee.lang.Map;
import de.jbee.lang.Set;

public class UtileMapLister
		implements Lister.MapLister {

	@Override
	public <E> Map<E> noEntries() {
		return OrderedList.mapOf( Set.with.<Map.Entry<E>> noElements() );
	}

}
