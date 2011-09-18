package de.jbee.dying;

import java.util.IdentityHashMap;
import java.util.Map;

public final class Range {

	private Range() {
		// util
	}

	private static Map<Class<?>, IRange<?>> EMPTY_RANGES = new IdentityHashMap<Class<?>, IRange<?>>();

	static {
		EMPTY_RANGES.put( Integer.class, IntRange.EMPTY );
		EMPTY_RANGES.put( int.class, IntRange.EMPTY );
	}

	public static abstract class AbstractRange<T>
			implements IRange<T> {

		protected abstract boolean le( T left, T right );

		protected abstract boolean lt( T left, T right );

		protected abstract boolean ge( T left, T right );

		protected abstract boolean gt( T left, T right );

		@Override
		public final boolean includes( T e ) {
			return ge( e, start() ) && le( e, end() );
		}

		@Override
		public final boolean includes( IRange<T> other ) {
			return ge( other.start(), start() ) && le( other.end(), end() );
		}

		public final boolean isEmpty() {
			return gt( start(), end() );
		}

		@Override
		public final boolean overlaps( IRange<T> other ) {
			return ( ge( other.end(), start() ) && lt( other.start(), start() ) )
					|| ( le( other.start(), end() ) && gt( other.end(), end() ) );
		}

	}

	public static IRange<Integer> between( int startInclusive, int endInclusive ) {
		return new IntRange( startInclusive, endInclusive );
	}

	@SuppressWarnings ( "unchecked" )
	public static <T> IRange<Integer> empty( Class<T> type ) {
		final IRange<?> res = EMPTY_RANGES.get( type );
		if ( res != null ) {
			return (IRange<Integer>) res;
		}
		throw new UnsupportedOperationException( "No empty range available for type: " + type );
	}
}
