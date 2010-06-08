package de.jbee.util;

import java.util.Comparator;

public abstract class ComparableList<T extends Comparable<T>, E extends IList<T>>
		extends ExtendingList<T, E> {

	private static final long serialVersionUID = 1L;

	protected ComparableList( IList<T> list ) {
		super( list );
	}

	public T maximum() {
		return maximumBy( comparator() );
	}

	private Comparator<T> comparator() {
		return Collection.<T> comparator();
	}

	public T minimum() {
		return minimumBy( comparator() );
	}
}
