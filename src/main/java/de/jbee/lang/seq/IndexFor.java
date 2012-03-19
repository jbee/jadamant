package de.jbee.lang.seq;

import static de.jbee.lang.Order.effective;
import de.jbee.lang.Eq;
import de.jbee.lang.Equal;
import de.jbee.lang.Is;
import de.jbee.lang.List;
import de.jbee.lang.ListIndex;
import de.jbee.lang.Operator;
import de.jbee.lang.Ord;
import de.jbee.lang.Order;
import de.jbee.lang.Ordering;
import de.jbee.lang.Predicate;
import de.jbee.lang.Sequence;
import de.jbee.lang.dev.Null;

public class IndexFor {

	public static boolean exists( int index ) {
		return index >= 0;
	}

	public static int insertionIndex( int index ) {
		return exists( index )
			? index
			: -index - 1;
	}

	public final ListIndex nothing = new NotContainedListIndex();
	public final ListIndex first = new OnPositionListIndex( 0 );
	public final ListIndex last = new OnPositionListIndex( -1 );
	public final ListIndex head = first;

	public static final IndexFor indexFor = new IndexFor();

	private IndexFor() {
		// hide
	}

	public ListIndex highestLimitedTo( int count ) {
		return count <= 0
			? nothing
			: count == 1
				? first
				: new LimitedHighestListIndex( last, count - 1 );
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
			? nothing
			: index == 0
				? head
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
			? last
			: pos == 0
				? head
				: new OnPositionListIndex( pos );
	}

	public ListIndex isNot( Predicate<Object> predicate ) {
		return nthIs( 1, Is.not( predicate ) );
	}

	public ListIndex is( Predicate<Object> predicate ) {
		return nthIs( 1, predicate );
	}

	public ListIndex nthIs( int n, Predicate<Object> predicate ) {
		return new NthFulfilledListIndex( n, predicate, 1 );
	}

	public ListIndex elem( Object e ) {
		return elemBy( e, Equal.equals );
	}

	public ListIndex elemBy( Object e, Eq<Object> eq ) {
		return nthElemBy( 1, e, eq );
	}

	public ListIndex notElemBy( Object e, Eq<Object> eq ) {
		return elemBy( e, Equal.not( eq ) );
	}

	public ListIndex notElem( Object e ) {
		return notElemBy( e, Equal.equals );
	}

	public ListIndex nthElem( int n, Object e ) {
		return nthElemBy( n, e, Equal.equals );
	}

	public ListIndex nthElemBy( int n, Object e, Eq<Object> eq ) {
		return nthElemBy( n, Is.eqBy( e, eq ) );
	}

	public ListIndex nthElemBy( int n, Predicate<Object> predicate ) {
		return n == 0
			? nothing
			: new NthFulfilledListIndex( n, predicate, 1 );
	}

	public ListIndex nthElemBy( int n, Object e, Ord<Object> order ) {
		return nthElemBy( n, Is.fulfilledBy( Operator.eqBy( order ), e ) );
	}

	//TODO test this method
	public ListIndex nthSubsequence( int n, Eq<Object> equality, Sequence<?> subsequence ) {
		return new NthSubSequenceListIndex( n, equality, subsequence );
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
		return insertBy( e, Order.inherent );
	}

	public ListIndex insertBy( Object e, Ord<Object> order ) {
		return new InsertionListIndex( e, order );
	}

	public ListIndex minimum() {
		return minimumBy( Order.inherent );
	}

	public ListIndex minimumBy( Ord<Object> order ) {
		return new MinimumElementListIndex( order );
	}

	public ListIndex maximum() {
		return maximumBy( Order.inherent );
	}

	public ListIndex maximumBy( Ord<Object> order ) {
		return new MaximimElementListIndex( order );
	}

	private static final class RelativListIndex
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

	private static final class InsertionListIndex
			implements ListIndex {

		private final Object key;
		private final Ord<Object> order;

		InsertionListIndex( Object key, Ord<Object> order ) {
			super();
			this.key = key;
			this.order = order;
		}

		@Override
		public <E> int in( Sequence<E> list ) {
			return list.isEmpty()
				? 0
				: insertionIndex( Order.binarySearch( list, 0, list.length(), key, effective(
						order, list ) ) );
		}
	}

