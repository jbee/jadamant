package de.jbee.draft;

import de.jbee.lang.Set;

public interface Map<K, V>
		extends Set<Entry<K, V>> {

	V get( K key );
}
