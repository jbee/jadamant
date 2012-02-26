package de.jbee.data;

import de.jbee.data.Dataset.RecordProperty;
import de.jbee.data.Dataset.Records;

public class Record<R, T>
		implements RecordProperty<R, T> {

	public static <R, T> Record<R, T> record( RecordProperty<R, T> record ) {
		return new Record<R, T>( record );
	}

	private final RecordProperty<R, T> record;

	private Record( RecordProperty<R, T> record ) {
		super();
		this.record = record;
	}

	@Override
	public Dataset<T> resolveIn( Path root, Records records ) {
		return record.resolveIn( root, records );
	}

	public <C> Record<R, C> child( RecordProperty<T, C> child ) {
		return new Record<R, C>( Property.record( record, child ) );
	}

	//		RangeProperty<R, T> repeat( int times );

	//		<S> ObjectProperty<R, S> dot( ObjectProperty<T, S> subpath );
	//
	//		<V> ValueProperty<R, V> dot( ValueProperty<T, V> subpath );
	//
	//		<E> RangeProperty<R, E> dot( RangeProperty<T, E> subpath );
}
