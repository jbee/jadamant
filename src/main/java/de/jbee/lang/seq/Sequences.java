package de.jbee.lang.seq;

import static de.jbee.lang.Enumerate.CHARACTERS;
import static de.jbee.lang.Enumerate.DIGITS;
import static de.jbee.lang.Enumerate.INTEGERS;
import static de.jbee.lang.Enumerate.LETTERS;
import de.jbee.lang.Arrayable;
import de.jbee.lang.Calculate;
import de.jbee.lang.Enum;
import de.jbee.lang.Enumerate;
import de.jbee.lang.Enumerator;
import de.jbee.lang.EnumeratorFactory;
import de.jbee.lang.List;
import de.jbee.lang.Lister;
import de.jbee.lang.Map;
import de.jbee.lang.PartialSequence;
import de.jbee.lang.Sequence;

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

	static final Lister LISTER = new DefaultLister();
	static final EnumeratorFactory ENUMERATOR_FACTORY = EnumList.ENUMERATOR_FACTORY;
	static final EnumeratorFactory LISTER_ENUMERATOR_FACTORY = new ListerEnumeratorFactory();

	//TODO should be private
	public static final Lister.BagLister BAG_LISTER = new UtileBagLister();
	public static final Lister.SetLister SET_LISTER = new UtileSetLister();

	private static final ProxyLister listProxy = new ProxyLister();
	public static final UtileLister list = new UtileLister( listProxy );

	private static final ProxyEnumeratorFactory enumeratorProxy = new ProxyEnumeratorFactory(
			Sequences.ENUMERATOR_FACTORY );
	public static final Range.RangeTo enumerator = Range.factory( enumeratorProxy );

	private static final ProxyEnumerator<Integer> numbersProxy = proxy( INTEGERS );
	private static final ProxyEnumerator<Integer> digitsProxy = proxy( DIGITS );
	private static final ProxyEnumerator<Character> charactersProxy = proxy( CHARACTERS );
	private static final ProxyEnumerator<Character> lettersProxy = proxy( LETTERS );

	public static final Range<Integer> numbers = utile( numbersProxy, INTEGERS );
	public static final Range<Integer> digits = utile( digitsProxy, DIGITS );
	public static final Range<Character> characters = utile( charactersProxy, CHARACTERS );
	public static final Range<Character> letters = utile( lettersProxy, LETTERS );

	private static <T> ProxyEnumerator<T> proxy( Enum<T> type ) {
		return new ProxyEnumerator<T>( enumerator.enumerate( type ) );
	}

	private static <T> Range<T> utile( Enumerator<T> e, Enum<T> type ) {
		return new Range<T>( e, type );
	}

	/**
	 * Change the list implementation used by changing the general list factory.
	 */
	static void setUp( Lister lister ) {
		listProxy.factory = lister;
	}

	@SuppressWarnings ( "unchecked" )
	static <T> void setUp( Class<T> type, Enumerator<T> enumerator ) {
		if ( type == Integer.class ) {
			setUpNumbers( (Enumerator<Integer>) enumerator );
			setUpDigits( (Enumerator<Integer>) enumerator );
		} else if ( type == Character.class ) {
			setUpCharacters( (Enumerator<Character>) enumerator );
			setUpLetters( (Enumerator<Character>) enumerator );
		}
	}

	/**
	 * Change the lister used for digit lists when created through the general {@link Enumerator}.
	 */
	public static void setUpDigits( Enumerator<Integer> digitEnumerator ) {
		digitsProxy.proxied = digitEnumerator;
	}

	/**
	 * Change the lister used for number lists when created through the general {@link Enumerator}.
	 */
	public static void setUpNumbers( Enumerator<Integer> numberEnumerator ) {
		numbersProxy.proxied = numberEnumerator;
	}

	/**
	 * Change the lister used for number lists when created through the general {@link Enumerator}.
	 */
	public static void setUpCharacters( Enumerator<Character> characterEnumerator ) {
		charactersProxy.proxied = characterEnumerator;
	}

	/**
	 * Change the lister used for number lists when created through the general {@link Enumerator}.
	 */
	public static void setUpLetters( Enumerator<Character> letterEnumerator ) {
		lettersProxy.proxied = letterEnumerator;
	}

	/**
	 * Change the factory creating new listers for custom {@link Enum}s.
	 */
	public static void setUp( EnumeratorFactory factory ) {
		enumeratorProxy.proxied = factory;
	}

	public static List<Integer> noInts() {
		return list.noElements();
	}

	public static List<Character> noChars() {
		return list.noElements();
	}

	public static List<Float> noFloats() {
		return list.noElements();
	}

	public static List<Double> noDoubles() {
		return list.noElements();
	}

	public static List<Long> noLongs() {
		return list.noElements();
	}

	public static List<String> noStrings() {
		return list.noElements();
	}

	public static List<Boolean> noBools() {
		return list.noElements();
	}

	public static <E> List<E> list() {
		return list.noElements();
	}

	public static <E> List<E> list( E e ) {
		return list.element( e );
	}

	public static <E> List<E> list( E e1, E e2 ) {
		return list.element( e2 ).prepand( e1 );
	}

	public static <E> List<E> list( E e1, E e2, E e3 ) {
		return list.element( e3 ).prepand( e2 ).prepand( e1 );
	}

	public static <E> List<E> list( E e1, E e2, E e3, E e4 ) {
		return list.element( e4 ).prepand( e3 ).prepand( e2 ).prepand( e1 );
	}

	public static <E> List<E> list( E e1, E e2, E e3, E e4, E e5 ) {
		return list.element( e5 ).prepand( e4 ).prepand( e3 ).prepand( e2 ).prepand( e1 );
	}

	public static <E> List<E> list( E e1, E e2, E e3, E e4, E e5, E e6 ) {
		return list.element( e6 ).prepand( e5 ).prepand( e4 ).prepand( e3 ).prepand( e2 ).prepand(
				e1 );
	}

	public static <E> List<E> list( E e1, E e2, E e3, E e4, E e5, E e6, E e7 ) {
		return list.element( e7 ).prepand( e6 ).prepand( e5 ).prepand( e4 ).prepand( e3 ).prepand(
				e2 ).prepand( e1 );
	}

	public static <E> List<E> list( E e1, E e2, E e3, E e4, E e5, E e6, E e7, E e8 ) {
		return list.element( e8 ).prepand( e7 ).prepand( e6 ).prepand( e5 ).prepand( e4 ).prepand(
				e3 ).prepand( e2 ).prepand( e1 );
	}

	public static <E> List<E> list( E e1, E e2, E e3, E e4, E e5, E e6, E e7, E e8, E e9 ) {
		return list.element( e9 ).prepand( e8 ).prepand( e7 ).prepand( e6 ).prepand( e5 ).prepand(
				e4 ).prepand( e3 ).prepand( e2 ).prepand( e1 );
	}

	public static int depth( PartialSequence<?> seq ) {
		return seq.isEmpty()
			? 0
			: 1 + depth( seq.subsequent() );
	}

	static final class ProxyEnumeratorFactory
			implements EnumeratorFactory {

		EnumeratorFactory proxied;

		ProxyEnumeratorFactory( EnumeratorFactory proxied ) {
			super();
			this.proxied = proxied;
		}

		@Override
		public <E> Enumerator<E> enumerate( Enum<E> type ) {
			return proxied.enumerate( type );
		}
	}

	static final class ProxyEnumerator<E>
			implements Enumerator<E> {

		Enumerator<E> proxied;

		ProxyEnumerator( Enumerator<E> proxied ) {
			super();
			this.proxied = proxied;
		}

		@Override
		public List<E> stepwiseFromTo( E first, E last, int increment ) {
			return proxied.stepwiseFromTo( first, last, increment );
		}

	}

	static final class ProxyLister
			implements Lister {

		Lister factory = Sequences.LISTER;

		ProxyLister() {
			// hide for singleton
		}

		@Override
		public <E> List<E> element( E e ) {
			return factory.element( e );
		}

		@Override
		public <E> List<E> elements( E... elems ) {
			return factory.elements( elems );
		}

		@Override
		public <E> List<E> elements( Sequence<E> elems ) {
			return factory.elements( elems );
		}

		@Override
		public <E> List<E> noElements() {
			return factory.noElements();
		}

	}

	/**
	 * A {@link Lister} uses the default implementations {@link EmptyList}, {@link ElementaryList},
	 * {@link EVolutionList} and {@link EnumList}.
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
			if ( e instanceof Integer ) {
				return integerList( e );
			}
			if ( e.getClass().isEnum() ) {
				return enumList( e );
			}
			return ElementaryList.element( e, this.<E> noElements() );
		}

		@SuppressWarnings ( "unchecked" )
		private <E> List<E> enumList( E e ) {
			return (List<E>) EnumList.enumElement( (java.lang.Enum<?>) e );
		}

		@SuppressWarnings ( "unchecked" )
		private <E> List<E> integerList( E e ) {
			Integer i = (Integer) e;
			return (List<E>) new EnumList<Integer>( Enumerate.INTEGERS, i, i,
					this.<Integer> noElements() );
		}

		@Override
		public <E> List<E> elements( E... elems ) {
			final int size = elems.length;
			if ( size == 0 ) {
				return noElements();
			}
			if ( elems.getClass() == Object[].class && ( elems.length % 2 ) == 0 ) {
				return EVolutionList.dominant( elems.length, elems );
			}
			Object[] stack = new Object[Calculate.nextHighestPowerOf2( size )];
			System.arraycopy( elems, 0, stack, stack.length - size, size );
			return EVolutionList.dominant( size, stack );
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
			Object[] stack = new Object[Calculate.nextHighestPowerOf2( size )];
			if ( elems instanceof Arrayable ) {
				( (Arrayable) elems ).fill( stack.length - size, stack, 0, size );
			} else {
				int index = stack.length - size;
				for ( int i = 0; i < size; i++ ) {
					stack[index++] = elems.at( i );
				}
			}
			return EVolutionList.dominant( size, stack );
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
				res = EVolutionList.dominant( size, stack, res );
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
		public <E> Enumerator<E> enumerate( Enum<E> type ) {
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

	public static <V> Map.Entry<V> entry( Map.Key key, V value ) {
		return new Sequences.Entry<V>( key, value );
	}

	public static <V> Map.Key key( CharSequence key ) {
		return key instanceof Map.Key
			? (Map.Key) key
			: new Sequences.Key( key.toString() );
	}

	public static <V> Map.Key keyFirstStartsWith( CharSequence key ) {
		return new Sequences.Key( key.toString() + Character.MIN_VALUE );
	}

	public static <V> Map.Key keyLastStartsWith( CharSequence key ) {
		return new Sequences.Key( key.toString() + Map.Key.PREFIX_TERMINATOR );
	}

	private static final class Key
			implements Map.Key {

		private final String pattern;

		Key( String pattern ) {
			super();
			this.pattern = pattern;
		}

		@Override
		public String path() {
			return pattern;
		}

		@Override
		public String toString() {
			return pattern;
		}

	}

}