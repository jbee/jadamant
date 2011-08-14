package de.jbee.dying;

public interface ICompareableList<T>
		extends IList<T> {

	T maximum();

	T minimum();
}
