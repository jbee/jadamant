package de.jbee.lang.seq;

import static de.jbee.lang.ListIndex.NOT_CONTAINED;
import de.jbee.lang.Array;
import de.jbee.lang.Bag;
import de.jbee.lang.Eq;
import de.jbee.lang.Equal;
import de.jbee.lang.Lang;
import de.jbee.lang.List;
import de.jbee.lang.ListIndex;
import de.jbee.lang.ListTransition;
import de.jbee.lang.Ord;
import de.jbee.lang.Order;
import de.jbee.lang.Predicate;
import de.jbee.lang.Set;
import de.jbee.lang.Sorted;
import de.jbee.lang.Traversal;

public class UtileListTransition
		implements ListTransition {

	public static final ListTransition none = new NoneTranstion();
	public static final ListTransition empty = new EmptyTransition();
	public static final ListTransition reverse = new ReversingTransition();
	public static final ListTransition shuffle = new ShuffleTransition();
	public static final ListTransition tidyUp = new TidyUpTransition();
	public static final ListTransition init = new TakeTillIndexTransition( List.indexFor.LAST, 1 );
	public static final ListTransition tail = new DropTillIndexTransition( List.indexFor.HEAD, 1 );

	public static final UtileListTransition instance = new UtileListTransition( none );

	private final ListTransition utilised;

	private UtileListTransition( ListTransition utilised ) {
		this.utilised = utilised;
	}

	@Override
	public <E> List<E> from( List<E> list ) {
		return utilised.from( list );
	}

	private UtileListTransition followedBy( ListTransition next ) {
		if ( next == none ) {
			return this;
		}
		if ( next == empty ) {
			return new UtileListTransition( next );
		}
		return new UtileListTransition( consec( utilised, next ) );
	}

	//TODO find better naming - this is not clear enough
	private static ListIndex lastUpTo( int count ) {
		return List.indexFor.lastUpTo( count );
	}

	private static ListTransition pure( ListTransition t ) {
		return t instanceof UtileListTransition
			? ( (UtileListTransition) t ).utilised
			: t;
	}

	public ListTransition pure() {
		return utilised;
	}

	public ListTransition tidiesUp() {
		return followedBy( tidyUp ).pure();
	}

	//TODO some kind of dropWhile working with a condition types with the list's type -> other interface
	public UtileListTransition dropsWhile( Predicate<Object> condition ) {
		return followedBy( new DropWhileTransition( condition ) );
	}

	//TODO some kind of takeWhile working with a condition types with the list's type -> other interface
	public UtileListTransition takesWhile( Predicate<Object> condition ) {
		return followedBy( new TakeWhileTransition( condition ) );
	}

	public UtileListTransition dropsFirst( int count ) {
		return count < 0
			? this
			: count == 1
				? followedBy( tail )
				: dropsTill( lastUpTo( count ), 1 );
	}

	public UtileListTransition dropsLast( int count ) {
		return count <= 0
			? this
			: takesTill( List.indexFor.elemOn( -count ) );
	}

	public UtileListTransition takesFirst( int count ) {
		return count <= 0
			? followedBy( empty )
			: takesTill( lastUpTo( count ), 1 );
	}

	public UtileListTransition takesLast( int count ) {
		return count <= 0
			? followedBy( empty )
			: dropsTill( List.indexFor.elemOn( -count ) );
	}

	public UtileListTransition takesTill( ListIndex index ) {
		return takesTill( index, 0 );
	}

	public UtileListTransition takesTill( ListIndex index, int offset ) {
		return followedBy( new TakeTillIndexTransition( index, offset ) );
	}

	public UtileListTransition dropsTill( ListIndex index ) {
		return dropsTill( index, 0 );
	}

	public UtileListTransition dropsTill( ListIndex index, int offset ) {
		return followedBy( new DropTillIndexTransition( index, offset ) );
	}

	public UtileListTransition takesFrom( int index ) {
		return dropsFirst( index );
	}

	public UtileListTransition takesUpTo( int index ) {
		return takesFirst( index );
	}

	public UtileListTransition dropsFrom( int index ) {
		return takesFirst( index );
	}

	public UtileListTransition dropsUpTo( int index ) {
		return dropsFirst( index );
	}

	public UtileListTransition slice( int startInclusive, int endExclusive ) {
		return takesFromTo( startInclusive, endExclusive - 1 );
	}

	public UtileListTransition takesFromTo( int start, int end ) {
		return sublists( start, end - start + 1 );
	}

	public UtileListTransition dropsFromTo( int start, int end ) {
		return cutsOut( start, end );
	}

	public UtileListTransition trims( int count ) {
		return followedBy( consec( dropsFirst( count ), dropsLast( count ) ) );
	}

	public UtileListTransition chops( int start, int end ) {
		return cutsOut( start, end );
	}

	public UtileListTransition cutsOut( int start, int end ) {
		return start == end
			? deletes( start )
			: concats( takesUpTo( start - 1 ), takesFrom( end + 1 ) );
	}

	public UtileListTransition sublists( int start, int length ) {
		return followedBy( new SublistTransition( start, length ) );
	}

	public UtileListTransition swaps( int idx1, int idx2 ) {
		return idx1 == idx2
			? this
			: followedBy( new SwapTransition( List.indexFor.elemAt( idx1 ),
					List.indexFor.elemAt( idx2 ) ) );
	}

	public UtileListTransition sorts() {
		return sortsBy( Order.inherent );
	}

	public UtileListTransition sortsBy( Ord<Object> ord ) {
		return followedBy( new SortingTransition( ord ) );
	}

	public UtileListTransition nubs() {
		return nubsBy( Equal.equals );
	}

	public UtileListTransition nubsBy( Eq<Object> eq ) {
		return followedBy( new NubTransition( eq ) );
	}

	public UtileListTransition deletes( int index ) {
		return index < 0
			? this
			: deletes( List.indexFor.elemAt( index ) );
	}

	public UtileListTransition deletes( Object elem ) {
		return deletes( elem, Equal.equals );
	}

	public UtileListTransition deletes( Object elem, Eq<Object> eq ) {
		return deletes( List.indexFor.elemBy( elem, eq ) );
	}

	public UtileListTransition deletes( ListIndex index ) {
		return followedBy( new DeleteIndexTransition( index ) );
	}

	public UtileListTransition concats( ListTransition head, ListTransition tail ) {
		return followedBy( new ConcatTransition( head, tail ) );
	}

	public SetTrasition disambiguates() {
		return disambiguatesBy( Order.inherent );
	}

	public SetTrasition disambiguatesBy( Ord<Object> ord ) {
		return consec( utilised, new ToSetTranstion( ord ) );
	}

	public ListTransition consec( ListTransition fst, ListTransition snd ) {
		fst = pure( fst );
		snd = pure( snd );
		if ( fst == none ) {
			return snd;
		}
		if ( snd == none ) {
			return fst;
		}
		return new ConsecutivelyTransition( fst, snd );
	}

	public SetTrasition consec( ListTransition fst, SetTrasition snd ) {
		fst = pure( fst );
		if ( fst == none ) {
			return snd;
		}
		return new ConsecutivelySetTransition( fst, snd );
	}

	//TODO filter(Predicate)

	static final class NoneTranstion
			implements ListTransition {

		@Override
		public <E> List<E> from( List<E> list ) {
			return list;
		}
	}

	static final class EmptyTransition
			implements ListTransition {

		@Override
		public <E> List<E> from( List<E> list ) {
			return list.take( 0 ); // get a empty one
		}
	}

	static final class TimesTranstion
			implements ListTransition {

		@Override
		public <E> List<E> from( List<E> list ) {
			//TODO
			return null;
		}

	}

	static final class SwapTransition
			implements ListTransition {

		private final ListIndex index1;
		private final ListIndex index2;

		SwapTransition( ListIndex idx1, ListIndex idx2 ) {
			super();
			this.index1 = idx1;
			this.index2 = idx2;
		}

		@Override
		public <E> List<E> from( List<E> list ) {
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
			final E e1 = list.at( idx2 );
			return list.replaceAt( idx2, list.at( idx1 ) ).replaceAt( idx1, e1 );
		}
	}

	static final class ShuffleTransition
			extends FillAndArrangeTranstion {

		@Override
		public <E> List<E> from( List<E> list ) {
			int size = list.length();
			if ( size < 2 ) {
				return list;
			}
			if ( size == 2 ) {
				return List.which.swaps( 0, 1 ).from( list );
			}
			return arrangedStackList( list );
		}

		@Override
		protected <E> void rearrange( Object[] list, int start ) {
			Array.shuffle( list ); //FIXME consider start / end
		}

	}

	static final class TidyUpTransition
			implements ListTransition {

		@Override
		public <E> List<E> from( List<E> list ) {
			return list.tidyUp();
		}

	}

	private static final class ToSetTranstion
			implements SetTrasition {

		private final Ord<Object> order;

		ToSetTranstion( Ord<Object> ord ) {
			super();
			this.order = ord;
		}

		@Override
		public <E> Set<E> from( List<E> list ) {
			return list instanceof Set<?>
				? (Set<E>) list
				: Set.with.elements( order,
						List.which.sortsBy( order ).nubsBy( Equal.by( order ) ).from( list ) );
		}

	}

	private static final class DisambiguateTranstion
			implements SetTrasition {

		@Override
		public <E> Set<E> from( List<E> list ) {
			if ( list instanceof Set<?> ) {
				return (Set<E>) list;
			}
			if ( list.isEmpty() ) {
				return Set.with.noElements();
			}
			if ( ! ( list instanceof Sorted ) ) {
				list = List.which.sorts().from( list );
			}
			Ord<Object> order = ( (Sorted) list ).order();
			List<E> res = list;
			//FIXME remove duplicates
			return Set.with.elements( order, res );
		}

	}

	private static final class ToBagTransition
			implements BagTransition {

		private final Ord<Object> order;

		ToBagTransition( Ord<Object> order ) {
			super();
			this.order = order;
		}

		@Override
		public <E> Bag<E> from( List<E> list ) {
			return list instanceof Bag<?>
				? (Bag<E>) list
				: new SortedBag<E>( order, List.which.sortsBy( order ).from( list ) );
		}

	}

	private static final class NubTransition
			implements ListTransition {

		private final Eq<Object> eq;

		NubTransition( Eq<Object> eq ) {
			super();
			this.eq = eq;
		}

		@Override
		public <E> List<E> from( List<E> list ) {
			List<E> res = list;
			int i = 0;
			while ( i < res.length() ) {
				int index = List.indexFor.duplicateOfBy( i, eq ).in( res );
				if ( index == ListIndex.NOT_CONTAINED ) {
					i++;
				} else {
					res = res.deleteAt( index );
				}
			}
			return res;
		}

	}

	private static final class DeleteIndexTransition
			implements ListTransition {

		private final ListIndex index;

		DeleteIndexTransition( ListIndex index ) {
			super();
			this.index = index;
		}

		@Override
		public <E> List<E> from( List<E> list ) {
			final int i = index.in( list );
			return i == ListIndex.NOT_CONTAINED
				? list
				: list.deleteAt( i );
		}
	}

	private static final class ConcatTransition
			implements ListTransition {

		private final ListTransition head;
		private final ListTransition tail;

		ConcatTransition( ListTransition head, ListTransition tail ) {
			super();
			this.head = head;
			this.tail = tail;
		}

		@Override
		public <E> List<E> from( List<E> list ) {
			return head.from( list ).concat( tail.from( list ) );
		}

	}

	private static final class SublistTransition
			implements ListTransition {

		private final int start;
		private final int length;

		SublistTransition( int start, int length ) {
			super();
			this.start = start;
			this.length = length;
		}

		@Override
		public <E> List<E> from( List<E> list ) {
			if ( start == 0 ) {
				return list.take( length );
			}
			int size = list.length();
			if ( start >= size ) {
				return list.take( 0 ); // will lead to empty list but impl. depends on argument
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

	private static final class TakeTillIndexTransition
			implements ListTransition {

		private final ListIndex index;
		private final int offset;

		TakeTillIndexTransition( ListIndex index, int offset ) {
			super();
			this.index = index;
			this.offset = offset;
		}

		@Override
		public <E> List<E> from( List<E> list ) {
			return list.take( index.in( list ) + offset );
		}

	}

	private static final class DropTillIndexTransition
			implements ListTransition {

		private final ListIndex index;
		private final int offset;

		DropTillIndexTransition( ListIndex index, int offset ) {
			super();
			this.index = index;
			this.offset = offset;
		}

		@Override
		public <E> List<E> from( List<E> list ) {
			return list.drop( index.in( list ) + offset );
		}

	}

	private static final class ConsecutivelyTransition
			implements ListTransition {

		final ListTransition fst;
		final ListTransition snd;

		ConsecutivelyTransition( ListTransition fst, ListTransition snd ) {
			super();
			this.fst = fst;
			this.snd = snd;
		}

		@Override
		public <E> List<E> from( List<E> list ) {
			return snd.from( fst.from( list ) );
		}
	}

	private static final class ConsecutivelySetTransition
			implements SetTrasition {

		final ListTransition fst;
		final SetTrasition snd;

		ConsecutivelySetTransition( ListTransition fst, SetTrasition snd ) {
			super();
			this.fst = fst;
			this.snd = snd;
		}

		@Override
		public <E> Set<E> from( List<E> list ) {
			return snd.from( fst.from( list ) );
		}
	}

	private static final class TakeWhileTransition
			implements ListTransition {

		final Predicate<Object> predicate;

		TakeWhileTransition( Predicate<Object> predicate ) {
			super();
			this.predicate = predicate;
		}

		@Override
		public <E> List<E> from( List<E> list ) {
			final int index = List.indexFor.firstFalse( predicate ).in( list );
			return index == NOT_CONTAINED
				? list
				: list.take( index );
		}

	}

	private static final class DropWhileTransition
			implements ListTransition {

		final Predicate<Object> predicate;

		DropWhileTransition( Predicate<Object> condition ) {
			super();
			this.predicate = condition;
		}

		@Override
		public <E> List<E> from( List<E> list ) {
			final int index = List.indexFor.firstFalse( predicate ).in( list );
			return index == NOT_CONTAINED
				? list.take( 0 ) // drop all by take nothing -> empty list
				: list.drop( index );
		}
	}

	private static final class ReversingTransition
			implements ListTransition {

		@Override
		public <E> List<E> from( List<E> list ) {
			if ( list instanceof ReversingList<?> ) {
				return ( (ReversingList<E>) list ).list;
			}
			return new ReversingList<E>( list );
		}

	}

	static abstract class FillAndArrangeTranstion
			implements ListTransition {

		protected final <E> List<E> arrangedStackList( List<E> list ) {
			int size = list.length();
			Object[] elems = new Object[Lang.nextHighestPowerOf2( size )];
			list.fill( elems.length - size, elems, 0, size );
			rearrange( elems, elems.length - size );
			return StackList.tidy( list.length(), elems, list.take( 0 ) );
		}

		protected abstract <E> void rearrange( Object[] list, int start );
	}

	static final class SortingTransition
			extends FillAndArrangeTranstion {

		private final Ord<Object> ord;

		SortingTransition( Ord<Object> ord ) {
			super();
			this.ord = ord;
		}

		@Override
		public <E> List<E> from( List<E> list ) {
			int size = list.length();
			if ( size < 2 ) {
				return list;
			}
			return arrangedStackList( list );
		}

		@Override
		public <E> void rearrange( Object[] list, int start ) {
			Order.sort( list, ord ); //FIXME consider start
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
		public void fill( int offset, Object[] array, int start, int length ) {
			int size = list.length();
			if ( start < size ) {
				for ( int i = start; i < start + length; i++ ) {
					array[offset++] = at( i );
				}
			}
		}

		@Override
		public List<E> append( E e ) {
			return reverseViewOf( list.prepand( e ) );
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
		public List<E> insertAt( int index, E e ) {
			return reverseViewOf( list.insertAt( reverseIndexOf( index ), e ) );
		}

		@Override
		public boolean isEmpty() {
			return list.isEmpty();
		}

		@Override
		public List<E> prepand( E e ) {
			return reverseViewOf( list.append( e ) );
		}

		@Override
		public int length() {
			return list.length();
		}

		@Override
		public List<E> take( int count ) {
			return reverseViewOf( list.drop( list.length() - count ) );
		}

		@Override
		public List<E> tidyUp() {
			return reverseViewOf( list.tidyUp() );
		}

		private int reverseIndexOf( int index ) {
			return list.length() - 1 - index;
		}

		private ReversingList<E> reverseViewOf( List<E> l ) {
			return l == list
				? this
				: new ReversingList<E>( l );
		}

		@Override
		public List<E> replaceAt( int index, E e ) {
			return reverseViewOf( list.replaceAt( reverseIndexOf( index ), e ) );
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
	}

}
