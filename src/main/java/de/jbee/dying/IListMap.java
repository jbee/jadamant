package de.jbee.dying;

import de.jbee.lang.List;
import de.jbee.lang.Predicate;

public interface IListMap<K, V>
		extends Iterable<List<V>> {

	List<V> values( K key );

	IListMap<K, V> append( K key, V value );

	IListMap<K, V> appendEach( V value );

	IListMap<K, V> appendEach( Predicate<? super K> keyCondition, V value );

	ISet<K> keys();

	List<V> merge();

	int size();
}
