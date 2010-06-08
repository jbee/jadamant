package de.jbee.util;

public interface ICluster<T>
		extends Iterable<T> {

	int size();

	boolean isEmpty();
}
