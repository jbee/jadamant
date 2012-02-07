package de.jbee.data;

import de.jbee.data.DataProperty.ObjectProperty;
import de.jbee.data.DataProperty.RangeProperty;
import de.jbee.data.DataProperty.ValueProperty;
import de.jbee.lang.List;

public interface Data<T> {

	<S> Data<S> sub( ObjectProperty<? super T, S> path );

	<V> V value( ValueProperty<? super T, V> path );

	<S> List<Data<S>> subs( RangeProperty<? super T, S> path );

	//TODO	<V> V value( NotionalPath<? super T, V> path );

	boolean isEmpty();

}
