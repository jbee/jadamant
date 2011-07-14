package de.jbee.core.list;

import java.util.Iterator;

import de.jbee.core.IndexAccess;

public final class UtileListTransition {

	static final UtileListTransition instance = new UtileListTransition();

	private UtileListTransition() {
		// enforce singleton
	}

	public static final ListTransition none = new NoneTranstion();
	public static final ListTransition reverse = new ReverseListTransition();
	public static final ListTransition tail = new DropLListTransition( 1 );
	public static final ListTransition init = new DropRListTransition( 1 );

	public ListTransition dropsLeft( int count ) {
		return count == 1
			? tail
			: new DropLListTransition( count );
	}

	public ListTransition dropsRight( int count ) {
		return new DropRListTransition( count );
	}

	public ListTransition takesLeft( int count ) {
		return new TakeLListTransition( count );
	}

	public ListTransition takesRight( int count ) {
		return new TakeRListTransition( count );
	}

	public ListTransition takesFrom( int index ) {
		return dropsLeft( index );
	}

	public ListTransition takesUpTo( int index ) {
		return takesLeft( index );
	}

	public ListTransition dropsFrom( int index ) {
		return takesLeft( index );
	}

	public ListTransition dropsUpTo( int index ) {
		return dropsLeft( index );
	}

	public ListTransition takesFromTo( int start, int end ) {
		return sublists( start, end - start + 1 );
	}

	public ListTransition dropsFromTo( int start, int end ) {
		return cutsOut( start, end );
	}

	public ListTransition cutsOut( int start, int end ) {
		return start == end
			? deletes( start )
			: concats( takesUpTo( start - 1 ), takesFrom( end + 1 ) );
	}

	public ListTransition sublists( int start, int length ) {
		return new SublistTransition( start, length );
	}

	public ListTransition swaps( int idx1, int idx2 ) {
		return idx1 == idx2
			? none
			: new SwapTranstion( idx1, idx2 );
	}

	public ListTransition deletes( int index ) {
		return new DeleteTransition( index );
	}

	public ListTransition consec( ListTransition fst, ListTransition snd ) {
		return new ConsecutivelyListTransition( fst, snd );
	}

	public ListTransition concats( ListTransition head, ListTransition tail ) {
		return new ConcatTranstion( head, tail );
	}

	static final class NoneTranstion
			implements ListTransition {

		@Override
		public <E> List<E> from( List<E> list ) {
			return list;
		}
	}

	static final class SwapTranstion
			implements ListTransition {

		private final int idx1;
		private final int idx2;

		SwapTranstion( int idx1, int idx2 ) {
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

	static final class TakeLListTransition
			implements ListTransition {

		private final int count;

		TakeLListTransition( int count ) {
			super();
			this.count = count;
		}

		@Override
		public <E> List<E> from( List<E> list ) {
			return list.take( count );
		}

	}

	static final class DropLListTransition
			implements ListTransition {

		private final int count;

		DropLListTransition( int count ) {
			super();
			this.count = count;
		}

		@Override
		public <E> List<E> from( List<E> list ) {
			return list.drop( count );
		}

	}

	static final class TakeRListTransition
			implements ListTransition {

		private final int count;

		TakeRListTransition( int count ) {
			super();
			this.count = count;
		}

		@Override
		public <E> List<E> from( List<E> list ) {
			return list.drop( list.size() - count );
		}
	}

	static final class DropRListTransition
			implements ListTransition {

		private final int count;

		DropRListTransition( int count ) {
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
