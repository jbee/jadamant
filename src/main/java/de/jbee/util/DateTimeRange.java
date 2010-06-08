package de.jbee.util;

import java.util.Iterator;

import org.joda.time.DateTime;
import org.joda.time.Days;

import de.jbee.util.Range.AbstractRange;

final class DateTimeRange
		extends AbstractRange<DateTime> {

	public static final IRange<DateTime> EMPTY = new DateTimeRange( new DateTime( 0L ),
			new DateTime( -1L ) );

	/**
	 * a point in time equal to or before end
	 */
	private final DateTime start;
	/**
	 * a point in time equal to or after start;
	 */
	private final DateTime end;

	static IRange<DateTime> auto( DateTime start, DateTime end ) {
		return new DateTimeRange( start.isAfter( end )
			? end
			: start, start.isAfter( end )
			? start
			: end );
	}

	DateTimeRange( DateTime start, DateTime end ) {
		super();
		this.start = start;
		this.end = end;
	}

	@Override
	public boolean abut( IRange<DateTime> other ) {
		return other.end().getMillis() + 1L == start().getMillis()
				|| other.start().getMillis() - 1L == end().getMillis();
	}

	@Override
	public DateTime end() {
		return end;
	}

	@Override
	public IRange<DateTime> gap( IRange<DateTime> other )
			throws UnsupportedOperationException {
		if ( overlaps( other ) ) {
			return EMPTY;
		}
		if ( lt( other.end(), start() ) ) {
			return new DateTimeRange( new DateTime( other.end().getMillis() + 1L ), new DateTime(
					start.getMillis() - 1L ) );
		}
		return new DateTimeRange( new DateTime( end.getMillis() + 1L ), new DateTime(
				other.start().getMillis() - 1L ) );
	}

	@Override
	public DateTime start() {
		return start;
	}

	@Override
	protected boolean ge( DateTime left, DateTime right ) {
		return left.isEqual( right ) || left.isAfter( right );
	}

	@Override
	protected boolean gt( DateTime left, DateTime right ) {
		return left.isAfter( right );
	}

	@Override
	protected boolean le( DateTime left, DateTime right ) {
		return left.isEqual( right ) || left.isBefore( right );
	}

	@Override
	protected boolean lt( DateTime left, DateTime right ) {
		return left.isBefore( right );
	}

	@Override
	public int size() {
		final long diff = Math.abs( end.getMillis() - start.getMillis() );
		return ( diff > Long.valueOf( Integer.MAX_VALUE ) )
			? Integer.MAX_VALUE
			: (int) diff;
	}

	@Override
	public Iterator<DateTime> iterator() {
		return new DateTimeRangeIterator( start, end );
	}

	@Override
	public boolean isTotal() {
		return start.getMillis() == Long.MIN_VALUE && end.getMillis() == Long.MAX_VALUE;
	}

	private static final class DateTimeRangeIterator
			implements Iterator<DateTime> {

		private final DateTime end;
		private DateTime current;

		DateTimeRangeIterator( DateTime start, DateTime end ) {
			super();
			this.current = start;
			this.end = end;
		}

		@Override
		public boolean hasNext() {
			return current.isBefore( end );
		}

		@Override
		public DateTime next() {
			current = current.plus( Days.ONE );
			return current;
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException( "Cannot remove a value from a range!" );
		}

	}
}
