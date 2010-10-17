package de.jbee.util;

import java.util.Comparator;

public class NumberList<N extends Number & Comparable<N>>
		extends ComparatorList<N, NumberList<N>> {

	private static final long serialVersionUID = 1L;

	NumberList( IList<N> list ) {
		this( list, Collection.<N> comparator() );
	}

	private NumberList( IList<N> list, Comparator<N> comparator ) {
		super( list, comparator );
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

	@Override
	public NumberList<N> comparedBy( Comparator<N> comparator ) {
		return new NumberList<N>( getCollection(), comparator );
	}
}
