package de.jbee.data;

import static de.jbee.lang.ListIndex.NOT_CONTAINED;
import de.jbee.data.Data.DataTable;
import de.jbee.data.DataProperty.NotionalProperty;
import de.jbee.data.DataProperty.ObjectProperty;
import de.jbee.data.DataProperty.ValueProperty;
import de.jbee.lang.List;
import de.jbee.lang.Map;
import de.jbee.lang.Table;
import de.jbee.lang.dev.Nullsave;
import de.jbee.lang.seq.Sequences;

/**
 * The util-class to work with {@link DataProperty}s and {@link Data}.
 * 
 * @author Jan Bernitt (jan.bernitt@gmx.de)
 * 
 */
public class Property {

	/**
	 * A special value holding the type (interface) represents the values of this 'object' (through
	 * {@link DataProperty}s).
	 */
	public static final Path OBJECT_TYPE = Path.path( ".object" );

	static final class TypedObjectProperty<R, T>
			implements ObjectProperty<R, T> {

		private final Class<T> type;
		private final Path name;

		TypedObjectProperty( Class<T> type, Path name ) {
			super();
			this.type = type;
			this.name = name;
		}

		@SuppressWarnings ( "unchecked" )
		@Override
		public Data<T> resolveIn( Path prefix, DataTable<?> values ) {
			final Path path = prefix.dot( name );
			int objectTypeIndex = values.indexFor( Sequences.key( path.dot( OBJECT_TYPE ) ) );
			if ( objectTypeIndex != NOT_CONTAINED && values.at( objectTypeIndex ) == type ) {
				int objectEndIndex = List.indexFor.insertBy(
						Sequences.entry( Sequences.key( path.toString() + Path.SEPARATOR
								+ Map.Key.PREFIX_TERMINATOR ), type ), values.order() ).in( values );
				if ( objectEndIndex - objectTypeIndex > 1 ) {
					return (Data<T>) values.slice( path, objectTypeIndex, objectEndIndex );
				}
			}
			return (Data<T>) values.slice( path, 0, 0 );
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
		public T resolveIn( Path prefix, Table<?> values ) {
			return notional.compute( value.resolveIn( prefix, values ) );
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
		public T resolveIn( Path prefix, Table<?> values ) {
			final int index = values.indexFor( Sequences.key( prefix.dot( name ) ) );
			if ( index == NOT_CONTAINED ) {
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
		public T resolveIn( Path prefix, Table<?> values ) {
			final T value = property.resolveIn( prefix, values );
			return value == null
				? nullValue
				: value;
		}

		@Override
		public String toString() {
			return property + " ?: " + nullValue;
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

	public static <T> ValueProperty<T, Integer> value( String name, int defaultValue ) {
		return new NonnullProperty<T, Integer>( new TypedProperty<T, Integer>( Integer.class,
				Path.path( name ) ), defaultValue );
	}

	public static <R, T> ObjectProperty<R, T> object( String name, Class<T> type ) {
		return new TypedObjectProperty<R, T>( type, Path.path( name ) );
	}
}
