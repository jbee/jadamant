package de.jbee.data;

import static de.jbee.data.Path.recordPath;
import de.jbee.lang.Ord;
import de.jbee.lang.ReducibleSequence;
import de.jbee.lang.Searchable;
import de.jbee.lang.Sequence;
import de.jbee.lang.Table;
import de.jbee.lang.Map.Entry;

public interface Dataset<T>
		extends Sequence<Object> {

	Ord<Object> ORDER = Entry.ORDER;

	<V> V value( ValueProperty<? super T, V> property );

	<R> Dataset<R> record( RecordProperty<? super T, R> property );

	<I> I items( ItemProperty<T, I> property );

	/**
	 * Represents a <i>simple</i> (unstructured) value that is resolved from a data object by
	 * {@link #resolveIn(Table)}. The actual <i>"path"</i> is hidden inside of the property.
	 * 
	 * @author Jan Bernitt (jan.bernitt@gmx.de)
	 */
	interface ValueProperty<T, V> {

		/**
		 * A data container is telling this path to resolve the value returned to the caller of
		 * {@link Dataset#value(ValueProperty)}.
		 * 
		 * @return usually the <code>value</code> passed in as an argument.
		 */
		V resolveIn( Path root, Values<? extends T> values );

	}

	/**
	 * A property that doesn't exist physically as a value or record but can be computed from one. A
	 * typical example is a {@link String} value's <code>length</code>.
	 * 
	 * @author Jan Bernitt (jan.bernitt@gmx.de)
	 */
	interface VirtualProperty<T, V> {

		V compute( T value );
	}

	interface Values<T>
			extends Table<Object> {

		//TODO duplicate method also in Dataset...
		<R> Dataset<R> record( RecordProperty<? super T, R> property );

	}

	interface RecordProperty<T, R> {

		Dataset<R> resolveIn( Path root, Records records );

	}

	interface Records
			extends Table<Object> {

		/**
		 * A special value holding the type (interface) represents the values of this 'object'
		 * (through {@link DataProperty}s).
		 */
		Path TYPE = recordPath( "__type__" );

		<E> Dataset<E> recordAt( Path format, Class<E> type );

		<E> Dataset<E> noneAs( Class<E> type );
	}

	interface ItemProperty<T, I> {

		I resolveIn( Items<T> items );
	}

	interface Items<E>
			extends Searchable, ReducibleSequence<Dataset<E>> {

		Items<E> range( Path start, Path end );

		@Override
		Items<E> take( int count );

		@Override
		Items<E> drop( int count );
	}

	interface Itemised<T> {

		Dataset<T> item( Path item );
	}

}
