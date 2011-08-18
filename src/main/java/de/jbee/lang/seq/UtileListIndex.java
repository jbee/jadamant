package de.jbee.lang.seq;

import de.jbee.lang.Eq;
import de.jbee.lang.Equal;
import de.jbee.lang.Fulfills;
import de.jbee.lang.List;
import de.jbee.lang.ListIndex;
import de.jbee.lang.Ord;
import de.jbee.lang.Order;
import de.jbee.lang.Ordering;
import de.jbee.lang.Predicate;
import de.jbee.lang.Sequence;
import de.jbee.lang.dev.Null;

public class UtileListIndex {

	static final ListIndex NONE = new NotContainedListIndex();
	static final ListIndex FIRST = new OnPositionListIndex( 0 );
	static final ListIndex LAST = new OnPositionListIndex( -1 );
	static final ListIndex HEAD = FIRST;

	public ListIndex lastUpTo( int count ) {
		return null; //FIXME liefert den letzten index bis zu einem maximum count - dann immer count -> für take/drop   
	}

	public ListIndex head() {
		return HEAD;
	}

	public ListIndex nextTo( int index ) {
		return elemAt( index + 1 );
	}

	public ListIndex nextTo( ListIndex index ) {
		return nthAfter( 1, index );
	}

	public ListIndex nthAfter( int offset, ListIndex index ) {
		return offset == 0
			? index
			: new RelativListIndex( index, offset );
	}

	public ListIndex nthBefore( int offset, ListIndex index ) {
		return offset == 0
			? index
			: new RelativListIndex( index, -offset );
	}

	/**
	 * Always returns a element which yields {@link ListIndex#NOT_CONTAINED} if index is negative.
	 * 
	 * @see #elemOn(int)
	 */
	public ListIndex elemAt( int index ) {
		return index < 0
			? NONE
			: index == 0
				? HEAD
				: new OnPositionListIndex( index );
	}

	/**
	 * Same as {@link #elemAt(int)} for positive pos values. Negative pos values start from the
	 * lists end so -1 is the last element of the list, -2 the one before and so on.
	 * 
	 * @see #elemAt(int)
	 */
	public ListIndex elemOn( int pos ) {
		return pos == -1
			? LAST
			: pos == 0
				? HEAD
				: new OnPositionListIndex( pos );
	}

	public ListIndex firstFalse( Predicate<Object> predicate ) {
		return nthTrue( 1, Fulfills.not( predicate ) );
	}

	public ListIndex firstTrue( Predicate<Object> predicate ) {
		return nthTrue( 1, predicate );
	}

	public ListIndex nthTrue( int n, Predicate<Object> predicate ) {
		return new NthEqualListIndex( n, predicate, 1 );
	}

	public ListIndex elem( Object e ) {
		return elem( e, Equal.equals );
	}

	public ListIndex elem( Object e, Eq<Object> eq ) {
		return nthElem( 1, e, eq );
	}

	public ListIndex notElem( Object e, Eq<Object> eq ) {
		return elem( e, Equal.not( eq ) );
	}

	public ListIndex notElem( Object e ) {
		return notElem( e, Equal.equals );
	}

	public ListIndex nthElem( int n, Object e ) {
		return nthElem( n, e, Equal.equals );
	}

	public ListIndex nthElem( int n, Object e, Eq<Object> eq ) {
		return n == 0
			? NONE
			: new NthEqualListIndex( n, Fulfills.eqTo( eq, e ), 1 );
	}

	public ListIndex duplicate() {
		return duplicateBy( Equal.equals );
	}

	public ListIndex duplicateBy( Eq<Object> eq ) {
		return duplicateFromBy( 0, eq );
	}

	public ListIndex duplicateFromBy( int start, Eq<Object> eq ) {
		return new DuplicateListIndex( start, eq );
	}

	public ListIndex duplicateFrom( int start ) {
		return duplicateFromBy( start, Equal.equals );
	}

	public ListIndex duplicateOf( int index ) {
		return duplicateOfBy( index, Equal.equals );
	}

	public ListIndex duplicateOfBy( int index, Eq<Object> eq ) {
		return new DuplicateForIndexListIndex( index, eq );
	}

	public ListIndex insert( Object e ) {
		return new InsertListIndex( e, Order.inherent );
	}

	public ListIndex insertBy( Object e, Ord<Object> ord ) {
		return new InsertListIndex( e, ord );
	}

	public ListIndex minimum() {
		return minimumBy( Order.inherent );
	}

	public ListIndex minimumBy( Ord<Object> ord ) {
		return new MinimumListIndex( ord );
	}

	public ListIndex maximum() {
		return maximumBy( Order.inherent );
	}

	public ListIndex maximumBy( Ord<Object> ord ) {
		return new MaximimListIndex( ord );
	}

	static final class RelativListIndex
			implements ListIndex {

		private final int offset;
		private final ListIndex index;

		RelativListIndex( ListIndex index, int offset ) {
			super();
			this.offset = offset;
			this.index = index;
		}

		@Override
		public <E> int in( Sequence<E> list ) {
			return List.indexFor.elemAt( index.in( list ) + offset ).in( list );
		}
	}

