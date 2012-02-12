package de.jbee.data;

import de.jbee.data.DataProperty.ObjectProperty;
import de.jbee.data.DataProperty.RangeProperty;
import de.jbee.data.DataProperty.ValueProperty;
import de.jbee.lang.List;
import de.jbee.lang.Table;

public interface Data<T> {

	<S> Data<S> object( ObjectProperty<? super T, S> property );

	<V> V value( ValueProperty<? super T, V> property );

	<S> List<Data<S>> subs( RangeProperty<? super T, S> path );

	boolean isEmpty();

	interface DataTable<T>
			extends Table<T> {

		Data<T> slice( Path prefix, int start, int end );
	}
}
