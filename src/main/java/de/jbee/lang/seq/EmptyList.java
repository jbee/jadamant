/**
 * 
 */
package de.jbee.lang.seq;

import de.jbee.lang.Enumerate;
import de.jbee.lang.List;
import de.jbee.lang.Traversal;
import de.jbee.lang.dev.Nonnull;

final class EmptyList<E>
		implements List<E> {

	private static final List<Object> EMPTY = new EmptyList<Object>();

	@SuppressWarnings ( "unchecked" )
	public static <T> List<T> instance() {
		return (List<T>) EMPTY;
	}

	@Override
	public void fill( int offset, Object[] array, int start, int length ) {
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
		if ( e instanceof Integer ) {
			return integerList( e );
		}
		if ( e.getClass().isEnum() ) {
			return enumList( e );
		}
		return ElementaryList.element( e, this );
	}

	@SuppressWarnings ( "unchecked" )
	private <T> List<E> enumList( E e ) {
		java.lang.Enum<?> v = (java.lang.Enum<?>) e;
		return (List<E>) EnumList.withElement( v );
	}

	@SuppressWarnings ( "unchecked" )
	private List<E> integerList( E e ) {
		Integer i = (Integer) e;
		return (List<E>) new EnumList<Integer>( Enumerate.INTEGERS, i, i );
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