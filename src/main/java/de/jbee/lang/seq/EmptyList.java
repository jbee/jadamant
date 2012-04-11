/**
 * 
 */
package de.jbee.lang.seq;

import de.jbee.lang.List;
import de.jbee.lang.Traversal;
import de.jbee.lang.dev.Nonnull;

/**
 * Behavior of an empty {@link List} using {@link List#with} to perform transition into a list with
 * the first element.
 * 
 * @author Jan Bernitt (jan.bernitt@gmx.de)
 */
final class EmptyList<E>
		implements List<E> {

	private static final List<Object> EMPTY = new EmptyList<Object>();

	@SuppressWarnings ( "unchecked" )
	public static <T> List<T> instance() {
		return (List<T>) EMPTY;
	}

	private EmptyList() {
		// enforce singleton
	}

	@Override
	public void fill( int offset, Object[] dest, int start, int length ) {
		// no elements to fill - done without any action
	}

	@Override
	public void traverse( int start, Traversal<? super E> traversal ) {
		// we are finished anyway - no more elements available
	}

	@Override
	public List<E> subsequent() {
		return this;
	}

	@Override
	public List<E> append( E e ) {
		return List.alterBy.reverse().from( prepand( e ) );
	}

	@Override
	public E at( int index )
			throws IndexOutOfBoundsException {
		throw outOfBounds( index );
	}

	@Override
	public List<E> concat( List<E> other ) {
		return other;
	}

	@Override
	public List<E> deleteAt( int index ) {
		throw outOfBounds( index );
	}

	@Override
	public List<E> drop( int count ) {
		return this;
	}

	@Override
	public List<E> insertAt( int index, E e ) {
		if ( index == 0 ) {
			return prepand( e );
		}
		throw outOfBounds( index );
	}

	@Override
	public boolean isEmpty() {
		return true;
	}

	@Override
	public List<E> prepand( E e ) {
		Nonnull.element( e );
		return List.with.element( e );
	}

	@Override
	public List<E> replaceAt( int index, E e ) {
		throw outOfBounds( index );
	}

	@Override
	public int length() {
		return 0;
	}

	@Override
	public List<E> take( int count ) {
		return this;
	}

	@Override
	public List<E> tidyUp() {
		return this;
	}

	@Override
	public String toString() {
		return "[]";
	}

	private IndexOutOfBoundsException outOfBounds( int index ) {
		return new IndexOutOfBoundsException( "No such element: " + index );
	}

	@Override
	public boolean equals( Object obj ) {
		return obj == EmptyList.EMPTY;
	}

	@Override
	public int hashCode() {
		return EmptyList.class.hashCode();
	}
}