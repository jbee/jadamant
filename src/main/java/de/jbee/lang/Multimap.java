package de.jbee.lang;

import de.jbee.lang.Map.Entry;

public interface Multimap<V>
		extends Bag<Map.Entry<V>> {

	/*
	 * The usual methods commonly known as get/put in the java-field.
	 */

	/**
	 * @return All values associated with the given key. Their sequence is the same as in the
	 *         multimap-part. In case no value is associated with the key a empty list is returned.
	 */
	List<V> valuesFor( CharSequence key );

	Multimap<V> insert( CharSequence key, V value );

	//OPEN List<V> values();

	/*
	 * Covariant return type overrides from Set interface with Multimap return type
	 */

	@Override
	Multimap<V> entriesAt( int index );

	/**
	 * You should prefer to use {@link #insert(CharSequence, Object)} since the map itself has
	 * control over the {@link Entry}'s type.
	 * 
	 * @see Multimap#insert(CharSequence, Object)
	 */
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
