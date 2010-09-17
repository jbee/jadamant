package de.jbee.util;

public interface IListMap<K, V>
		extends Iterable<IList<V>> {

	IList<V> values( K key );

	IListMap<K, V> append( K key, V value );

	ISet<K> keys();
}
