package de.jbee.dying;

public interface ICompareableList<T>
		extends List<T> {

	T maximum();

	T minimum();
}
