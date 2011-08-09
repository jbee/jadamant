package de.jbee.util;

public interface ICluster<T>
		extends Iterable<T> {

	//TODO rename to length
	int size();

	boolean isEmpty();

	//OPEN maybe the classic Cluster is the new Collection ?
	//OPEN replace the Iterable with a better way to access contained elements ?
}
