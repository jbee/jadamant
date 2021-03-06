/**
 * 
 */
package de.jbee.lang.seq;

import de.jbee.lang.Array;
import de.jbee.lang.List;
import de.jbee.lang.Sequence;
import de.jbee.lang.Traversal;
import de.jbee.lang.dev.Nonnull;

/**
 * A list consists of a single element and another subsequent {@link List} as tail.
 * 
 * So a {@linkplain ElementaryList} will not have {@link #length()} of 1 as soon as the tail list
 * isn't empty.
 * 
 * @author Jan Bernitt (jan.bernitt@gmx.de)
 */
final class ElementaryList<E>
		implements List<E> {

	/**
	 * @return A single element list with an empty tail.
	 */
	static <E> List<E> element( E element ) {
		return element( element, List.with.<E> noElements() );
	}

	/**
	 * @return A single element list with the <code>tail</code> given.
	 */
	static <E> List<E> element( E element, List<E> tail ) {
		Nonnull.element( element );
		return new ElementaryList<E>( element, tail );
	}

	private final E element;
	private final List<E> tail;

	private ElementaryList( E element, List<E> tail ) {
		super();
		this.element = element;
		this.tail = tail;
	}

	@Override
	public void traverse( int start, Traversal<? super E> traversal ) {
		if ( start > 0 ) {
			tail.traverse( start - 1, traversal );
		} else {
			while ( start == 0 ) {
				start += traversal.incrementOn( element );
			}
			tail.traverse( start, traversal );
		}
	}

	@Override
	public List<E> subsequent() {
		return tail;
	}

	@Override
	public List<E> append( E e ) {
		Nonnull.element( e );
		return tail.isEmpty()
			// if tail is empty another single element list would be appended to this one.
			? List.with.elements( Array.sequence( element, e ) )
			: thisWithTail( tail.append( e ) );
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
	public void fill( int offset, Object[] dest, int start, int length ) {
		if ( start == 0 ) {
			dest[offset] = element;
			tail.fill( offset + 1, dest, 0, length - 1 );
		} else {
			tail.fill( offset, dest, start - 1, length );
		}
	}

	@Override
	public List<E> insertAt( int index, E e ) {
		return index == 0
			? prepand( e )
			: index == 1
				? EVolutionList.dominant( length() + 1, new Object[] { element, e }, tail )
				: thisWithTail( tail.insertAt( index - 1, e ) );
	}

	@Override
	public boolean isEmpty() {
		return false;
	}

	@Override
	public List<E> prepand( E e ) {
		Nonnull.element( e );
		//TODO not use StackList directly - Lister has to be extended to support tail list arguments in some way
		return EVolutionList.dominant( length() + 1, new Object[] { e, element }, tail );
	}

	@Override
	public List<E> replaceAt( int index, E e ) {
		return index == 0
			? element( e, tail )
			: thisWithTail( tail.replaceAt( index - 1, e ) );
	}

	@Override
	public int length() {
		return tail.length() + 1;
	}

	@Override
	public List<E> take( int count ) {
		return count > 0
			? thisWithTail( tail.take( count - 1 ) )
			: List.with.<E> noElements();
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
		return "[" + String.valueOf( element ) + "]" + Sequence.CONCAT_OPERATOR_SYMBOL
				+ tail.toString();
	}

	private List<E> thisWithTail( List<E> tail ) {
		return new ElementaryList<E>( element, tail );
	}

}