package de.jbee.dying;

public interface IMutableList<T>
		extends ICluster<T> {

	IMutableList<T> append( T e );

	IMutableList<T> append( ICluster<T> cluster );

	IMutableList<T> insert( int index, T e );

	IList<T> immutable();
}
