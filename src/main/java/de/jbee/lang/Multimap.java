package de.jbee.lang;

import de.jbee.lang.Map.Entry;

/**
 * A {@linkplain Multimap} may contain multiple values for each key. Therefore it is a {@link Bag}
 * or {@link Map.Entry}s where equality is given by equal keys.
 * 
 * OPEN how to remove all values of one key -> just a utility function ?!
 * 
 * @author Jan Bernitt (jan.bernitt@gmx.de)
 * 
 */
public interface Multimap<V>
		extends Bag<Map.Entry<V>> {

	/**
	 * @return All values in this {@link Multimap}. The result is primary ordered by the key order
	 *         and secondary by the value order used to initialize this {@link Multimap}.
	 */
	Bag<V> values();

	/*
	 * The usual methods commonly known as get/put in the java-field.
	 */

	/**
	 * @see #entriesAt(int)
	 * 
	 * @return All values associated with the given key. Their sequence is the same as in the
	 *         multimap-part. In case no value is associated with the key a empty list is returned.
	 */
	Bag<V> valuesFor( CharSequence key );

	Multimap<V> insert( CharSequence key, V value );

	/*
	 * Covariant return type overrides from Set interface with Multimap return type
	 */

	/**
	 * @see #valuesFor(CharSequence)
	 * 
	 * @return a {@link Multimap} containing all entries having the same key as the entry at the
	 *         given <code>index</code>.
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
