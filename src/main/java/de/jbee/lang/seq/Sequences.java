package de.jbee.lang.seq;

import de.jbee.lang.Arrayable;
import de.jbee.lang.Enum;
import de.jbee.lang.Enumerate;
import de.jbee.lang.Enumerator;
import de.jbee.lang.EnumeratorFactory;
import de.jbee.lang.Lang;
import de.jbee.lang.List;
import de.jbee.lang.Lister;
import de.jbee.lang.Map;
import de.jbee.lang.Sequence;
import de.jbee.lang.seq.EvolutionList.DominantEvolutionList;

/**
 * Acts as a API of this package. It 'publishes' the implementation (they are package local) though
 * their interfaces.
 * <p>
 * This is *not* a {@link Sequence} or {@link List} util-class. In case you are looking for
 * utilities have a look to the {@link List} interface instead.
 * </p>
 * 
 * @author Jan Bernitt (jan.bernitt@gmx.de)
 */
public final class Sequences {

	public static final Lister LISTER = new DefaultLister();
	public static final Lister.BagLister BAG_LISTER = new UtileBagLister();
	public static final Lister.SetLister SET_LISTER = new UtileSetLister();
	public static final EnumeratorFactory ENUMERATOR_FACTORY = EnumList.ENUMERATOR_FACTORY;
	public static final EnumeratorFactory LISTER_ENUMERATOR_FACTORY = new ListerEnumeratorFactory();

	/**
	 * A {@link Lister} uses the default implementations {@link EmptyList}, {@link ElementaryList},
	 * {@link EvolutionList} and {@link EnumList}.
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
			return ElementaryList.element( e );
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
			if ( elems.getClass() == Object[].class && ( elems.length % 2 ) == 0 ) {
				return EvolutionList.dominant( elems.length, elems );
			}
			Object[] stack = new Object[Lang.nextHighestPowerOf2( size )];
			System.arraycopy( elems, 0, stack, stack.length - size, size );
			return EvolutionList.dominant( size, stack );
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
			return EvolutionList.dominant( size, stack );
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
				res = new DominantEvolutionList<E>( size, stack, res );
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

	private static final class Entry<V>
			implements Map.Entry<V> {

		final Map.Key key;
		final V value;

		Entry( Map.Key key, V value ) {
			super();
			this.key = key;
			this.value = value;
		}

		@Override
		public Map.Key key() {
			return key;
		}

		@Override
		public V value() {
			return value;
		}

		@Override
		public String toString() {
			return key + "=>" + value;
		}

	}

	static <V> Map.Entry<V> entry( Map.Key key, V value ) {
		return new Sequences.Entry<V>( key, value );
	}

	static <V> Map.Key key( CharSequence key ) {
		return new Sequences.Key( key.toString() );
	}

	private static final class Key
			implements Map.Key {

		private final String pattern;

		Key( String pattern ) {
			super();
			this.pattern = pattern;
		}

		@Override
		public String pattern() {
			return pattern;
		}

	}

}