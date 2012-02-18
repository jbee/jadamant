package de.jbee.data;

import static de.jbee.data.Path.element;
import static de.jbee.lang.seq.IndexFor.exists;
import static de.jbee.lang.seq.IndexFor.insertionIndex;
import static de.jbee.lang.seq.Sequences.keyFirstStartsWith;
import static de.jbee.lang.seq.Sequences.keyLastStartsWith;
import de.jbee.data.Data.DataTable;
import de.jbee.data.DataProperty.NotionalProperty;
import de.jbee.data.DataProperty.ObjectProperty;
import de.jbee.data.DataProperty.RangeProperty;
import de.jbee.data.DataProperty.ValueProperty;
import de.jbee.lang.Map;
import de.jbee.lang.Table;
import de.jbee.lang.Map.Key;
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

	public static <R, T> ObjectProperty<R, T> object( String name, Class<T> type ) {
		return new TypedObjectProperty<R, T>( type, Path.path( name ) );
	}

	public static <R, T> RangeProperty<R, T> objects( String name, Class<T> elementType, int start,
			int end ) {
		return new TypedRangeProperty<R, T>( elementType, Path.path( name ), start, end );
	}

	public static <R, T> RangeProperty<R, T> objects( String string, Class<T> elementType ) {
		return objects( string, elementType, 0, -1 );
	}

	public static <R, T> ValueProperty<R, T> value( String name, Class<T> type, T defaultValue ) {
		return new NonnullProperty<R, T>( new TypedProperty<R, T>( type, Path.path( name ) ),
				defaultValue );
	}

	public static <R> ValueProperty<R, Integer> value( String name, int defaultValue ) {
		return value( name, Integer.class, defaultValue );
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
		public T resolveIn( Path prefix, Table<?> values ) {
			return notional.compute( value.resolveIn( prefix, values ) );
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
			if ( exists( objectTypeIndex ) && values.at( objectTypeIndex ) == type ) { //FIXME type can be a supertype of the actual available obj.
				Key key = Sequences.key( path.toString() + Path.SEPARATOR + ""
						+ Map.Key.PREFIX_TERMINATOR );
				int objectEndIndex = insertionIndex( values.indexFor( key ) );
				if ( objectEndIndex - objectTypeIndex > 1 ) {
					return (Data<T>) values.slice( path, objectTypeIndex, objectEndIndex );
				}
			}
			return (Data<T>) values.slice( path, 0, 0 );
		}

		@Override
		public String toString() {
			return type.getSimpleName() + " " + name;
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

	static final class TypedRangeProperty<R, E>
			implements RangeProperty<R, E> {

		private final Class<E> elementType;
		private final Path name;
		private final int start;
		private final int end;

		TypedRangeProperty( Class<E> elementType, Path name, int start, int end ) {
			super();
			this.elementType = elementType;
			this.name = name;
			this.start = start;
			this.end = end;
		}

		@Override
		public Data<E> resolveIn( Path prefix, DataTable<?> values ) {
			Path path = prefix.dot( name );
			int startIndex = insertionIndex( values.indexFor( keyFirstStartsWith( path.dot( element( start + 1 ) ) ) ) );
			Path endPath = end < 0
				? path
				: path.dot( element( end + 1 ) );
			int endIndex = insertionIndex( values.indexFor( keyLastStartsWith( endPath ) ) );
			return (Data<E>) values.slice( path, startIndex, endIndex );
		}

		@Override
		public String toString() {
			return elementType.getSimpleName() + " " + name + "[" + start + ":" + end + "]";
		}

	}

}
