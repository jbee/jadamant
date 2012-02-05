package de.jbee.lang.seq;

import static de.jbee.lang.ListIndex.NOT_CONTAINED;
import de.jbee.lang.Array;
import de.jbee.lang.Bag;
import de.jbee.lang.Eq;
import de.jbee.lang.Equal;
import de.jbee.lang.Lang;
import de.jbee.lang.List;
import de.jbee.lang.ListAlteration;
import de.jbee.lang.ListIndex;
import de.jbee.lang.Lister;
import de.jbee.lang.Ord;
import de.jbee.lang.Order;
import de.jbee.lang.Predicate;
import de.jbee.lang.Set;
import de.jbee.lang.Traversal;

public class AlterBy
		implements ListAlteration {

	public static final ListAlteration none = new NoAlteration();
	public static final ListAlteration empty = new EmptyingAlteration();
	public static final ListAlteration reverse = new ReversingAlteration();
	public static final ListAlteration shuffle = new ShuffleAlteration();
	public static final ListAlteration tidyUp = new TidyUpAlteration();
	public static final ListAlteration init = new TakeTillIndexAlteration( List.indexFor.last(), 0 );
	public static final ListAlteration tail = new DropTillIndexAlteration( List.indexFor.head(), 1 );

	public static final AlterBy instance = new AlterBy( none );

	//TODO find better naming - this is not clear enough
	private static ListIndex lastUpTo( int count ) {
		return List.indexFor.lastUpTo( count );
	}

	private static ListAlteration pure( ListAlteration t ) {
		return t instanceof AlterBy
			? ( (AlterBy) t ).alteration
			: t;
	}

	private final ListAlteration alteration;

	private AlterBy( ListAlteration utilised ) {
		this.alteration = utilised;
	}

	public AlterBy chop( int start, int end ) {
		return cutOut( start, end );
	}

	public AlterBy concat( ListAlteration head, ListAlteration tail ) {
		return append( new ConcatAlteration( head, tail ) );
	}

	public BagAlteration chain( ListAlteration fst, BagAlteration snd ) {
		fst = pure( fst );
		if ( fst == none ) {
			return snd;
		}
		return new ConsecutiveBagAlteration( fst, snd );
	}

	public ListAlteration chain( ListAlteration fst, ListAlteration snd ) {
		fst = pure( fst );
		snd = pure( snd );
		if ( fst == none ) {
			return snd;
		}
		if ( snd == none ) {
			return fst;
		}
		return new ConsecutiveAlteration( fst, snd );
	}

	public SetAlteration chain( ListAlteration fst, SetAlteration snd ) {
		fst = pure( fst );
		if ( fst == none ) {
			return snd;
		}
		return new ConsecutiveSetAlteration( fst, snd );
	}

	public AlterBy cutOut( int start, int end ) {
		return start == end
			? delete( start )
			: concat( takeUpTo( start - 1 ), takeFrom( end + 1 ) );
	}

	public AlterBy delete( int index ) {
		return index < 0
			? this
			: delete( List.indexFor.elemAt( index ) );
	}

	public AlterBy delete( ListIndex index ) {
		return append( new DeleteIndexAlteration( index ) );
	}

	public AlterBy delete( Object elem ) {
		return delete( elem, Equal.equals );
	}

	public AlterBy delete( Object elem, Eq<Object> eq ) {
		return delete( List.indexFor.elemBy( elem, eq ) );
	}

	public AlterBy drop( int count ) {
		return count < 0
			? this
			: count == 1
				? append( tail )
				: dropTill( lastUpTo( count ), 1 );
	}

	public AlterBy dropFrom( int index ) {
		return take( index );
	}

	public AlterBy dropFromTo( int start, int end ) {
		return cutOut( start, end );
	}

	public AlterBy dropRight( int count ) {
		return count <= 0
			? this
			: takeTill( List.indexFor.elemOn( -count ) );
	}

	public AlterBy dropTill( ListIndex index ) {
		return dropTill( index, 0 );
	}

	public AlterBy dropTill( ListIndex index, int offset ) {
		return append( new DropTillIndexAlteration( index, offset ) );
	}

	public AlterBy dropUpTo( int index ) {
		return drop( index );
	}

	//TODO some kind of dropWhile working with a something like Predicate<List<E>> -> other interface
	public AlterBy dropWhile( Predicate<Object> condition ) {
		return append( new DropWhileAlteration( condition ) );
	}

	@Override
	public <E> List<E> in( List<E> list ) {
		return alteration.in( list );
	}

	public AlterBy nub() {
		return nubBy( Equal.equals );
	}

	public AlterBy nubBy( Ord<Object> order ) {
		return append( new NubAlteration( order ) );
	}

	public AlterBy nubBy( Eq<Object> equality ) {
		return nubBy( Order.by( equality ) );
	}

	public ListAlteration pure() {
		return alteration;
	}

	public BagAlteration refineToBag() {
		return refineToBagBy( Order.inherent );
	}

	public BagAlteration refineToBagBy( Ord<Object> order ) {
		return append( new ToBagAlteration( order ) );
	}

	public SetAlteration refineToSet() {
		return refineToSetBy( Order.inherent );
	}

	public SetAlteration refineToSetBy( Ord<Object> order ) {
		return append( new ToSetAlteration( order ) );
	}

	public AlterBy slice( int startInclusive, int endExclusive ) {
		return takeFromTo( startInclusive, endExclusive - 1 );
	}

	public BagAlteration sort() {
		return sortBy( Order.inherent );
	}

	public BagAlteration sortBy( Ord<Object> order ) {
		return append( new SortingAlteration( order ) );
	}

	public AlterBy sublist( int start, int length ) {
		return append( new SublistAlteration( start, length ) );
	}

	public AlterBy swap( int idx1, int idx2 ) {
		return idx1 == idx2
			? this
			: append( new SwapAlteration( List.indexFor.elemAt( idx1 ),
					List.indexFor.elemAt( idx2 ) ) );
	}

	public AlterBy take( int count ) {
		return count <= 0
			? append( empty )
			: takeTill( lastUpTo( count ), 1 );
	}

	public AlterBy takeFrom( int index ) {
		return drop( index );
	}

	public AlterBy takeFromTo( int start, int end ) {
		return sublist( start, end - start + 1 );
	}

	public AlterBy takeRight( int count ) {
		return count <= 0
			? append( empty )
			: dropTill( List.indexFor.elemOn( -count ) );
	}

	public AlterBy takeTill( ListIndex index ) {
		return takeTill( index, 0 );
	}

	public AlterBy takeTill( ListIndex index, int offset ) {
		return append( new TakeTillIndexAlteration( index, offset ) );
	}

	public AlterBy takeUpTo( int index ) {
		return take( index );
	}

	//TODO some kind of takeWhile working with a condition types with the list's type -> other interface
	public AlterBy takeWhile( Predicate<Object> condition ) {
		return append( new TakeWhileAlteration( condition ) );
	}

	public ListAlteration tidyUp() {
		return append( tidyUp ).pure();
	}

	public AlterBy trim( int count ) {
		return append( chain( drop( count ), dropRight( count ) ) );
	}

	private BagAlteration append( BagAlteration snd ) {
		return chain( alteration, snd );
	}

	private AlterBy append( ListAlteration next ) {
		if ( next == none ) {
			return this;
		}
		if ( next == empty ) {
			return new AlterBy( next );
		}
		return new AlterBy( chain( alteration, next ) );
	}

	private SetAlteration append( SetAlteration snd ) {
		return chain( alteration, snd );
	}

	//TODO filter(Predicate)

	private static final class EmptyingAlteration
			implements ListAlteration {

		EmptyingAlteration() {
			// make visible
		}

		@Override
		public <E> List<E> in( List<E> list ) {
			return list.take( 0 ); // get a empty one
		}
	}

	static abstract class FillAndArrangeAlteration
			implements ListAlteration {

		protected final <E> List<E> arrangedStackList( List<E> list ) {
			int size = list.length();
			Object[] elems = new Object[Lang.nextHighestPowerOf2( size )];
			list.fill( elems.length - size, elems, 0, size );
			rearrange( elems, elems.length - size );
			return EvolutionList.dominant( list.length(), elems, list.take( 0 ) );
		}

		protected abstract <E> void rearrange( Object[] list, int start );
	}

	private static final class NoAlteration
			implements ListAlteration {

		NoAlteration() {
			// make visible
		}

		@Override
		public <E> List<E> in( List<E> list ) {
			return list;
		}
	}

	private static final class ShuffleAlteration
			extends FillAndArrangeAlteration {

		ShuffleAlteration() {
			// make visible
		}

		@Override
		public <E> List<E> in( List<E> list ) {
			int size = list.length();
			if ( size < 2 ) {
				return list;
			}
			if ( size == 2 ) {
				return List.alterBy.swap( 0, 1 ).in( list );
			}
			return arrangedStackList( list );
		}

		@Override
		protected <E> void rearrange( Object[] list, int start ) {
			Array.shuffle( list ); //FIXME consider start / end
		}

	}

	/**
	 * We cannot just create a {@link Bag} using the {@link Lister.BagLister} since the
	 * implementation might use this alteration to ensure bag ordering constraint.
	 */
	private static final class SortingAlteration
			extends FillAndArrangeAlteration
			implements BagAlteration {

		private final Ord<Object> order;

		SortingAlteration( Ord<Object> order ) {
			super();
			this.order = order;
		}

		@Override
		public <E> Bag<E> in( List<E> list ) {
			int size = list.length();
			return size < 2
				? OrderedList.bagOf( list, order )
				: OrderedList.bagOf( arrangedStackList( list ), order );
		}

		@Override
		public <E> void rearrange( Object[] list, int start ) {
			Order.sort( list, order ); //FIXME consider start
		}
	}

	private static final class SwapAlteration
			implements ListAlteration {

		private final ListIndex index1;
		private final ListIndex index2;

		SwapAlteration( ListIndex idx1, ListIndex idx2 ) {
			super();
			this.index1 = idx1;
			this.index2 = idx2;
		}

		@Override
		public <E> List<E> in( List<E> list ) {
			final int idx1 = index1.in( list );
			final int idx2 = index2.in( list );
			if ( idx1 == idx2 ) {
				return list;
			}
			// swap indices so that idx1 is always smaller - that will change higher index first
			return idx1 > idx2
				? swap( idx2, idx1, list )
				: swap( idx1, idx2, list );
		}

		private <E> List<E> swap( int idx1, int idx2, List<E> list ) {
			final E e2 = list.at( idx2 );
			List<E> replaceAt = list.replaceAt( idx2, list.at( idx1 ) );
			return replaceAt.replaceAt( idx1, e2 );
		}
	}

	private static final class TidyUpAlteration
			implements ListAlteration {

		TidyUpAlteration() {
			// make visible
		}

		@Override
		public <E> List<E> in( List<E> list ) {
			return list.tidyUp();
		}

	}

	private static final class ConcatAlteration
			implements ListAlteration {

		private final ListAlteration one;
		private final ListAlteration other;

		ConcatAlteration( ListAlteration one, ListAlteration other ) {
			super();
			this.one = one;
			this.other = other;
		}

		@Override
		public <E> List<E> in( List<E> list ) {
			return one.in( list ).concat( other.in( list ) );
		}

	}

	private static final class ConsecutiveBagAlteration
			implements BagAlteration {

		final ListAlteration fst;
		final BagAlteration snd;

		ConsecutiveBagAlteration( ListAlteration fst, BagAlteration snd ) {
			super();
			this.fst = fst;
			this.snd = snd;
		}

		@Override
		public <E> Bag<E> in( List<E> list ) {
			return snd.in( fst.in( list ) );
		}
	}

	private static final class ConsecutiveSetAlteration
			implements SetAlteration {

		final ListAlteration fst;
		final SetAlteration snd;

		ConsecutiveSetAlteration( ListAlteration fst, SetAlteration snd ) {
			super();
			this.fst = fst;
			this.snd = snd;
		}

		@Override
		public <E> Set<E> in( List<E> list ) {
			return snd.in( fst.in( list ) );
		}
	}

	private static final class ConsecutiveAlteration
			implements ListAlteration {

		final ListAlteration fst;
		final ListAlteration snd;

		ConsecutiveAlteration( ListAlteration fst, ListAlteration snd ) {
			super();
			this.fst = fst;
			this.snd = snd;
		}

		@Override
		public <E> List<E> in( List<E> list ) {
			return snd.in( fst.in( list ) );
		}
	}

	private static final class DeleteIndexAlteration
			implements ListAlteration {

		private final ListIndex index;

		DeleteIndexAlteration( ListIndex index ) {
			super();
			this.index = index;
		}

		@Override
		public <E> List<E> in( List<E> list ) {
			final int i = index.in( list );
			return i == ListIndex.NOT_CONTAINED
				? list
				: list.deleteAt( i );
		}
	}

	private static final class DropTillIndexAlteration
			implements ListAlteration {

		private final ListIndex index;
		private final int offset;

		DropTillIndexAlteration( ListIndex index, int offset ) {
			super();
			this.index = index;
			this.offset = offset;
		}

		@Override
		public <E> List<E> in( List<E> list ) {
			return list.drop( index.in( list ) + offset );
		}

	}

	private static final class DropWhileAlteration
			implements ListAlteration {

		final Predicate<Object> predicate;

		DropWhileAlteration( Predicate<Object> condition ) {
			super();
			this.predicate = condition;
		}

		@Override
		public <E> List<E> in( List<E> list ) {
			final int index = List.indexFor.firstFalse( predicate ).in( list );
			return index == NOT_CONTAINED
				? empty.in( list )
				: list.drop( index );
		}
	}

	private static final class NubAlteration
			implements ListAlteration {

		private final Ord<Object> order;

		NubAlteration( Ord<Object> order ) {
			super();
			this.order = order;
		}

		@Override
		public <E> List<E> in( List<E> list ) {
			if ( list.isEmpty() ) {
				return list;
			}
			//OPEN use FillAndArrangeAlteration
			if ( Order.used( order, list ) ) {
				return preOrderedNub( list );
			}
			return unorderedNub( list );
		}

		private <E> List<E> unorderedNub( List<E> list ) {
			final int l = list.length();
			Object[] elements = new Object[Lang.nextHighestPowerOf2( l )];
			int j = 0;
			for ( int i = 0; i < l; i++ ) {
				E e = list.at( i );
				if ( List.indexFor.nthElemBy( 1, e, order ).in( Array.sequence( elements ) ) == ListIndex.NOT_CONTAINED ) {
					elements[j++] = e;
				}
			}
			return evoList( j, elements );
		}

		private <E> List<E> evoList( int occupied, Object[] elems ) {
			Array.push( elems.length - occupied, elems );
			return EvolutionList.dominant( occupied, elems );
		}

		private <E> List<E> preOrderedNub( List<E> list ) {
			final int l = list.length();
			Object[] elements = new Object[Lang.nextHighestPowerOf2( l )];
			int j = 0;
			E previous = list.at( 0 );
			elements[j++] = previous;
			for ( int i = 1; i < l; i++ ) {
				E e = list.at( i );
				if ( order.ord( previous, e ).isLt() ) {
					previous = e;
					elements[j++] = e;
				}
			}
			return evoList( j, elements );
		}
	}

	private final static class ReversingList<E>
			implements List<E> {

		final List<E> list;

		ReversingList( List<E> beingReversed ) {
			super();
			this.list = beingReversed;
		}

		@Override
		public List<E> append( E e ) {
			return reverseViewOf( list.prepand( e ) );
		}

		@Override
		public List<E> subsequent() {
			return reverseViewOf( list.subsequent() );
		}

		@Override
		public E at( int index ) {
			return list.at( reverseIndexOf( index ) );
		}

		@Override
		public List<E> concat( List<E> other ) {
			return reverseViewOf( other.concat( list ) );
		}

		@Override
		public List<E> deleteAt( int index ) {
			return reverseViewOf( list.deleteAt( reverseIndexOf( index ) ) );
		}

		@Override
		public List<E> drop( int count ) {
			return reverseViewOf( list.take( list.length() - count ) );
		}

		@Override
		public void fill( int offset, Object[] array, int start, int length ) {
			int size = list.length();
			if ( start < size ) {
				for ( int i = start; i < start + length; i++ ) {
					array[offset++] = at( i );
				}
			}
		}

		@Override
		public List<E> insertAt( int index, E e ) {
			return reverseViewOf( list.insertAt( reverseIndexOf( index ), e ) );
		}

		@Override
		public boolean isEmpty() {
			return list.isEmpty();
		}

		@Override
		public int length() {
			return list.length();
		}

		@Override
		public List<E> prepand( E e ) {
			return reverseViewOf( list.append( e ) );
		}

		@Override
		public List<E> replaceAt( int index, E e ) {
			return reverseViewOf( list.replaceAt( reverseIndexOf( index ), e ) );
		}

		@Override
		public List<E> take( int count ) {
			return reverseViewOf( list.drop( list.length() - count ) );
		}

		@Override
		public List<E> tidyUp() {
			return reverseViewOf( list.tidyUp() );
		}

		@Override
		public String toString() {
			return list.toString();
		}

		@Override
		public void traverse( int start, Traversal<? super E> traversal ) {
			final int l = list.length();
			int i = start;
			int inc = 0;
			while ( inc >= 0 && i < l ) {
				inc = traversal.incrementOn( at( i ) );
				i += inc;
			}
		}

		private int reverseIndexOf( int index ) {
			return list.length() - 1 - index;
		}

		private ReversingList<E> reverseViewOf( List<E> l ) {
			return l == list
				? this
				: new ReversingList<E>( l );
		}
	}

	private static final class ReversingAlteration
			implements ListAlteration {

		ReversingAlteration() {
			// make visible
		}

		@Override
		public <E> List<E> in( List<E> list ) {
			if ( list instanceof ReversingList<?> ) {
				return ( (ReversingList<E>) list ).list;
			}
			return new ReversingList<E>( list );
		}

	}

	private static final class SublistAlteration
			implements ListAlteration {

		private final int start;
		private final int length;

		SublistAlteration( int start, int length ) {
			super();
			this.start = start;
			this.length = length;
		}

		@Override
		public <E> List<E> in( List<E> list ) {
			if ( start == 0 ) {
				return list.take( length );
			}
			int size = list.length();
			if ( start >= size ) {
				return empty.in( list );
			}
			if ( start + length > size ) {
				return list.drop( start );
			}
			if ( length == 1 ) {
				return list.take( 1 ).replaceAt( 0, list.at( start ) );
			}
			return list.drop( start ).take( length );
		}

	}

	private static final class TakeTillIndexAlteration
			implements ListAlteration {

		private final ListIndex index;
		private final int offset;

		TakeTillIndexAlteration( ListIndex index, int offset ) {
			super();
			this.index = index;
			this.offset = offset;
		}

		@Override
		public <E> List<E> in( List<E> list ) {
			return list.take( index.in( list ) + offset );
		}

	}

	private static final class TakeWhileAlteration
			implements ListAlteration {

		final Predicate<Object> predicate;

		TakeWhileAlteration( Predicate<Object> predicate ) {
			super();
			this.predicate = predicate;
		}

		@Override
		public <E> List<E> in( List<E> list ) {
			final int index = List.indexFor.firstFalse( predicate ).in( list );
			return index == NOT_CONTAINED
				? list
				: list.take( index );
		}

	}

	private static final class ToBagAlteration
			implements BagAlteration {

		private final Ord<Object> order;

		ToBagAlteration( Ord<Object> order ) {
			super();
			this.order = order;
		}

		@Override
		public <E> Bag<E> in( List<E> list ) {
			return Bag.with.elements( order, list );
		}

	}

	private static final class ToSetAlteration
			implements SetAlteration {

		private final Ord<Object> order;

		ToSetAlteration( Ord<Object> ord ) {
			super();
			this.order = ord;
		}

		@Override
		public <E> Set<E> in( List<E> list ) {
			return Set.with.elements( order, list );
		}

	}

}
