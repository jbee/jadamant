package de.jbee.lang.seq;

import de.jbee.lang.Bag;
import de.jbee.lang.Equal;
import de.jbee.lang.List;
import de.jbee.lang.Ord;
import de.jbee.lang.Order;
import de.jbee.lang.Set;
import de.jbee.lang.Sorted;
import de.jbee.lang.Traversal;

abstract class SortedList<E, L extends Sorted & List<E>>
		implements Sorted, List<E> {

	public static <E> Set<E> toSet( List<E> list, Ord<Object> order ) {
		return list instanceof Set<?>
			? (Set<E>) list
			: asSet( List.which.sortsBy( order ).nubsBy( Equal.by( order ) ).from( list ), order );
	}

	public static <E> Bag<E> toBag( List<E> list, Ord<Object> order ) {
		return list instanceof Bag<?>
			? (Bag<E>) list
			: asBag( List.which.sortsBy( order ).from( list ), order );
	}

	static <E> Bag<E> asBag( List<E> elements, Ord<Object> order ) {
		return new SortedBag<E>( order, elements );
	}

	static <E> Set<E> asSet( List<E> elements, Ord<Object> order ) {
		return new SortedSet<E>( order, elements );
	}

	private final Ord<Object> order;
	private final List<E> elems;

	SortedList( Ord<Object> order, List<E> elements ) {
		super();
		this.order = order;
		this.elems = elements;
	}

	@Override
	public String toString() {
		return elems.toString();
	}

	@Override
	public final List<E> append( E e ) {
		//TODO might keep Set/Bag alive - check 
		return elems.append( e );
	}

	@Override
	public final E at( int index ) {
		return elems.at( index );
	}

	@Override
	public final List<E> concat( List<E> other ) {
		return elems.concat( other );
	}

	@Override
	public final L deleteAt( int index ) {
		return thisWith( elems.deleteAt( index ) );
	}

	@Override
	public final L drop( int count ) {
		return count <= 0
			? self()
			: thisWith( elems.drop( count ) );
	}

	@Override
	public final void fill( int offset, Object[] array, int start, int length ) {
		elems.fill( offset, array, start, length );
	}

	@Override
	public final List<E> insertAt( int index, E e ) {
		int inOrderIndex = List.indexFor.insertBy( e, order ).in( elems );
		List<E> inserted = elems.insertAt( inOrderIndex, e );
		return inOrderIndex == index
			? thisWith( inserted ) //FIXME destroys set !?
			: inserted;
	}

	@Override
	public final boolean isEmpty() {
		return elems.isEmpty();
	}

	@Override
	public final int length() {
		return elems.length();
	}

	@Override
	public final Ord<Object> order() {
		return order;
	}

	@Override
	public final List<E> prepand( E e ) {
		return elems.prepand( e );
	}

	@Override
	public final List<E> replaceAt( int index, E e ) {
		List<E> replaced = elems.replaceAt( index, e );
		return isSortedIndex( index, replaced )
			? thisWith( replaced ) //FIXME destroys set !?
			: replaced;
	}

	@Override
	public final L take( int count ) {
		return thisWith( elems.take( count ) );
	}

	@Override
	public final L tidyUp() {
		return thisWith( elems.tidyUp() );
	}

	@Override
	public final void traverse( int start, Traversal<? super E> traversal ) {
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
		return ( index == 0 || order.ord( l.at( index - 1 ), e ).isLe() )
				&& ( index == l.length() - 1 || order.ord( l.at( l.length() - 1 ), e ).isGe() );
	}

	final int insertionIndexFor( E e ) {
		return List.indexFor.insertBy( e, order ).in( elems );
	}

	final boolean containsAt( int index, E e ) {
		return order.ord( e, at( index ) ).isEq();
	}

	private static class SortedBag<E>
			extends SortedList<E, Bag<E>>
			implements Bag<E> {

		SortedBag( Ord<Object> ord, List<E> elements ) {
			super( ord, elements );
		}

		@Override
		public Bag<E> add( E e ) {
			return thisWith( elems().insertAt( insertionIndexFor( e ), e ) );
		}

		@Override
		public List<E> entriesAt( int index ) {
			E e = at( index );
			int l = length();
			int end = index + 1;
			while ( end < l && containsAt( end, e ) ) {
				end++;
			}
			return List.which.slices( index, end ).from( elems() );
		}

		@Override
		Bag<E> self() {
			return this;
		}

		@Override
		Bag<E> selfWith( List<E> elements ) {
			return new SortedBag<E>( order(), elements );
		}

		@Override
		public String toString() {
			String list = super.toString();
			return "#(" + list.substring( 1, list.length() - 1 ) + ")";
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
			int idx = insertionIndexFor( e );
			if ( !containsAt( idx, e ) ) {
				return insert( e, idx );
			}
			return asBag( elems().insertAt( idx, e ), order() );
		}

		private Set<E> insert( E e, int index ) {
			return thisWith( elems().insertAt( index, e ) );
		}

		@Override
		public List<E> entriesAt( int index ) {
			return List.with.element( at( index ) );
		}

		@Override
		public Set<E> insert( E e ) {
			int idx = insertionIndexFor( e );
			if ( containsAt( idx, e ) ) {
				return this;
			}
			return insert( e, idx );
		}

		@Override
		Set<E> self() {
			return this;
		}

		@Override
		Set<E> selfWith( List<E> elements ) {
			return new SortedSet<E>( order(), elements );
		}

		@Override
		public String toString() {
			String list = super.toString();
			return "(" + list.substring( 1, list.length() - 1 ) + ")";
		}

		@Override
		public int indexFor( E e ) {
			int pos = Order.binarySearch( elems(), 0, length(), e, order() );
			return pos < 0
				? -pos - 1
				: pos;
		}
	}

}
