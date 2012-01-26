package de.jbee.lang;

public interface MapFactory<K, V> {

	Map<V> emptyMap();

	Map.Key key( K key );

	Map.Entry<V> entry( K key, V value );
}
