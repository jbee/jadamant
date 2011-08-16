package de.jbee.dying;

import java.util.Comparator;

public abstract class ComparatorList<T, E extends List<T>>
		extends ExtendingList<T, E>
		implements ICompareableList<T> {

	private static final long serialVersionUID = 1L;

	private final Comparator<T> comparator;

	protected ComparatorList( List<T> list, Comparator<T> comparator ) {
		super( list );
		this.comparator = comparator;
	}

	@Override
	public final T maximum() {
		return maximumBy( comparator() );
	}

	protected final Comparator<T> comparator() {
		return comparator;
	}

	@Override
	public final T minimum() {
		return minimumBy( comparator() );
	}

	@Override
	public ICompareableList<T> comparedBy( Comparator<T> comparator ) {
		return comparator == comparator()
			? this
			: new CompareableList<T>( getCollection(), comparator );
	}
}
