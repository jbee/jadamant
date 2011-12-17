/**
 * 
 */
package de.jbee.lang.seq;

import de.jbee.lang.Enum;
import de.jbee.lang.Enumerate;
import de.jbee.lang.Enumerator;
import de.jbee.lang.EnumeratorFactory;
import de.jbee.lang.List;
import de.jbee.lang.Traversal;
import de.jbee.lang.dev.Nonnull;

final class EnumList<E>
		implements List<E> {

	//OPEN isn't a EnumList also a Set ?

	static final EnumeratorFactory ENUMERATOR_FACTORY = new EnumListEnumeratorFactory();

	static <T extends java.lang.Enum<?>> List<T> withElement( T e ) {
		Class<? extends java.lang.Enum<?>> c = e.getDeclaringClass();
		@SuppressWarnings ( "unchecked" )
		Enum<T> type = (Enum<T>) Enumerate.type( c );
		return new EnumList<T>( type, e, e );
	}

	private final List<E> tail;
	private final Enum<E> type;
	private final int firstOrdinal;
	private final int lastOrdinal;

	EnumList( Enum<E> type, E first, E last ) {
		this( type, type.toOrdinal( first ), type.toOrdinal( last ), List.with.<E> noElements() );
	}

	EnumList( Enum<E> type, int firstOrdianl, int lastOrdinal, List<E> tail ) {
		super();
		this.type = type;
		this.tail = tail;
		this.firstOrdinal = firstOrdianl;
		this.lastOrdinal = lastOrdinal;
	}

	@Override
	public List<E> tail() {
		return tail;
	}

	@Override
	public void traverse( int start, Traversal<? super E> traversal ) {
		final int l = len();
		int i = start;
		int inc = 0;
		while ( inc >= 0 && i < l ) { //TODO here is some code duplication with stack list - its just the element access that differs
			inc = traversal.incrementOn( type.toEnum( firstPlus( i ) ) );
			i += inc;
		}
		if ( inc > 0 ) {
			tail.traverse( i - l, traversal );
		}
	}

	@Override
	public List<E> append( E e ) {
		Nonnull.element( e );
		final int eOrdinal = type.toOrdinal( e );
		if ( tail.isEmpty() ) {
			final int nextOrdinal = lastPlus( 1 );
			if ( eOrdinal == nextOrdinal ) {
				return list( firstOrdinal, nextOrdinal );
			}
		}
		final int length = length();
		if ( length == 1 ) { // check for a desc sequence
			return firstMinus( 1 ) == eOrdinal
				? list( firstOrdinal, eOrdinal )
				: ElementList.with( at( 0 ) ).append( e );
		}
		if ( len() == 1 ) {
			return ElementList.with( at( 0 ), tail ).append( e );
		}
		return thisWithTail( tail.append( e ) );
	}

	@Override
	public void fill( int offset, Object[] array, int start, int length ) {
		final int l = len();
		if ( start < l ) {
			if ( ascending() ) {
				int startOrd = firstOrdinal + start;
				int endOrd = Math.min( lastOrdinal, startOrd + length - 1 );
				for ( int ord = startOrd; ord <= endOrd; ord++ ) {
					array[offset++] = type.toEnum( ord );
				}
			} else {
				int startOrd = firstOrdinal - start;
				int endOrd = Math.max( lastOrdinal, startOrd - length + 1 );
				for ( int ord = startOrd; ord >= endOrd; ord-- ) {
					array[offset++] = type.toEnum( ord );
				}
			}
			if ( start + length > l ) {
				tail.fill( offset, array, 0, length - ( l - start ) );
			}
		} else {
			tail.fill( offset, array, start - l, length );
		}
	}

	@Override
	public E at( int index ) {
		final int l = len();
		return index >= l
			? tail.at( index - l )
			: type.toEnum( firstPlus( index ) );
	}

	@Override
	public List<E> concat( List<E> other ) {
		if ( !tail.isEmpty() ) {
			return tail.concat( other );
		}
		if ( other instanceof EnumList<?> ) {
			EnumList<E> o = (EnumList<E>) other;
			if ( ascending() == o.ascending() && o.firstOrdinal == lastPlus( 1 ) ) {
				return list( firstOrdinal, o.lastOrdinal );
			}
		}
		return thisWithTail( other );
	}

	@Override
	public List<E> deleteAt( int index ) {
		final int length = len();
		if ( index >= length ) { // its in the tail
			return thisWithTail( tail.deleteAt( index - length ) );
		}
		if ( index == 0 ) { // first of this enum list
			return length == 1
				? tail
				: list( firstPlus( 1 ), lastOrdinal );
		}
		if ( index == length - 1 ) { // last of this enum list
			return length == 1
				? tail
				: list( firstOrdinal, lastMinus( 1 ) );
		}
		// sadly: in between this list
		final int indexOrdinal = type.toOrdinal( at( index ) );
		return list( firstOrdinal, ordinalMinus( indexOrdinal, 1 ), list( ordinalPlus(
				indexOrdinal, 1 ), lastOrdinal, tail ) );
	}

	@Override
	public List<E> drop( int count ) {
		if ( count <= 0 ) {
			return this;
		}
		final int length = length();
		if ( count >= length ) {
			return empty();
		}
		final int l = len();
		if ( count == l ) {
			return tail;
		}
		if ( count > l ) {
			return tail.drop( count - l );
		}
		return list( firstPlus( count ), lastOrdinal );
	}

	@Override
	public List<E> insertAt( int index, E e ) {
		Nonnull.element( e );
		if ( index == 0 ) {
			final int eOrdinal = type.toOrdinal( e );
			if ( eOrdinal == ordinalMinus( firstOrdinal, 1 ) ) {
				return list( eOrdinal, lastOrdinal );
			}
			return ElementList.with( e, this );
		}
		final int l = len();
		if ( index >= l ) {
			return thisWithTail( tail.insertAt( index - l, e ) );
		}
		// somewhere in between this enumeration
		return list( firstOrdinal, firstPlus( index - 1 ), ElementList.with( e, list(
				firstPlus( index ), lastOrdinal, tail ) ) );
	}

	@Override
	public boolean isEmpty() {
		return false;
	}

	@Override
	public List<E> prepand( E e ) {
		Nonnull.element( e );
		final int priorOrdinal = firstMinus( 1 );
		final int eOrdinal = type.toOrdinal( e );
		if ( eOrdinal == priorOrdinal ) {
			return list( priorOrdinal, lastOrdinal );
		}
		final int length = length();
		if ( length == 1 ) { // check a descending sequence
			return firstPlus( 1 ) == eOrdinal
				? list( eOrdinal, lastOrdinal )
				: ElementList.with( at( 0 ) ).prepand( e );
		}
		if ( len() == 1 ) {
			return ElementList.with( at( 0 ), tail ).prepand( e );
		}
		return list( eOrdinal, eOrdinal, this );
	}

	@Override
	public List<E> replaceAt( int index, E e ) {
		final int l = len();
		if ( index >= l ) {
			thisWithTail( tail.replaceAt( index - l, e ) );
		}
		E ei = at( index );
		if ( ei == e ) { // not use equals - equality might be defined different
			return this;
		}
		if ( index == 0 ) {
			return drop( 1 ).prepand( e );
		}
		return take( index ).concat( ElementList.with( e, drop( index + 1 ) ) );
	}

	@Override
	public int length() {
		return len() + tail.length();
	}

	@Override
	public List<E> take( int count ) {
		if ( count <= 0 ) {
			return empty();
		}
		if ( count >= length() ) {
			return this;
		}
		final int l = len();
		if ( count == l ) {
			return thisWithTail( empty() );
		}
		return count <= l
			? list( firstOrdinal, firstPlus( count - 1 ), empty() )
			: thisWithTail( tail.take( count - l ) );
	}

	@Override
	public List<E> tidyUp() {
		List<E> tidyTail = tail.tidyUp();
		return tidyTail == tail
			? this
			: thisWithTail( tidyTail );
	}

	@Override
	public String toString() {
		int l = len();
		String res = "[";
		if ( l > 0 ) {
			res += String.valueOf( type.toEnum( firstOrdinal ) );
		}
		if ( l > 2 ) {
			res += "," + String.valueOf( at( 1 ) );
		}
		if ( l > 1 ) {
			res += "..";
			res += String.valueOf( type.toEnum( lastOrdinal ) );
		}
		return res + "]" + List.CONCAT_OPERATOR_SYMBOL + tail.toString();
	}

	private boolean ascending() {
		return firstOrdinal <= lastOrdinal;
	}

	private int lastMinus( int dec ) {
		return ordinalMinus( lastOrdinal, dec );
	}

	private int lastPlus( int inc ) {
		return ordinalPlus( lastOrdinal, inc );
	}

	/**
	 * @return The amount of elements in this enumerated list (*not* considering the {@link #tail}s
	 *         length).
	 */
	private int len() {
		return Math.abs( lastOrdinal - firstOrdinal ) + 1;
	}

	private List<E> list( int firstOrdinal, int lastOrdinal ) {
		return new EnumList<E>( type, firstOrdinal, lastOrdinal, tail );
	}

	private List<E> list( int firstOrdianl, int lastOrdinal, List<E> tail ) {
		return new EnumList<E>( type, firstOrdianl, lastOrdinal, tail );
	}

	private List<E> empty() {
		return List.with.noElements();
	}

	private int ordinalMinus( int ordinal, int dec ) {
		return ordinalPlus( ordinal, -dec );
	}

	private int ordinalPlus( int ordinal, int inc ) {
		return ascending()
			? ordinal + inc
			: ordinal - inc;
	}

	private int firstMinus( int dec ) {
		return ordinalMinus( firstOrdinal, dec );
	}

	private int firstPlus( int inc ) {
		return ordinalPlus( firstOrdinal, inc );
	}

	private List<E> thisWithTail( List<E> tail ) {
		return new EnumList<E>( type, firstOrdinal, lastOrdinal, tail );
	}

	static final class EnumListEnumeratorFactory
			implements EnumeratorFactory {

		@Override
		public <E> Enumerator<E> enumerates( Enum<E> type ) {
			return new EnumList.EnumListEnumerator<E>( type );
		}

	}

	static final class EnumListEnumerator<E>
			extends Enumerate.StepwiseEnumerator<E> {

		EnumListEnumerator( Enum<E> type ) {
			super( type );
		}

		@Override
		protected List<E> fromTo( E first, E last, Enum<E> type ) {
			return new EnumList<E>( type, first, last );
		}

	}

}