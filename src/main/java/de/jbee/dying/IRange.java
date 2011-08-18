package de.jbee.dying;

import de.jbee.lang.dev.Nullness;
import de.jbee.lang.dev.Nulls;

@Nulls ( returns = Nullness.NOT_ALLOWED, parameters = Nullness.NOT_ALLOWED )
public interface IRange<T>
		extends Iterable<T> { //FIXME impl Sequence 

	T start();

	T end();

	boolean includes( T e );

	boolean includes( IRange<T> other );

	boolean overlaps( IRange<T> other );

	boolean abut( IRange<T> other );

	boolean isTotal();

	IRange<T> gap( IRange<T> other )
			throws UnsupportedOperationException;
}
