package de.jbee.lang.seq;

import de.jbee.lang.Bag;
import de.jbee.lang.List;
import de.jbee.lang.Ord;
import de.jbee.lang.Set;
import de.jbee.lang.Sorted;
import de.jbee.lang.Traversal;

abstract class SortedList<E, L extends Sorted & List<E>>
		implements Sorted, List<E> {

	public static <E> Set<E> asSet( Ord<Object> ord, List<E> elements ) {
		return new SortedSet<E>( ord, elements );
	}

	public static <E> Bag<E> asBag( Ord<Object> ord, List<E> elements ) {
		return new SortedBag<E>( ord, elements );
	}

	private final Ord<Object> ord;
	private final List<E> elems;

	SortedList( Ord<Object> ord, List<E> elements ) {
		super();
		this.ord = ord;
		this.elems = elements;
	}

	@Override
	public String toString() {
		return elems.toString();
	}

	@Override
	public List<E> append( E e ) {
		return elems.append( e );
	}

	@Override
	public E at( int index ) {
		return elems.at( index );
	}

	@Override
	public List<E> concat( List<E> other ) {
		return elems.concat( other );
	}

	@Override
	public L deleteAt( int index ) {
		return thisWith( elems.deleteAt( index ) );
	}

	@Override
	public L drop( int count ) {
		return count <= 0
			? self()
			: thisWith( elems.drop( count ) );
	}

	@Override
	public void fill( int offset, Object[] array, int start, int length ) {
		elems.fill( offset, array, start, length );
	}

	@Override
	public List<E> insertAt( int index, E e ) {
		int inOrderIndex = List.indexFor.insertBy( e, ord ).in( elems );
		List<E> inserted = elems.insertAt( inOrderIndex, e );
		return inOrderIndex == index
			? thisWith( inserted )
			: inserted;
	}

	@Override
	public boolean isEmpty() {
		return elems.isEmpty();
	}

	@Override
	public int length() {
		return elems.length();
	}

	@Override
	public Ord<Object> order() {
		return ord;
	}

	@Override
	public List<E> prepand( E e ) {
		return elems.prepand( e );
	}

	@Override
	public List<E> replaceAt( int index, E e ) {
		List<E> replaced = elems.replaceAt( index, e );
		return isSortedIndex( index, replaced )
			? thisWith( replaced )
			: replaced;
	}

	@Override
	public L take( int count ) {
		return thisWith( elems.take( count ) );
	}

	@Override
	public L tidyUp() {
		return thisWith( elems.tidyUp() );
	}

	@Override
	public void traverse( int start, Traversal<? super E> traversal ) {
		elems.traverse( start, traversal );
	}

	final List<E> elems() {
		return elems;
	}

	abstract L self();

	abstract L selfWith( List<E> elements );

	final L thisWith( List<E> elements ) {
		return elements == elems
			? self()
			: selfWith( elements );
	}

	private boolean isSortedIndex( int index, List<E> l ) {
		final E e = l.at( index );
		return ( index == 0 || ord.ord( l.at( index - 1 ), e ).isLe() )
				&& ( index == l.length() - 1 || ord.ord( l.at( l.length() - 1 ), e ).isGe() );
	}

	private static class SortedBag<E>
			extends SortedList<E, Bag<E>>
			implements Bag<E> {

		SortedBag( Ord<Object> ord, List<E> elements ) {
			super( ord, elements );
		}

		@Override
		public Bag<E> add( E e ) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public List<E> entriesAt( int index ) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		Bag<E> self() {
			return this;
		}

		@Override
		Bag<E> selfWith( List<E> elements ) {
			return new SortedBag<E>( order(), elements );
		}

	}

	private static class SortedSet<E>
			extends SortedList<E, Set<E>>
			implements Set<E> {

		SortedSet( Ord<Object> ord, List<E> elements ) {
			super( ord, elements );
		}

		@Override
		public Bag<E> add( E e ) {
			int idx = indexFor( e );
			return null;
		}

		@Override
		public List<E> entriesAt( int index ) {
			return List.with.element( at( index ) );
		}

		@Override
		public Set<E> insert( E e ) {
			int idx = indexFor( e );
			final List<E> elements = elems();
			if ( order().ord( e, elements.at( idx ) ).isEq() ) {
				return this;
			}
			return thisWith( elements.insertAt( idx, e ) );
		}

		private int indexFor( E e ) {
			return List.indexFor.insertBy( e, order() ).in( elems() );
		}

		@Override
		Set<E> self() {
			return this;
		}

		@Override
		Set<E> selfWith( List<E> elements ) {
			return new SortedSet<E>( order(), elements );
		}
	}

}
