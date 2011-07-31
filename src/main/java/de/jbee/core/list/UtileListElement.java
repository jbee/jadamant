package de.jbee.core.list;

import de.jbee.core.type.Eq;
import de.jbee.core.type.Equal;

public class UtileListElement {

	static final ListElement NONE = new NoListElement();
	static final ListElement FIRST = new OnPositionListElement( 0 );
	static final ListElement LAST = new OnPositionListElement( -1 );
	static final ListElement HEAD = FIRST;

	ListElement nextTo( int index ) {
		return at( index + 1 );
	}

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
				: new OnPositionListElement( index );
	}

	/**
	 * Same as {@link #at(int)} for positive pos values. Negative pos values start from the lists
	 * end so -1 is the last element of the list, -2 the one before and so on.
	 * 
	 * @see #at(int)
	 */
	ListElement on( int pos ) {
		return pos == -1
			? LAST
			: pos == 0
				? HEAD
				: new OnPositionListElement( pos );
	}

	//TODO same as eq with condition interface

	ListElement eq( Object sample, Eq<Object> equality ) {
		return nthEq( 1, sample, equality );
	}

	ListElement nthEq( int n, Object sample, Eq<Object> equality ) {
		return n == 0
			? NONE
			: new NthEqualListElement( n, sample, equality, 1 );
	}

	ListElement duplicate() {
		return duplicate( Equal.equality );
	}

	ListElement duplicate( Eq<Object> eq ) {
		return duplicate( 0, eq );
	}

	ListElement duplicate( int start, Eq<Object> eq ) {
		return new DuplicateListElement( start, eq );
	}

	static final class NoListElement
			implements ListElement {

		@Override
		public <E> int in( List<E> list ) {
			return NOT_CONTAINED;
		}

	}

	static final class DuplicateListElement
			implements ListElement {

		private final int start;
		private final Eq<Object> eq;

		DuplicateListElement( int start, Eq<Object> eq ) {
			super();
			this.start = start;
			this.eq = eq;
		}

		@Override
		public <E> int in( List<E> list ) {
			int size = list.size();
			for ( int i = start; i < size - 1; i++ ) {
				final E e = list.at( i );
				for ( int j = i + 1; j < size; j++ ) {
					if ( eq.eq( e, list.at( j ) ) ) {
						return j;
					}
				}
			}
			return NOT_CONTAINED;
		}
	}

	static final class OnPositionListElement
			implements ListElement {

		private final int pos;

		OnPositionListElement( int pos ) {
			super();
			this.pos = pos;
		}

		@Override
		public <E> int in( List<E> list ) {
			return Math.abs( pos ) < list.size()
				? pos < 0
					? list.size() + pos
					: pos
				: NOT_CONTAINED;
		}
	}

	static final class NthEqualListElement
			implements ListElement {

		private final int n;
		private final int step;
		private final Object sample;
		private final Eq<Object> equality;

		NthEqualListElement( int n, Object sample, Eq<Object> equality, int step ) {
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
