package de.jbee.lang.seq;

import de.jbee.lang.Element;
import de.jbee.lang.List;
import de.jbee.lang.Traversal;

/**
 * A list maps to the {@link Element}'s values of any kind of list consists of {@link Element}s.
 * 
 * @see ElementaryList
 * 
 * @author Jan Bernitt (jan.bernitt@gmx.de)
 */
class ElementList<E>
		implements List<E> {

	private final List<? extends Element<E>> elems;

	private ElementList( List<? extends Element<E>> elems ) {
		super();
		this.elems = elems;
	}

	static <E> List<E> elements( List<? extends Element<E>> elems ) {
		return new ElementList<E>( elems );
	}

	private List<E> list( List<? extends Element<E>> elems ) {
		return elems == this.elems
			? this
			: elements( elems );
	}

	/**
	 * maps to the values of the {@link Element}s in this list.
	 */
	private List<E> values() {
		return List.with.elements( this );
	}

	@Override
	public List<E> append( E e ) {
		return values().append( e );
	}

	@Override
	public List<E> concat( List<E> other ) {
		return values().concat( other );
	}

	@Override
	public List<E> deleteAt( int index ) {
		return values().deleteAt( index );
	}

	@Override
	public List<E> drop( int count ) {
		return list( elems.drop( count ) );
	}

	@Override
	public List<E> insertAt( int index, E e ) {
		return values().insertAt( index, e );
	}

	@Override
	public List<E> prepand( E e ) {
		return values().prepand( e );
	}

	@Override
	public List<E> replaceAt( int index, E e ) {
		return values().replaceAt( index, e );
	}

	@Override
	public List<E> subsequent() {
		return list( elems.subsequent() );
	}

	@Override
	public List<E> take( int count ) {
		return list( elems.take( count ) );
	}

	@Override
	public List<E> tidyUp() {
		return values(); // we create values so we know its tidied up.
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
		return elems.at( index ).value();
	}

	@Override
	public void fill( int offset, Object[] array, int start, int length ) {
		int l = length();
		for ( int i = start; i < Math.min( l, start + length ); i++ ) {
			array[offset++] = at( i );
		}
	}

	@Override
	public void traverse( int start, Traversal<? super E> traversal ) {
		if ( start < 0 ) {
			return;
		}
		final int l = length();
		int inc = 0;
		while ( inc >= 0 && start < l ) {
			inc = traversal.incrementOn( at( start ) );
			start += inc;
		}
	}

	@Override
	public String toString() {
		StringBuilder b = new StringBuilder();
		for ( int i = 0; i < length(); i++ ) {
			b.append( ',' );
			b.append( at( i ) );
		}
		return '[' + ( b.length() == 0
			? ""
			: b.substring( 1 ) ) + ']';
	}
}
