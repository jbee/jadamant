package de.jbee.lang;

import static de.jbee.lang.Order.entriesBy;
import static de.jbee.lang.Order.typeaware;

/**
 * A {@linkplain Map} is a special case of {@link Multimap} where each key may just refer to one
 * value or in other words its a {@link Set} or {@link Entry}s where equality is given by equal
 * keys.
 * 
 * @author Jan Bernitt (jan.bernitt@gmx.de)
 */
public interface Map<V>
		extends Set<Map.Entry<V>>, Multimap<V> {

	/**
	 * Default order used for all {@link Map}s. The keys are sorted alphabetical.
	 */
	Ord<Object> ENTRY_ORDER = typeaware( entriesBy( Order.alphabetical ), Map.Entry.class );

	/*
	 * Covariant return type overrides from List/Bag/Set/Multimap interface with Map return type
	 */

	@Override
	Map<V> deleteAt( int index );

	@Override
	Map<V> drop( int count );

	/**
	 * There will be one or none so we know that the {@link Map}-constraint won't be violated.
	 */
	@Override
	Map<V> entriesAt( int index );

	/**
	 * In case a entry with the same key already exists the key's value is redefined and the
	 * <code>value</code> given (and only that value) is associated with the key from now on.
	 */
	@Override
	Map<V> insert( Key key, V value );

	@Override
	Map<V> insert( Map.Entry<V> e );

	@Override
	Map<V> subsequent();

	@Override
	Map<V> take( int count );

	@Override
	Map<V> tidyUp();

	interface Key {

		/**
		 * The terminator is the highest unicode character existing. It is appended to the searched
		 * prefix to find the position between the last key having the prefix and the first one that
		 * does not.
		 */
		String PREFIX_TERMINATOR = "\ufffd";

		String path();

		//OPEN add start + end index to narrow search ? -> it doesn't make sense for insert
	}

	interface Entry<V>
			extends Element<V> {

		Key key();

	}
}
