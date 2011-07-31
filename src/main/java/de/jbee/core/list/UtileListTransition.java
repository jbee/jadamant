package de.jbee.core.list;

import java.util.Iterator;

import de.jbee.core.IndexAccess;
import de.jbee.util.ICondition;

public class UtileListTransition
		implements ListTransition {

	public static final ListTransition none = new NoneTranstion();
	public static final ListTransition reverse = new ReverseListTransition();
	public static final ListTransition tail = new DropFirstListTransition( 1 );
	public static final ListTransition init = new DropLastListTransition( 1 );

	static final UtileListTransition instance = new UtileListTransition( none );

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
		return new UtileListTransition( consec( utilised, next ) );
	}

	private ListTransition plain( ListTransition t ) {
		return t instanceof UtileListTransition
			? ( (UtileListTransition) t ).utilised
			: t;
	}

	//TODO some kind of dropWhile working with a condition types with the list type -> other interface
	public UtileListTransition dropsWhile( ICondition<Object> condition ) {
		return followedBy( new DropWhileListTransition( condition ) );
	}

	//TODO some kind of takeWhile working with a condition types with the list type -> other interface
	public UtileListTransition takesWhile( ICondition<Object> condition ) {
		return followedBy( new TakeWhileListTransition( condition ) );
	}

	public UtileListTransition dropsFirst( int count ) {
		return followedBy( count == 1
			? tail
			: new DropFirstListTransition( count ) );
	}

	public UtileListTransition dropsLast( int count ) {
		return followedBy( new DropLastListTransition( count ) );
	}

	public UtileListTransition takesFirst( int count ) {
		return followedBy( new TakeFirstListTransition( count ) );
	}

	public UtileListTransition takesLast( int count ) {
		return followedBy( new TakeLastListTransition( count ) );
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
		return followedBy( idx1 == idx2
			? none
			: new SwapTransition( idx1, idx2 ) );
	}

	public UtileListTransition deletes( int index ) {
		return followedBy( new DeleteTransition( index ) );
	}

	public UtileListTransition concats( ListTransition head, ListTransition tail ) {
		return followedBy( new ConcatTranstion( head, tail ) );
	}

	public ListTransition consec( ListTransition fst, ListTransition snd ) {
		fst = plain( fst );
		snd = plain( snd );
		if ( fst == none ) {
			return snd;
		}
		if ( snd == none ) {
			return fst;
		}
		return new ConsecutivelyListTransition( fst, snd );
	}

	//TODO sort / filter(Predicate)

	static final class NoneTranstion
			implements ListTransition {

		@Override
		public <E> List<E> from( List<E> list ) {
			return list;
		}
	}

	static final class SwapTransition
			implements ListTransition {

		private final int idx1;
		private final int idx2;

		SwapTransition( int idx1, int idx2 ) {
			super();
			this.idx1 = idx1;
			this.idx2 = idx2;
		}

		@Override
		public <E> List<E> from( List<E> list ) {
			final E e1 = list.at( idx1 );
			return list.replaceAt( idx1, list.at( idx2 ) ).replaceAt( idx2, e1 );
		}
	}

	static final class DeleteTransition
			implements ListTransition {

		private final int index;

		DeleteTransition( int index ) {
			super();
			this.index = index;
		}

		@Override
		public <E> List<E> from( List<E> list ) {
			return list.deleteAt( index );
		}
	}

	static final class ConcatTranstion
			implements ListTransition {

		private final ListTransition head;
		private final ListTransition tail;

		ConcatTranstion( ListTransition head, ListTransition tail ) {
			super();
			this.head = head;
			this.tail = tail;
		}

		@Override
		public <E> List<E> from( List<E> list ) {
			return head.from( list ).concat( tail.from( list ) );
		}

	}

	static final class SublistTransition
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
			int size = list.size();
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

	static final class TakeFirstListTransition
			implements ListTransition {

		private final int count;

		TakeFirstListTransition( int count ) {
			super();
			this.count = count;
		}

		@Override
		public <E> List<E> from( List<E> list ) {
			return list.take( count );
		}

	}

	static final class DropFirstListTransition
			implements ListTransition {

		private final int count;

		DropFirstListTransition( int count ) {
			super();
			this.count = count;
		}

		@Override
		public <E> List<E> from( List<E> list ) {
			return list.drop( count );
		}

	}

	static final class TakeLastListTransition
			implements ListTransition {

		private final int count;

		TakeLastListTransition( int count ) {
			super();
			this.count = count;
		}

		@Override
		public <E> List<E> from( List<E> list ) {
			return list.drop( list.size() - count );
		}
	}

	static final class DropLastListTransition
			implements ListTransition {

		private final int count;

		DropLastListTransition( int count ) {
			super();
			this.count = count;
		}

		@Override
		public <E> List<E> from( List<E> list ) {
			return list.take( list.size() - count );
		}

	}

	static final class ConsecutivelyListTransition
			implements ListTransition {

		final ListTransition fst;
		final ListTransition snd;

		ConsecutivelyListTransition( ListTransition fst, ListTransition snd ) {
			super();
			this.fst = fst;
			this.snd = snd;
		}

		@Override
		public <E> List<E> from( List<E> list ) {
			return snd.from( fst.from( list ) );
		}
	}

	static final class TakeWhileListTransition
			implements ListTransition {

		final ICondition<Object> condition;

		TakeWhileListTransition( ICondition<Object> condition ) {
			super();
			this.condition = condition;
		}

		@Override
		public <E> List<E> from( List<E> list ) {
			int size = list.size();
			for ( int i = 0; i < size; i++ ) {
				if ( condition.fulfilledBy( list.at( i ) ) ) {
					return list.take( i );
				}
			}
			return list;
		}

	}

	static final class DropWhileListTransition
			implements ListTransition {

		final ICondition<Object> condition;

		DropWhileListTransition( ICondition<Object> condition ) {
			super();
			this.condition = condition;
		}

		@Override
		public <E> List<E> from( List<E> list ) {
			int size = list.size();
			for ( int i = 0; i < size; i++ ) {
				if ( condition.fulfilledBy( list.at( i ) ) ) {
					return list.drop( i );
				}
			}
			return list.drop( size ); // will return the empty list
		}
	}

	static final class ReverseListTransition
			implements ListTransition {

		@Override
		public <E> List<E> from( List<E> list ) {
			if ( list instanceof ReversingList<?> ) {
				return ( (ReversingList<E>) list ).list;
			}
			return new ReversingList<E>( list );
		}

	}

	final static class ReversingList<E>
			implements List<E> {

		final List<E> list;

		ReversingList( List<E> beingReversed ) {
			super();
			this.list = beingReversed;
		}

		@Override
		public void fill( int offset, Object[] array, int start, int length ) {
			int size = list.size();
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
		public E at( int index )
				throws IndexOutOfBoundsException {
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
			return reverseViewOf( list.take( list.size() - count ) );
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
		public Iterator<E> iterator() {
			return IndexAccess.reverseIterator( list, 0, size() );
		}

		@Override
		public List<E> prepand( E e ) {
			return reverseViewOf( list.append( e ) );
		}

		@Override
		public int size() {
			return list.size();
		}

		@Override
		public List<E> take( int count ) {
			return reverseViewOf( list.drop( list.size() - count ) );
		}

		@Override
		public List<E> tidyUp() {
			return reverseViewOf( list.tidyUp() );
		}

		private int reverseIndexOf( int index ) {
			return list.size() - 1 - index;
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
	}

}
