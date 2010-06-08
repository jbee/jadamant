package de.jbee.util;

public interface IMutableSet<T>
		extends ICluster<T> {

	IMutableSet<T> add( T e );

	ISet<T> immutable();
}
