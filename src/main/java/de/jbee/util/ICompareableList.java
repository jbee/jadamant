package de.jbee.util;

public interface ICompareableList<T>
		extends IList<T> {

	T maximum();

	T minimum();
}
