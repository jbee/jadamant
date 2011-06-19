package de.jbee.util;

import de.jbee.core.IndexAccessible;
import de.jbee.core.Prepandable;

/**
 * All collections build out of ordered elements are {@link ISequence}s.
 * 
 * This interface intentionally kept compact since it also should be used for any kind
 * data-structure used as backing implementation of wide collection APIs.
 * 
 * Modifying operation have to leave the called instance unchanged (immutable behaviour).
 * 
 * 
 * @author Jan Bernitt (jan.bernitt@gmx.de)
 * 
 * @param <T>
 */
public interface ISequence<T>
		extends ICluster<T>, IndexAccessible<T>, Prepandable<T> {

	T at( int index, T outOffBoundsValue );

	T head()
			throws IndexOutOfBoundsException;

	T head( T emptyValue );

	T last()
			throws IndexOutOfBoundsException;

	T last( T emptyValue );

	ISequence<T> tail();

}
