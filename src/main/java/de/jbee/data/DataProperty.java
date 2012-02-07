/**
 * 
 */
package de.jbee.data;

import de.jbee.lang.Map;
import de.jbee.lang.Sequence;
import de.jbee.lang.Table;

/**
 * TODO the utility methods to transit into other paths should not be part of the interfaces.
 * 
 * @author Jan Bernitt (jan.bernitt@gmx.de)
 */
public interface DataProperty<R, T> { // maybe we don't need a common Property interface since there are no methods in it

	char SEPARATOR = '.';

	//TODO Map.Entry<T> entry( T e );

	//OPEN how to access all names of a ListPath ? --> we add another getter for a last element and use ranges ?

	//OPEN given a value T a path has to be able to build the appropriate key/value pairs otherwise we could not build the data so the path will know how to get it out again.

	public static interface PseudoProperty<R, T> { // in contrast to a NotionalPath is must not be a ValuePath (and the type of element stays the same?)
		// etwa 1. element eines ListPath

		// für counter: addition oder subtraktion eines fixen betrags auf ein counter 
	}

	public static interface RangeProperty<R, E>
			extends ObjectProperty<R, E> {

		//OPEN maybe we just support ranges - otherwise a List of Paths can be used to model some cherry picking in a list

		ObjectProperty<R, E> head(); //OPEN how to model lists of values and objects ?

		ObjectProperty<R, E> at( int index );

		RangeProperty<R, E> range( int start, int end );

		RangeProperty<R, E> limit( int maxlength );

		Map<E> entries( Sequence<E> elems );
	}

	/**
	 * A list consists of theoretical randomly selected elements (of same type). They might be
	 * selected from multiple different base-paths as well.
	 * 
	 * @author Jan Bernitt (jan.bernitt@gmx.de)
	 */
	public static interface SelectionProperty<R, E>
			extends ObjectProperty<R, E> { //OPEN maybe extend RangePath ??

		//TODO 
	}

	public static interface ObjectProperty<R, T>
			extends DataProperty<R, T> {

		//IData<T> dig( IData<? extends R> data );

		RangeProperty<R, T> repeat( int times );

		<S> ObjectProperty<R, S> dot( ObjectProperty<T, S> subpath );

		<V> ValueProperty<R, V> dot( ValueProperty<T, V> subpath );

		<E> RangeProperty<R, E> dot( RangeProperty<T, E> subpath );
	}

	/**
	 * Represents a <i>simple</i> (unstructured) value that is resolved from a data object by
	 * {@link #resolveIn(Table)}. The actual <i>"path"</i> is hidden inside of the property.
	 * 
	 * @author Jan Bernitt (jan.bernitt@gmx.de)
	 */
	public static interface ValueProperty<R, T>
			extends DataProperty<R, T> {

		/**
		 * A data container is telling this path to resolve the value returned to the caller of
		 * {@link Data#value(ValueProperty)}.
		 * 
		 * @return usually the <code>value</code> passed in as an argument.
		 */
		T resolveIn( Table<?> values );

	}

	public static interface NotionalProperty<R, T> {

		T compute( R value );
	}

}