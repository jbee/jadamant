package de.jbee.dying;

public interface IMutableSet<T>
		extends ICluster<T> {

	IMutableSet<T> add( T e );

	ISet<T> immutable();

	boolean isIn( T e );
}
