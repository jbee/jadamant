package de.jbee.dying;

import java.util.Comparator;

class CompareableList<T>
		extends ComparatorList<T, CompareableList<T>> {

	protected CompareableList( List<T> list, Comparator<T> comparator ) {
		super( list, comparator );
	}

	@Override
	protected CompareableList<T> extend( List<T> list ) {
		return new CompareableList<T>( list, comparator() );
	}

}
