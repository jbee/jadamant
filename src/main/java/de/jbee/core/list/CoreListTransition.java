package de.jbee.core.list;

import java.util.Iterator;

import de.jbee.core.IndexAccess;

public final class CoreListTransition {

	private CoreListTransition() {
		throw new UnsupportedOperationException( "util class" );
	}

	public static final ListTransition reverse = new ReverseListTransition();

	public static ListTransition dropL( int beginning ) {
		return new DropLListTransition( beginning );
	}

	public static ListTransition concat( ListTransition fst, ListTransition snd ) {
		return new ConcatListTransition( fst, snd );
	}

	static final class DropLListTransition
			implements ListTransition {

		private final int beginning;

		DropLListTransition( int beginning ) {
			super();
			this.beginning = beginning;
		}

		@Override
		public <E> List<E> from( List<E> list ) {
			return list.dropL( beginning );
		}

	}

	static final class ConcatListTransition
			implements ListTransition {

		final ListTransition fst;
		final ListTransition snd;

		ConcatListTransition( ListTransition fst, ListTransition snd ) {
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
		public List<E> dropL( int beginning ) {
			return reverseViewOf( list.dropR( beginning ) );
		}

		@Override
		public List<E> dropR( int ending ) {
			return reverseViewOf( list.dropL( ending ) );
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
		public List<E> takeL( int beginning ) {
			return reverseViewOf( list.takeR( beginning ) );
		}

		@Override
		public List<E> takeR( int ending ) {
			return reverseViewOf( list.takeL( ending ) );
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

	}

}
