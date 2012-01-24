package de.jbee.lang.seq;

import static de.jbee.lang.seq.ElementList.elements;
import de.jbee.lang.Bag;
import de.jbee.lang.IndexDeterminable;
import de.jbee.lang.List;
import de.jbee.lang.ListIndex;
import de.jbee.lang.Map;
import de.jbee.lang.Multimap;
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

	static <E> Multimap<E> multimapOf( List<Map.Entry<E>> entries, Ord<Object> keyOrder,
			Ord<Object> valueOrder ) {
		return new MultimapList<E>( keyOrder, valueOrder, entries );
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

	public L entriesAt( int index ) {
		int l = length();
		if ( index < 0 || index >= l ) {
			return thisWith( List.with.<E> noElements() );
		}
		E e = at( index );
		int end = index + 1;
		while ( end < l && containsAt( end, e ) ) {
			end++;
		}
		return thisWith( List.that.slices( index, end ).from( elems() ) );
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
		List<E> inserted = elems.insertAt( index, e );
		return indexInOrder( index, inserted )
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
		Ord<Object> order = overallOrder();
		return ( index == 0 || order.ord( l.at( index - 1 ), e ).isLe() )
				&& ( index == l.length() - 1 || order.ord( l.at( l.length() - 1 ), e ).isGe() );
	}

	int insertionIndexFor( E e ) {
		return List.indexFor.insertBy( e, overallOrder() ).in( elems );
	}

	Ord<Object> overallOrder() {
		return order;
	}

	final boolean containsAt( int index, E e ) {
		return index < elems.length() && order.ord( e, at( index ) ).isEq();
	}

	final L insert( E e, int index ) {
		return thisWith( elems().insertAt( index, e ) );
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

		@Override
		public Set<E> entriesAt( int index ) {
			if ( index < 0 || index >= length() ) {
				return setOf( List.with.<E> noElements(), order() );
			}
			return setOf( List.with.element( at( index ) ), order() );
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
			extends SortedList<Map.Entry<V>, Map<V>>
			implements Map<V> {

		MapList( List<Map.Entry<V>> entries ) {
			this( ENTRY_ORDER, entries );
		}

		MapList( Ord<Object> order, List<Map.Entry<V>> entries ) {
			super( order, entries );
		}

		@Override
		public V valueFor( CharSequence key ) {
			final int idx = indexFor( entry( key, (V) null ) );
			return idx >= 0
				? at( idx ).value()
				: null;
		}

		@Override
		public Bag<V> valuesFor( CharSequence key ) {
			final V value = valueFor( key );
			List<V> elements = value == null
				? List.with.<V> noElements()
				: List.with.element( value );
			return bagOf( elements, Order.keep );
		}

		@Override
		public Map<V> insert( CharSequence key, V value ) {
			return new MapList<V>( insert( entry( key, value ) ) );
		}

		@Override
		public Multimap<V> add( Map.Entry<V> e ) {
			int idx = insertionIndexFor( e );
			if ( !containsAt( idx, e ) ) {
				return insert( e, idx );
			}
			return multimapOf( elems().insertAt( idx, e ), order(), Order.keep );
		}

		@Override
		public Map<V> entriesAt( int index ) {
			if ( index < 0 || index >= length() ) {
				return thisWith( List.with.<Map.Entry<V>> noElements() );
			}
			return thisWith( List.with.element( at( index ) ) );
		}

		@Override
		public Map<V> insert( Map.Entry<V> e ) {
			int idx = insertionIndexFor( e );
			if ( containsAt( idx, e ) ) {
				return this;
			}
			return insert( e, idx );
		}

		@Override
		Map<V> self() {
			return this;
		}

		@Override
		Map<V> selfWith( List<Map.Entry<V>> entries ) {
			return new MapList<V>( order(), entries );
		}

		@Override
		public Map<V> subsequent() {
			return thisWith( elems().subsequent() );
		}

		@Override
		public String toString() {
			String list = super.toString();
			return "{" + list.substring( 1, list.length() - 1 ) + "}";
		}

	}

	private static final class Entry<V>
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

		@Override
		public String toString() {
			return key + "=>" + value;
		}

	}

	static <V> Map.Entry<V> entry( CharSequence key, V value ) {
		return new SortedList.Entry<V>( key.toString(), value );
	}

	private static class MultimapList<V>
			extends SortedList<Map.Entry<V>, Multimap<V>>
			implements Multimap<V> {

		private final Ord<Object> valueOrder;

		//FIXME use a extra Ord for the suborder of equal keys - the valuesFor than can return a Bag also
		MultimapList( Ord<Object> keyOrder, Ord<Object> valueOrder, List<Map.Entry<V>> entries ) {
			super( keyOrder, entries );
			this.valueOrder = valueOrder;
		}

		@Override
		Multimap<V> self() {
			return this;
		}

		@Override
		Multimap<V> selfWith( List<Map.Entry<V>> entries ) {
			return new MultimapList<V>( order(), valueOrder, entries );
		}

		@Override
		Ord<Object> overallOrder() {
			return Order.sub2( order(), Order.nullsave( valueOrder ) );
		}

		@Override
		public Multimap<V> add( Map.Entry<V> e ) {
			return thisWith( elems().insertAt( insertionIndexFor( e ), e ) ); //TODO same as bag impl. -> DRY
		}

		@Override
		public Bag<V> valuesFor( CharSequence key ) {
			Map.Entry<V> e = entry( key, (V) null );
			int first = indexFor( e );
			if ( first == ListIndex.NOT_CONTAINED ) {
				return bagOf( List.with.<V> noElements(), valueOrder );
			}
			while ( first > 0 && order().ord( e, at( first - 1 ) ).isEq() ) {
				first--;
			}
			final int l = length();
			int idx = first + 1;
			while ( idx < l && order().ord( e, at( idx ) ).isEq() ) {
				idx++;
			}
			return bagOf( elements( List.that.slices( first, idx ).from( elems() ) ), valueOrder );
		}

		@Override
		public Multimap<V> insert( CharSequence key, V value ) {
			return add( entry( key, value ) );
		}

		@Override
		public Multimap<V> subsequent() {
			return thisWith( elems().subsequent() );
		}

	}
}
