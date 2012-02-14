package de.jbee.lang;

import de.jbee.lang.Map.Key;

public interface Searchable {

	int indexFor( Key key );

	int indexFor( Key key, int startInclusive, int endExclusive );
}