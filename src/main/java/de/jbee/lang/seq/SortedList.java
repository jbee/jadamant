package de.jbee.lang.seq;

import de.jbee.lang.Bag;
import de.jbee.lang.IndexDeterminable;
import de.jbee.lang.List;
import de.jbee.lang.ListIndex;
import de.jbee.lang.Map;
import de.jbee.lang.Ord;
import de.jbee.lang.Order;
import de.jbee.lang.Set;
import de.jbee.lang.Sorted;
import de.jbee.lang.Traversal;

abstract class SortedList<E, L extends Sorted & List<E>>
		implements IndexDeterminable<E>, Sorted, List<E> {

	/**
	 * The elements are considered to be ordered by given order.
	 */
	static <E> Bag<E> bagOf( List<E> elements, Ord<Object> order ) {
		return new BagList<E>( order, elements );
	}

	/**
	 * The elements are considered to be ordered by given order and containing no duplicates.
	 */
	static <E> Set<E> setOf( List<E> elements, Ord<Object> order ) {
		return new SetList<E>( order, elements );
	}

	static <E> Map<E> mapOf( Set<Map.Entry<E>> entries ) {
		return new MapList<E>( entries );
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
			? thisWith( inserted ) //FIXME might destroy Set through duplicate element!
			: inserted;
	}

	@Override
	public int indexFor( E e ) {
		int pos = Order.binarySearch( elems(), 0, length(), e, order() );
		return pos < 0
			? ListIndex.NOT_CONTAINED
			: pos;
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
		return indexInOrder( index, replaced )
			? thisWith( replaced ) //FIXME might destroy Set through duplicate element!
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

	private boolean indexInOrder( int index, List<E> l ) {
		final E e = l.at( index );
		return ( index == 0 || order.ord( l.at( index - 1 ), e ).isLe() )
				&& ( index == l.length() - 1 || order.ord( l.at( l.length() - 1 ), e ).isGe() );
	}

	final int insertionIndexFor( E e ) {
		return List.indexFor.insertBy( e, order ).in( elems );
	}

	final boolean containsAt( int index, E e ) {
		return index < elems.length() && order.ord( e, at( index ) ).isEq();
	}

	private static class BagList<E>
			extends SortedList<E, Bag<E>>
			implements Bag<E> {

		BagList( Ord<Object> ord, List<E> elements ) {
			super( ord, elements );
		}

		@Override
		public Bag<E> subsequent() {
			return bagOf( elems().subsequent(), order() );
		}

		@Override
		public Bag<E> add( E e ) {
			return thisWith( elems().insertAt( insertionIndexFor( e ), e ) );
		}

		@Override
		public Bag<E> entriesAt( int index ) {
			int l = length();
			if ( index < 0 || index >= l ) {
				return bagOf( List.with.<E> noElements(), order() );
			}
			E e = at( index );
			int end = index + 1;
			while ( end < l && containsAt( end, e ) ) {
				end++;
			}
			return bagOf( List.that.slices( index, end ).from( elems() ), order() );
		}

		@Override
		Bag<E> self() {
			return this;
		}

		@Override
		Bag<E> selfWith( List<E> elements ) {
			return new BagList<E>( order(), elements );
		}

		@Override
		public String toString() {
			String list = super.toString();
			return "#(" + list.substring( 1, list.length() - 1 ) + ")";
		}
	}

	private static class SetList<E>
			extends SortedList<E, Set<E>>
			implements Set<E> {

		SetList( Ord<Object> ord, List<E> elements ) {
			super( ord, elements );
		}

		@Override
		public Set<E> subsequent() {
			return setOf( elems().subsequent(), order() );
		}

		@Override
		public Bag<E> add( E e ) {
			int idx = insertionIndexFor( e );
			if ( !containsAt( idx, e ) ) {
				return insert( e, idx );
			}
			return bagOf( elems().insertAt( idx, e ), order() );
		}

		private Set<E> insert( E e, int index ) {
			return thisWith( elems().insertAt( index, e ) );
		}

		@Override
		public Bag<E> entriesAt( int index ) {
			if ( index < 0 || index >= length() ) {
				return bagOf( List.with.<E> noElements(), order() );
			}
			return bagOf( List.with.element( at( index ) ), order() );
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
			return new SetList<E>( order(), elements );
		}

		@Override
		public String toString() {
			String list = super.toString();
			return "(" + list.substring( 1, list.length() - 1 ) + ")";
		}

	}

	private static class MapList<V>
			extends SetList<Map.Entry<V>>
			implements Map<V> {

		MapList( Set<Map.Entry<V>> entries ) {
			super( ENTRY_ORDER, entries );
		}

		@Override
		public V get( CharSequence key ) {
			final int idx = indexFor( new Entry<V>( key.toString(), null ) );
			return idx >= 0
				? at( idx ).value()
				: null;
		}

		@Override
		public Map<V> put( CharSequence key, V value ) {
			return new MapList<V>( insert( new Entry<V>( key.toString(), value ) ) );
		}

		static final class Entry<V>
				implements Map.Entry<V> {

			final String key;
			final V value;

			Entry( String key, V value ) {
				super();
				this.key = key;
				this.value = value;
			}

			@Override
			public CharSequence key() {
				return key;
			}

			@Override
			public V value() {
				return value;
			}

		}
	}
}
