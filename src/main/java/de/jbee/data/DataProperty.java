/**
 * 
 */
package de.jbee.data;

import de.jbee.data.Data.DataTable;
import de.jbee.lang.Table;

/**
 * TODO the utility methods to transit into other paths should not be part of the interfaces.
 * 
 * @author Jan Bernitt (jan.bernitt@gmx.de)
 */
public interface DataProperty<R, T> { // maybe we don't need a common Property interface since there are no methods in it

	public static interface PseudoProperty<R, T> { // in contrast to a NotionalPath is must not be a ValuePath (and the type of element stays the same?)
		// etwa 1. element eines ListPath

		// für counter: addition oder subtraktion eines fixen betrags auf ein counter 
	}

	public static interface RangeProperty<R, E>
			extends ObjectProperty<R, E> {

		//		ObjectProperty<R, E> head(); //OPEN how to model lists of values and objects ?
		//
		//		ObjectProperty<R, E> at( int index );
		//
		//		RangeProperty<R, E> range( int start, int end );
		//
		//		RangeProperty<R, E> limit( int maxlength );

		//Map<E> entries( Sequence<E> elems );
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

		Data<T> resolveIn( Path prefix, DataTable<?> values );

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
	public static interface ValueProperty<R, T>
			extends DataProperty<R, T> {

		/**
		 * A data container is telling this path to resolve the value returned to the caller of
		 * {@link Data#value(ValueProperty)}.
		 * 
		 * @return usually the <code>value</code> passed in as an argument.
		 */
		T resolveIn( Path prefix, Table<?> values );

	}

	public static interface NotionalProperty<R, T> {

		T compute( R value );
	}

}