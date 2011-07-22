/**
 * 
 */
package de.jbee.core.list;

import java.util.Iterator;

import de.jbee.core.IndexAccess;
import de.jbee.core.Nonnull;

final class SingleElementList<E>
		implements List<E> {

	static <E> List<E> with( E element, List<E> tail ) {
		return new SingleElementList<E>( element, tail );
	}

	private final E element;

	private final List<E> tail;

	private SingleElementList( E element, List<E> tail ) {
		super();
		this.element = element;
		this.tail = tail;
	}

	@Override
	public List<E> append( E e ) {
		return thisWithTail( tail.append( e ) );
	}

	@Override
	public E at( int index ) {
		return index == 0
			? element
			: tail.at( index - 1 );
	}

	@Override
	public List<E> concat( List<E> other ) {
		return thisWithTail( tail.concat( other ) );
	}

	@Override
	public List<E> deleteAt( int index ) {
		return index == 0
			? tail
			: thisWithTail( tail.deleteAt( index - 1 ) );
	}

	@Override
	public List<E> drop( int count ) {
		return count <= 0
			? this
			: count == 1
				? tail
				: tail.drop( count - 1 );
	}

	@Override
	public void fill( int offset, Object[] array, int start, int length ) {
		if ( start == 0 ) {
			array[offset] = element;
			tail.fill( offset + 1, array, 0, length - 1 );
		} else {
			tail.fill( offset, array, start - 1, length );
		}
	}

	@Override
	public List<E> insertAt( int index, E e ) {
		return index == 0
			? prepand( e )
			: thisWithTail( tail.insertAt( index - 1, e ) );
	}

	@Override
	public boolean isEmpty() {
		return false;
	}

	@Override
	public Iterator<E> iterator() {
		return IndexAccess.iterator( this, 0, size() );
	}

	@Override
	public List<E> prepand( E e ) {
		Nonnull.element( e );
		return StackList.primary( size() + 1, StackList.stack( e, 2 ), this );
	}

	@Override
	public List<E> replaceAt( int index, E e ) {
		return index == 0
			? SingleElementList.with( e, tail )
			: thisWithTail( tail.replaceAt( index - 1, e ) );
	}

	@Override
	public int size() {
		return tail.size() + 1;
	}

	@Override
	public List<E> take( int count ) {
		return count > 0
			? thisWithTail( tail.take( count - 1 ) )
			: InitList.LISTER.<E> noElements();
	}

	@Override
	public List<E> tidyUp() {
		final List<E> tidyTail = tail.tidyUp();
		return tidyTail == tail
			? this
			: thisWithTail( tidyTail );
	}

	@Override
	public String toString() {
		return "[" + String.valueOf( element ) + "]" + List.CONCAT_OPERATOR + tail.toString();
	}

	private List<E> thisWithTail( List<E> tail ) {
		return new SingleElementList<E>( element, tail );
	}

}