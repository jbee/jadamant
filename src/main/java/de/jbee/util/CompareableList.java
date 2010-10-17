package de.jbee.util;

import java.util.Comparator;

class CompareableList<T>
		extends ComparatorList<T, CompareableList<T>> {

	protected CompareableList( IList<T> list, Comparator<T> comparator ) {
		super( list, comparator );
	}

	@Override
	protected CompareableList<T> extend( IList<T> list ) {
		return new CompareableList<T>( list, comparator() );
	}

}
