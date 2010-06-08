package de.jbee.util;

public class NumberList<N extends Number & Comparable<N>>
		extends ComparableList<N, NumberList<N>> {

	private static final long serialVersionUID = 1L;

	NumberList( IList<N> list ) {
		super( list );
	}

	@Override
	protected NumberList<N> extend( IList<N> list ) {
		return new NumberList<N>( list );
	}

	public int sumInt() {
		return foldL( NumberOperator.<N> plusInt(), 0 );
	}

	public Conditional<NumberList<N>> split( N at ) {
		return extend( split( at, Collection.<N> comparator() ) );
	}
}
