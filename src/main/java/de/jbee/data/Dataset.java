package de.jbee.data;

import static de.jbee.data.Path.memberPath;
import de.jbee.lang.Ord;
import de.jbee.lang.ReducibleSequence;
import de.jbee.lang.Searchable;
import de.jbee.lang.Sequence;
import de.jbee.lang.Table;
import de.jbee.lang.Map.Entry;

public interface Dataset<T>
		extends Sequence<Object> {

	Ord<Object> ORDER = Entry.ORDER;

	<M> Dataset<M> member( MemberProperty<? super T, M> property );

	<I> I items( ItemProperty<T, I> property );

	<V> V value( ValueProperty<? super T, V> property );

	interface Values<T>
			extends Table<Object> {

		//TODO duplicate method also in Dataset...
		<M> Dataset<M> member( MemberProperty<? super T, M> property );

	}

	interface Members
			extends Table<Object> {

		/**
		 * A special value holding the type (interface) represents the values of this 'object'
		 * (through {@link DataProperty}s).
		 */
		Path TYPE = memberPath( "__type__" );

		<E> Dataset<E> memberAt( Path descriptor, Class<E> type );

		<E> Dataset<E> noneAs( Class<E> type );
	}

	interface Items<E>
			extends Searchable, ReducibleSequence<Dataset<E>> {

		Items<E> range( Path start, Path end );

		@Override
		Items<E> take( int count );

		@Override
		Items<E> drop( int count );
	}

	interface MemberDescriptor {

		boolean isAssured( Class<?> required );
	}

	interface MemberProperty<R, T> {

		Dataset<T> resolveIn( Path root, Members members );

		//		RangeProperty<R, T> repeat( int times );

		//		<S> ObjectProperty<R, S> dot( ObjectProperty<T, S> subpath );
		//
		//		<V> ValueProperty<R, V> dot( ValueProperty<T, V> subpath );
		//
		//		<E> RangeProperty<R, E> dot( RangeProperty<T, E> subpath );
	}

	/**
	 * Represents a <i>simple</i> (unstructured) value that is resolved from a data object by
	 * {@link #resolveIn(Table)}. The actual <i>"path"</i> is hidden inside of the property.
	 * 
	 * @author Jan Bernitt (jan.bernitt@gmx.de)
	 */
	interface ValueProperty<R, T> {

		/**
		 * A data container is telling this path to resolve the value returned to the caller of
		 * {@link Dataset#value(ValueProperty)}.
		 * 
		 * @return usually the <code>value</code> passed in as an argument.
		 */
		T resolveIn( Path root, Values<? extends R> values );

	}

	interface NotionalProperty<R, T> {

		T compute( R value );
	}

	interface ItemProperty<T, I> {

		I resolveIn( Items<T> items );
	}
}
