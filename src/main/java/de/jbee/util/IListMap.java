package de.jbee.util;

import de.jbee.lang.Predicate;

public interface IListMap<K, V>
		extends Iterable<IList<V>> {

	IList<V> values( K key );

	IListMap<K, V> append( K key, V value );

	IListMap<K, V> appendEach( V value );

	IListMap<K, V> appendEach( Predicate<? super K> keyCondition, V value );

	ISet<K> keys();

	IList<V> merge();

	int size();
}