	static final class InsertListIndex
			implements ListIndex {

		private final Object key;
		private final Ord<Object> ord;

		InsertListIndex( Object key, Ord<Object> ord ) {
			super();
			this.key = key;
			this.ord = ord;
		}

		@Override
		public <E> int in( Sequence<E> list ) {
			if ( list.isEmpty() ) {
				return 0;
			}
			int pos = binarySearch( list, 0, list.length(), key, ord );
			return pos < 0
				? -pos - 1
				: pos;
		}

		//TODO find a place for such utils like binarySearch and sort (which is in Order now)
		static <E> int binarySearch( Sequence<E> list, int startInclusive, int endExcluisve,
				Object key, Ord<Object> ord ) {
			int low = startInclusive;
			int high = endExcluisve - 1;
			while ( low <= high ) {
				int mid = ( low + high ) >>> 1;
				E midVal = list.at( mid );
				Ordering cmp = ord.ord( midVal, key );
				if ( cmp.isLt() ) {
					low = mid + 1;
				} else if ( cmp.isGt() ) {
					high = mid - 1;
				} else {
					return mid; // key found
				}
			}
			return - ( low + 1 ); // key not found.
		}
	}

	static abstract class OrderingListIndex
			implements ListIndex {

		private final Ord<Object> ord;

		OrderingListIndex( Ord<Object> ord ) {
			super();
			this.ord = ord;
		}

		@Override
		public <E> int in( Sequence<E> list ) {
			if ( list.isEmpty() ) {
				return NOT_CONTAINED;
			}
			int bestIndex = 0;
			E best = list.at( bestIndex );
			int size = list.length();
			for ( int i = 1; i < size; i++ ) {
				E e = list.at( i );
				if ( improving( ord.ord( best, e ) ) ) {
					best = e;
					bestIndex = i;
				}
			}
			if ( bestIndex == 0 ) { // does best just "win" because we started with it ?
				if ( Null.isSave( ord ) ) {
					if ( ord.ord( null, best ) == ord.ord( best, null ) ) {
						return NOT_CONTAINED;
					}
				} else if ( list.length() > 1 ) {
					E other = list.at( 1 );
					Ordering order = ord.ord( best, other );
					if ( !order.isEq() && order == ord.ord( other, best ) ) {
						return NOT_CONTAINED;
					}
				}
				// sadly than there is no way to tell if best is just the best because its the first
			}
			return bestIndex;
		}

		abstract <E> boolean improving( Ordering ordering );
	}

	static final class MinimumListIndex
			extends OrderingListIndex {

		MinimumListIndex( Ord<Object> ord ) {
			super( ord );
		}

		@Override
		<E> boolean improving( Ordering ordering ) {
			return ordering == Ordering.GT; // because the right (actual) arg is less the left is GT
		}

	}

	static final class MaximimListIndex
			extends OrderingListIndex {

		MaximimListIndex( Ord<Object> ord ) {
			super( ord );
		}

		@Override
		<E> boolean improving( Ordering ordering ) {
			return ordering == Ordering.LT;
		}

	}

	static final class NotContainedListIndex
			implements ListIndex {

		@Override
		public <E> int in( Sequence<E> list ) {
			return NOT_CONTAINED;
		}

	}

	static final class DuplicateForIndexListIndex
			implements ListIndex {

		private final int index;
		private final Eq<Object> eq;

		DuplicateForIndexListIndex( int index, Eq<Object> eq ) {
			super();
			this.index = index;
			this.eq = eq;
		}

		@Override
		public <E> int in( Sequence<E> list ) {
			if ( index >= list.length() ) {
				return NOT_CONTAINED;
			}
			E e = list.at( index );
			for ( int j = index + 1; j < list.length(); j++ ) {
				if ( eq.holds( e, list.at( j ) ) ) {
					return j;
				}
			}
			return NOT_CONTAINED;
		}

	}

	static final class DuplicateListIndex
			implements ListIndex {

		private final int start;
		private final Eq<Object> eq;

		DuplicateListIndex( int start, Eq<Object> eq ) {
			super();
			this.start = start;
			this.eq = eq;
		}

		@Override
		public <E> int in( Sequence<E> list ) {
			int size = list.length();
			for ( int i = start; i < size - 1; i++ ) {
				int index = List.indexFor.duplicateOfBy( i, eq ).in( list );
				if ( index != NOT_CONTAINED ) {
					return index;
				}
			}
			return NOT_CONTAINED;
		}
	}

	static final class OnPositionListIndex
			implements ListIndex {

		private final int pos;

		OnPositionListIndex( int pos ) {
			super();
			this.pos = pos;
		}

		@Override
		public <E> int in( Sequence<E> list ) {
			final int size = list.length();
			return Math.abs( pos ) < size
				? pos < 0
					? size + pos
					: pos
				: NOT_CONTAINED;
		}
	}

	static final class NthEqualListIndex
			implements ListIndex {

		private final int n;
		private final int step;
		private final Predicate<Object> predicate;

		NthEqualListIndex( int n, Predicate<Object> predicate, int step ) {
			super();
			this.n = n;
			this.predicate = predicate;
			this.step = step;
			if ( step == 0 ) {
				throw new IllegalArgumentException( "increment must be less or greater than 0" );
			}
		}

		@Override
		public <E> int in( Sequence<E> list ) {
			if ( list.isEmpty() ) {
				return NOT_CONTAINED;
			}
			int size = list.length();
			int equal = 0;
			int idx = step > 0
				? 0
				: size - 1;
			while ( idx >= 0 && idx < size ) {
				final E e = list.at( idx );
				if ( predicate.fulfilledBy( e ) ) {
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
