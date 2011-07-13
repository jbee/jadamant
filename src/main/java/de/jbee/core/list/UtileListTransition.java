package de.jbee.core.list;

import java.util.Iterator;

import de.jbee.core.IndexAccess;

public final class UtileListTransition {

	static final UtileListTransition instance = new UtileListTransition();

	private UtileListTransition() {
		// enforce singleton
	}

	public static final ListTransition reverse = new ReverseListTransition();
	public static final ListTransition tail = new DropLListTransition( 1 );
	public static final ListTransition init = new DropRListTransition( 1 );

	public ListTransition dropLeft( int count ) {
		return count == 1
			? tail
			: new DropLListTransition( count );
	}

	public ListTransition dropRight( int count ) {
		return new DropRListTransition( count );
	}

	public ListTransition takeLeft( int count ) {
		return new TakeLListTransition( count );
	}

	public ListTransition takeRight( int count ) {
		return new TakeRListTransition( count );
	}

	public ListTransition takeFrom( int index ) {
		return dropLeft( index );
	}

	public ListTransition takeUpTo( int index ) {
		return takeLeft( index );
	}

	public ListTransition dropFrom( int index ) {
		return takeLeft( index );
	}

	public ListTransition dropUpTo( int index ) {
		return dropLeft( index );
	}

	public ListTransition takeFromTo( int start, int end ) {
		return sublist( start, end - start + 1 );
	}

	public ListTransition dropFromTo( int start, int end ) {
		return concat( takeUpTo( start - 1 ), takeFrom( end + 1 ) );
	}

	public ListTransition sublist( int start, int length ) {
		return new SublistTransition( start, length );
	}

	public ListTransition consec( ListTransition fst, ListTransition snd ) {
		return new ConsecutivelyListTransition( fst, snd );
	}

	public ListTransition concat( ListTransition head, ListTransition tail ) {
		return new ConcatTranstion( head, tail );
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
