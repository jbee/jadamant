package de.jbee.util;

import de.jbee.lang.dev.Nullness;
import de.jbee.lang.dev.Nulls;

@Nulls ( returns = Nullness.NOT_ALLOWED, parameters = Nullness.NOT_ALLOWED )
public interface IRange<T>
		extends ICluster<T>, Iterable<T> {

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
