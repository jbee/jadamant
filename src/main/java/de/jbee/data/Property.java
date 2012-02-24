package de.jbee.data;

import static de.jbee.data.Path.itemPath;
import static de.jbee.data.Path.memberPath;
import static de.jbee.lang.seq.IndexFor.exists;
import static de.jbee.lang.seq.Sequences.key;
import de.jbee.data.Dataset.ItemProperty;
import de.jbee.data.Dataset.Items;
import de.jbee.data.Dataset.MemberDescriptor;
import de.jbee.data.Dataset.MemberProperty;
import de.jbee.data.Dataset.Members;
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

	public static MemberDescriptor type( Class<?> type ) {
		return new InheritanceTypeDescriptor( type );
	}

	public static <R, T> MemberProperty<R, T> object( String name, Class<T> type ) {
		return new DynamicMemberProperty<R, T>( type, memberPath( name ), itemPath( 1 ) );
	}

	public static <R, T> ValueProperty<R, T> value( String name, Class<T> type, T defaultValue ) {
		return new NonnullProperty<R, T>( new TypedProperty<R, T>( type, memberPath( name ) ),
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
			implements MemberDescriptor {

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

	static final class DynamicMemberProperty<R, T>
			implements MemberProperty<R, T> {

		private final Class<T> type;
		private final Path name;
		private final Path defaultItem;

		DynamicMemberProperty( Class<T> type, Path name, Path defaultItem ) {
			super();
			this.type = type;
			this.name = name;
			this.defaultItem = defaultItem;
		}

		@Override
		public Dataset<T> resolveIn( Path root, Members members ) {
			final Path member = root.dot( name );
			Path descriptor = member.dot( Members.TYPE );
			if ( !existsMember( descriptor, members ) ) {
				descriptor = member.dot( defaultItem ).dot( Members.TYPE );
				if ( !existsMember( descriptor, members ) ) {
					return members.noneAs( type );
				}
			}
			return members.memberAt( descriptor, type );
		}

		private boolean existsMember( Path descriptor, Members members ) {
			return exists( members.indexFor( key( descriptor ) ) );
		}

		@Override
		public String toString() {
			return type.getSimpleName() + " " + name;
		}
	}

	static final class MemberValueProperty<R, T, V>
			implements ValueProperty<R, V> {

		private final MemberProperty<R, T> member;
		private final ValueProperty<T, V> value;

		MemberValueProperty( MemberProperty<R, T> member, ValueProperty<T, V> value ) {
			super();
			this.member = member;
			this.value = value;
		}

		@Override
		public V resolveIn( Path root, Values<? extends R> values ) {
			return values.member( member ).value( value );
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
