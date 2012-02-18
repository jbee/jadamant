package de.jbee.data;

import de.jbee.data.DataProperty.ObjectProperty;
import de.jbee.data.DataProperty.RangeProperty;
import de.jbee.data.DataProperty.ValueProperty;
import de.jbee.lang.Map;
import de.jbee.lang.Ord;
import de.jbee.lang.Sequence;
import de.jbee.lang.Table;

public interface Data<T>
		extends Sequence<Object> {

	Ord<Object> ORDER = Map.ENTRY_ORDER;

	<S> Data<S> object( ObjectProperty<? super T, S> property );

	<S> Data<S> objects( RangeProperty<? super T, S> property );

	Sequence<Data<T>> each();

	<V> V value( ValueProperty<? super T, V> property );

	interface DataTable<T>
			extends Table<T> {

		Data<T> slice( Path prefix, int startInclusive, int endExclusive );
	}

}
