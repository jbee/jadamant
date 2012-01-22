package de.jbee.lang;

public interface Map<V>
		extends Set<Map.Entry<V>> {

	Ord<Object> ENTRY_ORDER = Order.typeaware( Order.entriesBy( Order.alphabetical ),
			Map.Entry.class );

	V get( CharSequence key );

	Map<V> put( CharSequence key, V value );

	interface Entry<V> {

		CharSequence key();

		V value();

	}
}
