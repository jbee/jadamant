package de.jbee.lang;

public interface Multimap<V>
		extends Bag<Map.Entry<V>> {

	/*
	 * The usual get/put
	 */

	List<V> valuesFor( CharSequence key );

	Multimap<V> insert( CharSequence key, V value );

	/*
	 * Covariant return type overrides from Set interface with Multimap return type
	 */

	Multimap<V> entriesAt( int index );

	@Override
	Multimap<V> add( Map.Entry<V> e );

	@Override
	Multimap<V> subsequent();

	@Override
	Multimap<V> deleteAt( int index );

	@Override
	Multimap<V> drop( int count );

	@Override
	Multimap<V> take( int count );

	@Override
	Multimap<V> tidyUp();
}
