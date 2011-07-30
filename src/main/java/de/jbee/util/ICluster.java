package de.jbee.util;

public interface ICluster<T>
		extends Iterable<T> {

	//TODO rename to length
	int size();

	boolean isEmpty();
}
