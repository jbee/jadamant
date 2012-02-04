package de.jbee.lang.seq;

import de.jbee.lang.Enum;
import de.jbee.lang.Enumerate;
import de.jbee.lang.Enumerator;
import de.jbee.lang.EnumeratorFactory;
import de.jbee.lang.List;

public class Range<E>
		implements Enumerator<E> {

	public static final RangeTo factory( EnumeratorFactory factory ) {
		return new RangeTo( factory );
	}

	private final Enumerator<E> utilised;
	private final Enum<E> type;

	public Range( Enumerator<E> utilised, Enum<E> type ) {
		super();
		this.utilised = utilised;
		this.type = type;
	}

	public List<E> from( E first ) {
		return fromTo( first, type.maxBound() );
	}

	public List<E> stepwisefrom( E first, int increment ) {
		return stepwiseFromTo( first, type.maxBound(), increment );
	}

	public List<E> fromTo( E first, E last ) {
		return stepwiseFromTo( first, last, 1 );
	}

	public List<E> fromThen( E first, E then ) {
		return stepwiseFromTo( first, type.maxBound(), incrementBetween( first, then ) );
	}

	public List<E> fromThenTo( E first, E then, E last ) {
		return stepwiseFromTo( first, last, incrementBetween( first, then ) );
	}

	public List<E> fromToCircular( E first, E last ) {
		int lastOrd = type.toOrdinal( last );
		int firstOrd = type.toOrdinal( first );
		if ( lastOrd == firstOrd ) {
			return fromTo( type.minBound(), type.maxBound() );
		}
		if ( lastOrd > firstOrd ) {
			return fromTo( first, type.minBound() ).concat( fromTo( type.maxBound(), last ) );
		}
		return fromTo( first, type.maxBound() ).concat( fromTo( type.minBound(), last ) );
	}

	private int incrementBetween( E first, E then ) {
		return type.toOrdinal( then ) - type.toOrdinal( first );
	}

	@Override
	public List<E> stepwiseFromTo( E first, E last, int increment ) {
		return utilised.stepwiseFromTo( first, last, increment );
	}

	public static final class RangeTo
			implements EnumeratorFactory {

		private final EnumeratorFactory factory;

		RangeTo( EnumeratorFactory factory ) {
			super();
			this.factory = factory;
		}

		@Override
		public <E> Range<E> enumerate( Enum<E> type ) {
			return new Range<E>( factory.enumerate( type ), type );
		}

		public <E extends java.lang.Enum<E>> Range<E> enumerate( Class<E> type ) {
			return enumerate( Enumerate.type( type ) );
		}

	}
}
