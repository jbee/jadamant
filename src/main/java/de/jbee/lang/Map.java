package de.jbee.lang;

public interface Map<V>
		extends Set<Map.Entry<V>>, Multimap<V> {

	Ord<Object> ENTRY_ORDER = Order.typeaware( Order.entriesBy( Order.alphabetical ),
			Map.Entry.class );

	V valueFor( CharSequence key );

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
	Map<V> insert( CharSequence key, V value );

	@Override
	Map<V> insert( Map.Entry<V> e );

	@Override
	Map<V> subsequent();

	@Override
	Map<V> take( int count );

	@Override
	Map<V> tidyUp();

	interface Entry<V> {

		CharSequence key();

		V value();

	}
}
