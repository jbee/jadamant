package de.jbee.core.list;

import java.util.Iterator;

import de.jbee.core.IndexAccess;
import de.jbee.core.type.Enum;

public final class EnumList<E>
		implements List<E> {

	private final Enum<E> type;
	private final int startOrdinal;
	private final int endOrdinal;

	EnumList( Enum<E> type, E start, E end ) {
		this( type, type.toOrdinal( start ), type.toOrdinal( end ) );
	}

	EnumList( Enum<E> type, int startOrdianl, int endOrdinal ) {
		super();
		this.type = type;
		this.startOrdinal = startOrdianl;
		this.endOrdinal = endOrdinal;
	}

	private List<E> list( int startOrdinal, int endOrdinal ) {
		return new EnumList<E>( type, startOrdinal, endOrdinal );
	}

	@Override
	public List<E> append( E e ) {
		if ( type.toOrdinal( e ) == endOrdinal + 1 ) {
			return list( startOrdinal, endOrdinal + 1 );
		}
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<E> concat( List<E> other ) {
		if ( other instanceof EnumList<?> ) {
			EnumList<E> o = (EnumList<E>) other;
			if ( o.startOrdinal == endOrdinal + 1 ) {
				return list( startOrdinal, o.endOrdinal );
			}
		}
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<E> deleteAt( int index ) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<E> dropL( int beginning ) {
		return beginning >= size()
			? List.with.<E> noElements()
			: list( startOrdinal + beginning, endOrdinal );
	}

	@Override
	public List<E> dropR( int ending ) {
		return ending >= size()
			? List.with.<E> noElements()
			: list( startOrdinal, endOrdinal - ending );
	}

	@Override
	public List<E> insertAt( int index, E e ) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<E> prepand( E e ) {
		if ( type.toOrdinal( e ) == startOrdinal - 1 ) {
			return list( startOrdinal - 1, endOrdinal );
		}
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<E> replaceAt( int index, E e ) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<E> takeL( int beginning ) {
		return beginning >= size()
			? this
			: list( startOrdinal, startOrdinal + beginning );
	}

	@Override
	public List<E> takeR( int ending ) {
		return ending >= size()
			? this
			: list( endOrdinal - ending, endOrdinal );
	}

	@Override
	public List<E> tidyUp() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isEmpty() {
		return false;
	}

	@Override
	public int size() {
		return Math.abs( endOrdinal - startOrdinal ) + 1;
	}

	@Override
	public Iterator<E> iterator() {
		return IndexAccess.iterator( this, 0, size() );
	}

	@Override
	public E at( int index )
			throws IndexOutOfBoundsException {
		if ( index < 0 || index > size() ) {
			throw new IndexOutOfBoundsException( "no element at index: " + index );
		}
		return type.toEnum( startOrdinal + index );
	}

	@Override
	public String toString() {
		return "[" + String.valueOf( type.toEnum( startOrdinal ) ) + ".."
				+ String.valueOf( type.toEnum( endOrdinal ) ) + "]";
	}
}
