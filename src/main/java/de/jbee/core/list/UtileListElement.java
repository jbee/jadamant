package de.jbee.core.list;

import de.jbee.core.type.Eq;

public class UtileListElement {

	static final ListElement NONE = new NoListElement();
	static final ListElement FIRST = new AtIndexElement( 0 );
	static final ListElement LAST = new AtIndexElement( -1 );
	static final ListElement HEAD = FIRST;

	/**
	 * Always returns a element which yields {@link ListElement#NOT_CONTAINED} if index is negative.
	 * 
	 * @see #on(int)
	 */
	ListElement at( int index ) {
		return index < 0
			? NONE
			: index == 0
				? HEAD
				: new AtIndexElement( index );
	}

	/**
	 * Same as {@link #at(int)} for positive index values. Negative index values start from the
	 * lists end so -1 is the last element of the list, -2 the one before and so on.
	 * 
	 * @see #at(int)
	 */
	ListElement on( int index ) {
		return index == -1
			? LAST
			: index == 0
				? HEAD
				: new AtIndexElement( index );
	}

	//TODO same as eq with condition interface

	ListElement eq( Object sample, Eq<Object> equality ) {
		return nthEq( 1, sample, equality );
	}

	ListElement nthEq( int n, Object sample, Eq<Object> equality ) {
		return n == 0
			? NONE
			: new NthEqualElement( n, sample, equality, 1 );
	}

	static final class NoListElement
			implements ListElement {

		@Override
		public <E> int in( List<E> list ) {
			return NOT_CONTAINED;
		}

	}

	static final class AtIndexElement
			implements ListElement {

		private final int idx;

		AtIndexElement( int idx ) {
			super();
			this.idx = idx;
		}

		@Override
		public <E> int in( List<E> list ) {
			return Math.abs( idx ) < list.size()
				? idx < 0
					? list.size() + idx
					: idx
				: NOT_CONTAINED;
		}
	}

	static final class NthEqualElement
			implements ListElement {

		private final int n;
		private final int step;
		private final Object sample;
		private final Eq<Object> equality;

		NthEqualElement( int n, Object sample, Eq<Object> equality, int step ) {
			super();
			this.n = n;
			this.step = step;
			this.sample = sample;
			this.equality = equality;
			if ( step == 0 ) {
				throw new IllegalArgumentException( "increment must be less or greater than 0" );
			}
		}

		@Override
		public <E> int in( List<E> list ) {
			if ( list.isEmpty() ) {
				return NOT_CONTAINED;
			}
			int size = list.size();
			int equal = 0;
			int idx = step > 0
				? 0
				: size - 1;
			while ( idx >= 0 && idx < size ) {
				final E e = list.at( idx );
				if ( equality.eq( sample, e ) ) {
					equal++;
					if ( equal == n ) {
						return idx;
					}
				}
				idx += step;
			}
			return NOT_CONTAINED;
		}
	}
}
