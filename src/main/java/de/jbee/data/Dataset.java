package de.jbee.data;

import static de.jbee.data.Path.path;
import de.jbee.data.DataProperty.ItemProperty;
import de.jbee.data.DataProperty.MemberProperty;
import de.jbee.data.DataProperty.ValueProperty;
import de.jbee.lang.Ord;
import de.jbee.lang.ReducibleSequence;
import de.jbee.lang.Searchable;
import de.jbee.lang.Sequence;
import de.jbee.lang.Table;
import de.jbee.lang.Map.Entry;

public interface Dataset<T>
		extends Sequence<Object> {

	Ord<Object> ORDER = Entry.ORDER;

	<S> Dataset<S> member( MemberProperty<? super T, S> property );

	<I> I items( ItemProperty<T, I> property );

	<V> V value( ValueProperty<? super T, V> property );

	interface Members
			extends Table<Object> {

		/**
		 * A special value holding the type (interface) represents the values of this 'object'
		 * (through {@link DataProperty}s).
		 */
		Path TYPE = path( "__type__" );

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

	interface TypeDescriptor {

		boolean isAssured( Class<?> required );
	}
}
