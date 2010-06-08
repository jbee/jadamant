package de.jbee.util;

public interface IMutableList<T>
		extends ICluster<T> {

	IMutableList<T> append( T e );

	IMutableList<T> insert( int index, T e );

	IList<T> immutable();
}
