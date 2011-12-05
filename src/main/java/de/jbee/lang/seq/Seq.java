package de.jbee.lang.seq;

import de.jbee.lang.Arrayable;
import de.jbee.lang.Enum;
import de.jbee.lang.Enumerate;
import de.jbee.lang.Enumerator;
import de.jbee.lang.EnumeratorFactory;
import de.jbee.lang.Lang;
import de.jbee.lang.List;
import de.jbee.lang.Lister;
import de.jbee.lang.Sequence;
import de.jbee.lang.seq.HarpList.TidyHarpList;

public final class Seq {

	public static final Lister LISTER = new DefaultLister();
	public static final EnumeratorFactory ENUMERATOR_FACTORY = EnumList.ENUMERATOR_FACTORY;
	public static final EnumeratorFactory LISTER_ENUMERATOR_FACTORY = new ListerEnumeratorFactory();

	/**
	 * A {@link Lister} uses the default implementations {@link EmptyList}, {@link ElementList},
	 * {@link HarpList} and {@link EnumList}.
	 * 
	 * @author Jan Bernitt (jan.bernitt@gmx.de)
	 */
	private static final class DefaultLister
			implements Lister {

		DefaultLister() {
			// make visible
		}

		@Override
		public <E> List<E> element( E e ) {
			if ( e instanceof java.lang.Enum<?> ) {
				return elementEnum( e );
			}
			return ElementList.with( e );
		}

		@SuppressWarnings ( "unchecked" )
		private <E> List<E> elementEnum( E e ) {
			return (List<E>) EnumList.withElement( (java.lang.Enum<?>) e );
		}

		@Override
		public <E> List<E> elements( E... elems ) {
			final int size = elems.length;
			if ( size == 0 ) {
				return noElements();
			}
			Object[] stack = new Object[Lang.nextHighestPowerOf2( size )];
			System.arraycopy( elems, 0, stack, stack.length - size, size );
			return HarpList.tidy( size, stack, EmptyList.<E> instance() );
		}

		@Override
		public <E> List<E> elements( Sequence<E> elems ) {
			if ( elems.isEmpty() ) {
				return noElements();
			}
			final int size = elems.length();
			if ( size == 1 ) {
				return element( elems.at( 0 ) );
			}
			Object[] stack = new Object[Lang.nextHighestPowerOf2( size )];
			if ( elems instanceof Arrayable ) {
				( (Arrayable) elems ).fill( stack.length - size, stack, 0, size );
			} else {
				int index = stack.length - size;
				for ( int i = 0; i < size; i++ ) {
					stack[index++] = elems.at( i );
				}
			}
			return HarpList.tidy( size, stack, EmptyList.<E> instance() );
		}

		@Override
		public <E> List<E> noElements() {
			return EmptyList.instance();
		}

	}

	/**
	 * A {@link Enumerator} uses the {@link Lister} to build the enumerated {@link List}s.
	 * 
	 * @author Jan Bernitt (jan.bernitt@gmx.de)
	 */
	private static final class ListerEnumerator<E>
			extends Enumerate.StepwiseEnumerator<E> {

		ListerEnumerator( Enum<E> type ) {
			super( type );
		}

		@Override
		protected List<E> fromTo( E first, E last, Enum<E> type ) {
			int fo = type.toOrdinal( first );
			int lo = type.toOrdinal( last );
			if ( fo == lo ) { // length 1
				return List.with.element( first );
			}
			int l = Math.abs( fo - lo ) + 1;
			if ( l == 2 ) {
				return List.with.element( last ).prepand( first );
			}
			int capacity = 2;
			List<E> res = List.with.noElements();
			E cur = last;
			final boolean asc = fo < lo;
			int size = 0;
			while ( size < l ) { // length will be > 2
				Object[] stack = new Object[capacity];
				int min = size + capacity < l
					? 0
					: capacity - ( l - size );
				for ( int i = capacity - 1; i >= min; i-- ) {
					stack[i] = cur;
					size++;
					if ( size < l ) {
						cur = asc
							? type.pred( cur )
							: type.succ( cur );
					}
				}
				res = new TidyHarpList<E>( size, stack, res );
				capacity += capacity;
			}
			return res;
		}

	}

	private static final class ListerEnumeratorFactory
			implements EnumeratorFactory {

		ListerEnumeratorFactory() {
			// make visible
		}

		@Override
		public <E> Enumerator<E> enumerates( Enum<E> type ) {
			return new ListerEnumerator<E>( type );
		}

	}

}