	static abstract class OrderingListIndex
			implements ListIndex {

		private final Ord<Object> order;

		OrderingListIndex( Ord<Object> order ) {
			super();
			this.order = order;
		}

		@Override
		public <E> int in( Sequence<E> list ) {
			if ( list.isEmpty() ) {
				return NOT_CONTAINED;
			}
			int bestIndex = 0;
			E best = list.at( bestIndex );
			int length = list.length();
			for ( int i = 1; i < length; i++ ) {
				E e = list.at( i );
				if ( improving( order.ord( best, e ) ) ) {
					best = e;
					bestIndex = i;
				}
			}
			if ( bestIndex == 0 ) { // does best just "win" because we started with it ?
				if ( Null.isSave( order ) ) {
					if ( order.ord( null, best ) == order.ord( best, null ) ) {
						return NOT_CONTAINED;
					}
				} else if ( list.length() > 1 ) {
					E other = list.at( 1 );
					Ordering ordering = order.ord( best, other );
					if ( !ordering.isEq() && ordering == order.ord( other, best ) ) {
						return NOT_CONTAINED;
					}
				}
				// sadly than there is no way to tell if best is just the best because its the first
			}
			return bestIndex;
		}

		abstract <E> boolean improving( Ordering ordering );
	}

	private static final class LimitedHighestListIndex
			implements ListIndex {

		private final ListIndex index;
		private final int maxIndex;

		LimitedHighestListIndex( ListIndex index, int maxIndex ) {
			super();
			this.index = index;
			this.maxIndex = maxIndex;
		}

		@Override
		public <E> int in( Sequence<E> list ) {
			return Math.min( maxIndex, index.in( list ) );
		}

	}

	private static final class MinimumElementListIndex
			extends OrderingListIndex {

		MinimumElementListIndex( Ord<Object> order ) {
			super( order );
		}

		@Override
		<E> boolean improving( Ordering ordering ) {
			return ordering == Ordering.GT; // because the right (actual) arg is less the left is GT
		}

	}

	private static final class MaximimElementListIndex
			extends OrderingListIndex {

		MaximimElementListIndex( Ord<Object> ord ) {
			super( ord );
		}

		@Override
		<E> boolean improving( Ordering ordering ) {
			return ordering == Ordering.LT;
		}

	}

	private static final class NotContainedListIndex
			implements ListIndex {

		NotContainedListIndex() {
			// make visible locally
		}

		@Override
		public <E> int in( Sequence<E> list ) {
			return NOT_CONTAINED;
		}

	}

	private static final class DuplicateForIndexListIndex
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

	private static final class DuplicateListIndex
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
			int length = list.length();
			for ( int i = start; i < length - 1; i++ ) {
				int index = List.indexFor.duplicateOfBy( i, eq ).in( list );
				if ( exists( index ) ) {
					return index;
				}
			}
			return NOT_CONTAINED;
		}
	}

	private static final class OnPositionListIndex
			implements ListIndex {

		private final int pos;

		OnPositionListIndex( int pos ) {
			super();
			this.pos = pos;
		}

		@Override
		public <E> int in( Sequence<E> list ) {
			final int length = list.length();
			return Math.abs( pos ) < length
				? pos < 0
					? length + pos
					: pos
				: NOT_CONTAINED;
		}
	}

	private static final class NthFulfilledListIndex
			implements ListIndex {

		private final int n;
		private final int step;
		private final Predicate<Object> predicate;

		NthFulfilledListIndex( int n, Predicate<Object> predicate, int step ) {
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
			int length = list.length();
			int fulfilled = 0;
			int idx = step > 0
				? 0
				: length - 1;
			while ( idx >= 0 && idx < length ) {
				final E e = list.at( idx );
				if ( predicate.is( e ) ) {
					fulfilled++;
					if ( fulfilled == n ) {
						return idx;
					}
				}
				idx += step;
			}
			return NOT_CONTAINED;
		}
	}

	// TODO nth can be separated from the other ListIndex classes 

	private static final class NthSubSequenceListIndex
			implements ListIndex {

		private final int n;
		private final Eq<Object> equality;
		private final Sequence<?> subsequence;

		NthSubSequenceListIndex( int n, Eq<Object> equality, Sequence<?> subsequence ) {
			super();
			this.n = n;
			this.equality = equality;
			this.subsequence = subsequence;
		}

		@Override
		public <E> int in( Sequence<E> list ) {
			int seqLen = subsequence.length();
			if ( list.isEmpty() || subsequence.isEmpty() || list.length() < seqLen ) {
				return NOT_CONTAINED;
			}
			int length = list.length();
			int idx = 0;
			int fulfilled = 0;
			int end = length - seqLen;
			while ( idx <= end ) {
				int elemIndex = 0;
				while ( elemIndex < seqLen
						&& equality.holds( list.at( idx + elemIndex ), subsequence.at( elemIndex ) ) ) {
					elemIndex++;
				}
				if ( elemIndex >= seqLen ) {
					fulfilled++;
					if ( fulfilled == n ) {
						return idx;
					}
					idx += seqLen;
				} else {
					idx++;
				}
			}
			return NOT_CONTAINED;
		}
	}
}
