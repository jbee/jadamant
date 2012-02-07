package de.jbee.data;

import de.jbee.data.DataProperty.NotionalProperty;
import de.jbee.data.DataProperty.ValueProperty;
import de.jbee.lang.ListIndex;
import de.jbee.lang.Table;
import de.jbee.lang.seq.Sequences;

/**
 * The util class to work with {@link DataProperty}s and {@link Data}.
 * 
 * @author Jan Bernitt (jan.bernitt@gmx.de)
 * 
 */
public class Property {

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
		public T resolveIn( Table<?> values ) {
			return notional.compute( value.resolveIn( values ) );
		}

	}

	static final class TypedProperty<R, T>
			implements ValueProperty<R, T> {

		private final Class<T> type;
		private final String name;

		TypedProperty( Class<T> type, String name ) {
			super();
			this.type = type;
			this.name = name;
		}

		@Override
		public T resolveIn( Table<?> values ) {
			int index = values.indexFor( Sequences.key( name ) );
			if ( index == ListIndex.NOT_CONTAINED ) {
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
		public T resolveIn( Table<?> values ) {
			final T value = property.resolveIn( values );
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
			implements NotionalProperty<String, Integer> {

		@Override
		public Integer compute( String value ) {
			return value.length();
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
}
