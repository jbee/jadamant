package de.jbee.core.list;

import de.jbee.core.type.Enum;

public final class RichLister<E>
		implements EnumLister<E> {

	private final EnumLister<E> enriched;
	private final Enum<E> type;

	public RichLister( EnumLister<E> enriched, Enum<E> type ) {
		super();
		this.enriched = enriched;
		this.type = type;
	}

	public List<E> from( E start ) {
		return fromTo( start, type.maxBound() );
	}

	public List<E> stepwisefrom( E start, int increment ) {
		return stepwiseFromTo( start, type.maxBound(), increment );
	}

	public List<E> fromTo( E start, E end ) {
		return stepwiseFromTo( start, end, 1 );
	}

	public List<E> fromThen( E start, E then ) {
		return stepwiseFromTo( start, type.maxBound(), incrementBetween( start, then ) );
	}

	public List<E> fromThenTo( E start, E then, E end ) {
		return stepwiseFromTo( start, end, incrementBetween( start, then ) );
	}

	private int incrementBetween( E start, E then ) {
		return type.toOrdinal( then ) - type.toOrdinal( start );
	}

	@Override
	public List<E> stepwiseFromTo( E start, E end, int increment ) {
		return enriched.stepwiseFromTo( start, end, increment );
	}

}
