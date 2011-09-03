package de.jbee.lang.seq;

import de.jbee.lang.Bag;
import de.jbee.lang.List;
import de.jbee.lang.Ord;
import de.jbee.lang.Traversal;

public class SortedBag<E>
		implements Bag<E> {

	private final Ord<Object> ord;
	private final List<E> elems;

	SortedBag( Ord<Object> ord, List<E> elems ) {
		super();
		this.ord = ord;
		this.elems = elems;
	}

	@Override
	public Bag<E> add( E e ) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Bag<E> deleteAt( int index ) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Bag<E> drop( int count ) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<E> entriesAt( int index ) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Bag<E> take( int count ) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Bag<E> tidyUp() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Ord<Object> order() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<E> append( E e ) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<E> concat( List<E> other ) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<E> insertAt( int index, E e ) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<E> prepand( E e ) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<E> replaceAt( int index, E e ) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isEmpty() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int length() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public E at( int index )
			throws IndexOutOfBoundsException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void fill( int offset, Object[] array, int start, int length ) {
		// TODO Auto-generated method stub

	}

	@Override
	public void traverse( int start, Traversal<? super E> traversal ) {
		// TODO Auto-generated method stub

	}

}
