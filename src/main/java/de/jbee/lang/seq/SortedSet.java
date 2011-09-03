package de.jbee.lang.seq;

import de.jbee.lang.Bag;
import de.jbee.lang.List;
import de.jbee.lang.Ord;
import de.jbee.lang.Set;
import de.jbee.lang.Traversal;

class SortedSet<E>
		implements Set<E> {

	private final Ord<Object> ord;
	private final List<E> elems;

	SortedSet( Ord<Object> ord, List<E> elements ) {
		super();
		this.ord = ord;
		this.elems = elements;
	}

	private Set<E> thisWith( List<E> elements ) {
		return elements == elems
			? this
			: new SortedSet<E>( ord, elements );
	}

	@Override
	public Set<E> insert( E e ) {
		return thisWith( elems.insertAt( List.indexFor.insertBy( e, ord ).in( elems ), e ) );
	}

	@Override
	public Ord<Object> order() {
		return ord;
	}

	@Override
	public List<E> append( E e ) {
		return elems.append( e );
	}

	@Override
	public List<E> concat( List<E> other ) {
		return elems.concat( other );
	}

	@Override
	public Set<E> deleteAt( int index ) {
		return thisWith( elems.deleteAt( index ) );
	}

	@Override
	public Set<E> drop( int count ) {
		return count <= 0
			? this
			: thisWith( elems.drop( count ) );
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

	private boolean isSortedIndex( int index, List<E> l ) {
		final E e = l.at( index );
		return ( index == 0 || ord.ord( l.at( index - 1 ), e ).isLe() )
				&& ( index == l.length() - 1 || ord.ord( l.at( l.length() - 1 ), e ).isGe() );
	}

	@Override
	public Set<E> take( int count ) {
		return thisWith( elems.take( count ) );
	}

	@Override
	public Set<E> tidyUp() {
		return thisWith( elems.tidyUp() );
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
	public E at( int index ) {
		return elems.at( index );
	}

	@Override
	public void fill( int offset, Object[] array, int start, int length ) {
		elems.fill( offset, array, start, length );
	}

	@Override
	public void traverse( int start, Traversal<? super E> traversal ) {
		elems.traverse( start, traversal );
	}

	@Override
	public Bag<E> add( E e ) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<E> entriesAt( int index ) {
		return List.with.element( at( index ) );
	}

}
