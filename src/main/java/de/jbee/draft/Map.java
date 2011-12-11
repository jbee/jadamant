package de.jbee.draft;

import de.jbee.lang.Set;

public interface Map<V>
		extends Set<Entry<V>> {

	V get( String key );
}
