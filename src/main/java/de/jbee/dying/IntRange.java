package de.jbee.dying;

import java.util.Iterator;

import de.jbee.dying.Range.AbstractRange;

final class IntRange
		extends AbstractRange<Integer> {

	public static final IRange<Integer> EMPTY = new IntRange( 0, -1 );

	private final int start;
	private final int end;

	IntRange( int start, int end ) {
		this.start = start;
		this.end = end;
	}

	@Override
	protected boolean ge( Integer left, Integer right ) {
		return left >= right;
	}

	@Override
	protected boolean gt( Integer left, Integer right ) {
		return left > right;
	}

	@Override
	protected boolean le( Integer left, Integer right ) {
		return left <= right;
	}

	@Override
	protected boolean lt( Integer left, Integer right ) {
		return left < right;
	}

	@Override
	public boolean abut( IRange<Integer> other ) {
		return other.end() + 1 == start || other.start() - 1 == end;
	}

	public Integer end() {
		return end;
	}

	@Override
	public IRange<Integer> gap( IRange<Integer> other )
			throws UnsupportedOperationException {
		if ( overlaps( other ) ) {
			return EMPTY;
		}
		if ( other.end() < start ) {
			return new IntRange( other.end() + 1, start - 1 );
		}
		return new IntRange( end + 1, other.start() - 1 );
	}

	@Override
	public boolean isTotal() {
		return ( start == Integer.MIN_VALUE && end == Integer.MAX_VALUE )
				|| ( start == Integer.MAX_VALUE && end == Integer.MIN_VALUE );
	}

	@Override
	public Iterator<Integer> iterator() {
		return new IntRangeIterator( start, end );
	}

	@Override
	public int size() {
		return Math.abs( start - end );
	}

	public Integer start() {
		return start;
	}

	private static final class IntRangeIterator
			implements Iterator<Integer> {

		private int current;
		private final int end;
		private final int increment;

		IntRangeIterator( int start, int end ) {
			super();
			this.current = start;
			this.end = end;
			this.increment = Integer.signum( end - start );
		}

		@Override
		public boolean hasNext() {
			return current < end;
		}

		@Override
		public Integer next() {
			current += increment;
			return current;
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException( "Cannot remove a value from a range!" );
		}
	}

}
