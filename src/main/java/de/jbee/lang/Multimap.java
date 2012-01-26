package de.jbee.lang;

import de.jbee.lang.Map.Entry;
import de.jbee.lang.Map.Key;

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

	/**
	 * @see #entriesAt(int)
	 * @see #indexFor(CharSequence)
	 * 
	 * @return All values having the same key as the entry at the given <code>index</code>. Their
	 *         sequence is corresponding to the sequence of the values entries.
	 */
	Bag<V> valuesAt( int index )
			throws IndexOutOfBoundsException;

	int indexFor( Key key );

	Multimap<V> insert( Key key, V value );

	/*
	 * Override to add appropiate javadoc
	 */

	/**
	 * A maps order reflects the key's order. It will not consider possible sorting of values (for
	 * the same key). That is {@link Map.Entry}s having the same key considered to be identical.
	 */
	@Override
	Ord<Object> order();

	/*
	 * Covariant return type overrides from Set interface with Multimap return type
	 */

	/**
	 * @see #valuesAt(CharSequence)
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
