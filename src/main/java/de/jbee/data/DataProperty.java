/**
 * 
 */
package de.jbee.data;

import de.jbee.data.Dataset.RecordProperty;

/**
 * TODO the utility methods to transit into other paths should not be part of the interfaces.
 * 
 * @author Jan Bernitt (jan.bernitt@gmx.de)
 * 
 * @deprecated Contains just older ideas that should go into the {@link Dataset} API one day.
 */
@Deprecated
interface DataProperty<R, T> { // maybe we don't need a common Property interface since there are no methods in it

	interface PseudoProperty<R, T> { // in contrast to a NotionalPath is must not be a ValuePath (and the type of element stays the same?)
		// etwa 1. element eines ListPath

		// für counter: addition oder subtraktion eines fixen betrags auf ein counter 
	}

	interface RangeProperty<R, E>
			extends RecordProperty<R, E> {

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
	interface SelectionProperty<R, E>
			extends RecordProperty<R, E> { //OPEN maybe extend RangePath ??

		//TODO 
	}

}