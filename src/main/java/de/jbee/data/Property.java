package de.jbee.data;

import static de.jbee.lang.seq.IndexFor.exists;
import static de.jbee.lang.seq.Sequences.key;
import de.jbee.data.DataProperty.ItemProperty;
import de.jbee.data.DataProperty.MemberProperty;
import de.jbee.data.DataProperty.NotionalProperty;
import de.jbee.data.DataProperty.ValueProperty;
import de.jbee.data.Dataset.Items;
import de.jbee.data.Dataset.Members;
import de.jbee.lang.Sequence;
import de.jbee.lang.Table;
import de.jbee.lang.dev.Nullsave;
import de.jbee.lang.seq.Sequences;

/**
 * The util-class to work with {@link DataProperty}s and {@link Dataset}.
 * 
 * @author Jan Bernitt (jan.bernitt@gmx.de)
 * 
 */
public class Property {

	public static <R, T> MemberProperty<R, T> object( String name, Class<T> type ) {
		return new TypedObjectProperty<R, T>( type, Path.path( name ) );
	}

	public static <R, T> ValueProperty<R, T> value( String name, Class<T> type, T defaultValue ) {
		return new NonnullProperty<R, T>( new TypedProperty<R, T>( type, Path.path( name ) ),
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
		public T resolveIn( Path root, Table<?> values ) {
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
		public T resolveIn( Path root, Table<?> values ) {
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

	static final class TypedObjectProperty<R, T>
			implements MemberProperty<R, T> {

		private final Class<T> type;
		private final Path name;

		TypedObjectProperty( Class<T> type, Path name ) {
			super();
			this.type = type;
			this.name = name;
		}

		@Override
		public Dataset<T> resolveIn( Path root, Members members ) {
			final Path path = root.dot( name );
			return exists( members.indexFor( key( path.dot( Members.TYPE ) ) ) )
				//FIXME need to recognize start of list/map also
				? members.memberAt( path, type )
				: members.none( type );
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
		public T resolveIn( Path root, Table<?> values ) {
			final int index = values.indexFor( Sequences.key( root.dot( name ) ) );
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
