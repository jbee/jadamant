package de.jbee.data;

import static de.jbee.data.Path.itemPath;
import static de.jbee.data.Path.recordPath;
import static de.jbee.lang.seq.IndexFor.exists;
import static de.jbee.lang.seq.Sequences.key;
import de.jbee.data.Dataset.ItemProperty;
import de.jbee.data.Dataset.Items;
import de.jbee.data.Dataset.RecordDescriptor;
import de.jbee.data.Dataset.RecordProperty;
import de.jbee.data.Dataset.Records;
import de.jbee.data.Dataset.NotionalProperty;
import de.jbee.data.Dataset.ValueProperty;
import de.jbee.data.Dataset.Values;
import de.jbee.lang.Sequence;
import de.jbee.lang.dev.Nullsave;

/**
 * The util-class to work with {@link DataProperty}s and {@link Dataset}.
 * 
 * @author Jan Bernitt (jan.bernitt@gmx.de)
 * 
 */
public class Property {

	public static RecordDescriptor type( Class<?> type ) {
		return new InheritanceTypeDescriptor( type );
	}

	public static <R, T> RecordProperty<R, T> record( String name, Class<T> type ) {
		return new DynamicRecordProperty<R, T>( type, recordPath( name ), itemPath( 1 ) );
	}

	public static <R, T> ValueProperty<R, T> value( String name, Class<T> type, T defaultValue ) {
		return new NonnullProperty<R, T>( new TypedProperty<R, T>( type, recordPath( name ) ),
				defaultValue );
	}

	public static <E> ItemProperty<E, Sequence<Dataset<E>>> each() {
		return new EachItemProperty<E>();
	}

	public static <R> ValueProperty<R, Integer> value( String name, int defaultValue ) {
		return value( name, Integer.class, defaultValue );
	}

	static class EachItemProperty<E>
			implements ItemProperty<E, Sequence<Dataset<E>>> {

		@Override
		public Sequence<Dataset<E>> resolveIn( Items<E> items ) {
			return items;
		}

	}

	static final class NonnullProperty<R, T>
			implements ValueProperty<R, T> {

		private final ValueProperty<R, T> property;
		private final T nullValue;

		NonnullProperty( ValueProperty<R, T> path, T nullValue ) {
			super();
			assert ( nullValue != null );
			this.property = path;
			this.nullValue = nullValue;
		}

		@Override
		public T resolveIn( Path root, Values<? extends R> values ) {
			final T value = property.resolveIn( root, values );
			return value == null
				? nullValue
				: value;
		}

		@Override
		public String toString() {
			return property + " ?: " + nullValue;
		}
	}

	static class NotionalChainProperty<R, I, T>
			implements NotionalProperty<R, T> {

		private final NotionalProperty<R, I> parent;
		private final NotionalProperty<I, T> sub;

		NotionalChainProperty( NotionalProperty<R, I> parent, NotionalProperty<I, T> sub ) {
			super();
			this.parent = parent;
			this.sub = sub;
		}

		@Override
		public T compute( R value ) {
			return sub.compute( parent.compute( value ) );
		}

		public <V> NotionalProperty<R, V> dot( NotionalProperty<T, V> subpath ) {
			return new NotionalChainProperty<R, T, V>( this, subpath );
		}

	}

	static final class NotionalValueProperty<R, V, T>
			implements ValueProperty<R, T> {

		private final ValueProperty<R, V> value;
		private final NotionalProperty<V, T> notional;

		NotionalValueProperty( ValueProperty<R, V> value, NotionalProperty<V, T> notional ) {
			super();
			this.value = value;
			this.notional = notional;
		}

		@Override
		public T resolveIn( Path root, Values<? extends R> values ) {
			return notional.compute( value.resolveIn( root, values ) );
		}

	}

	static class StringLengthProperty
			implements NotionalProperty<String, Integer>, Nullsave {

		@Override
		public Integer compute( String value ) {
			return value == null
				? 0
				: value.length();
		}

	}

	static final class InheritanceTypeDescriptor
			implements RecordDescriptor {

		private final Class<?> type;

		InheritanceTypeDescriptor( Class<?> type ) {
			super();
			this.type = type;
		}

		@Override
		public boolean isAssured( Class<?> required ) {
			return required == type || type.isAssignableFrom( required );
		}

	}

	static final class DynamicRecordProperty<R, T>
			implements RecordProperty<R, T> {

		private final Class<T> type;
		private final Path name;
		private final Path defaultItem;

		DynamicRecordProperty( Class<T> type, Path name, Path defaultItem ) {
			super();
			this.type = type;
			this.name = name;
			this.defaultItem = defaultItem;
		}

		@Override
		public Dataset<T> resolveIn( Path root, Records records ) {
			final Path record = root.dot( name );
			Path descriptor = record.dot( Records.TYPE );
			if ( !existsRecord( descriptor, records ) ) {
				descriptor = record.dot( defaultItem ).dot( Records.TYPE );
				if ( !existsRecord( descriptor, records ) ) {
					return records.noneAs( type );
				}
			}
			return records.recordAt( descriptor, type );
		}

		private boolean existsRecord( Path descriptor, Records records ) {
			return exists( records.indexFor( key( descriptor ) ) );
		}

		@Override
		public String toString() {
			return type.getSimpleName() + " " + name;
		}
	}

	static final class ChildRecordProperty<R, T, M>
			implements RecordProperty<R, M> {

		private final RecordProperty<R, T> parent;
		private final RecordProperty<T, M> child;

		ChildRecordProperty( RecordProperty<R, T> parent, RecordProperty<T, M> child ) {
			super();
			this.parent = parent;
			this.child = child;
		}

		@Override
		public Dataset<M> resolveIn( Path root, Records records ) {
			return parent.resolveIn( root, records ).record( child );
		}

	}

	static final class RecordValueProperty<R, T, V>
			implements ValueProperty<R, V> {

		private final RecordProperty<R, T> record;
		private final ValueProperty<T, V> value;

		RecordValueProperty( RecordProperty<R, T> record, ValueProperty<T, V> value ) {
			super();
			this.record = record;
			this.value = value;
		}

		@Override
		public V resolveIn( Path root, Values<? extends R> values ) {
			return values.record( record ).value( value );
		}

	}

	static final class TypedProperty<R, T>
			implements ValueProperty<R, T> {

		private final Class<T> type;
		private final Path name;

		TypedProperty( Class<T> type, Path name ) {
			super();
			this.type = type;
			this.name = name;
		}

		@Override
		public T resolveIn( Path root, Values<? extends R> values ) {
			final int index = values.indexFor( key( root.dot( name ) ) );
			if ( !exists( index ) ) {
				return null;
			}
			Object value = values.at( index );
			return type.isInstance( value )
				? type.cast( value )
				: null;
		}

		@Override
		public String toString() {
			return type.getSimpleName() + " " + name;
		}
	}

}
