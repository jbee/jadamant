package de.jbee.lang;

import static de.jbee.lang.Enumerate.CHARACTERS;
import static de.jbee.lang.Enumerate.DIGITS;
import static de.jbee.lang.Enumerate.INTEGERS;
import static de.jbee.lang.Enumerate.LETTERS;
import de.jbee.lang.seq.InitList;
import de.jbee.lang.seq.UtileEnumerator;
import de.jbee.lang.seq.UtileEnumeratorFactory;
import de.jbee.lang.seq.UtileLister;

/**
 * Provides the basis utilities.
 * 
 * <h3>Lists</h3>
 * <p>
 * The {@link Lister} creates {@link List} along with a bunch of static factory methods
 * {@link #list()}, {@link #list(Object)}, ...
 * </p>
 * 
 * @author Jan Bernitt (jan.bernitt@gmx.de)
 * 
 */
public final class Lang {

	private Lang() {
		throw new UnsupportedOperationException( "util" );
	}

	private static final ProxyLister listProxy = new ProxyLister();
	public static final UtileLister list = new UtileLister( listProxy );

	private static final ProxyEnumeratorFactory enumeratorProxy = new ProxyEnumeratorFactory(
			InitList.ENUMERATOR_FACTORY );
	public static final UtileEnumeratorFactory enumerator = new UtileEnumeratorFactory(
			enumeratorProxy );

	private static final ProxyEnumerator<Integer> numbersProxy = proxy( INTEGERS );
	private static final ProxyEnumerator<Integer> digitsProxy = proxy( DIGITS );
	private static final ProxyEnumerator<Character> charactersProxy = proxy( CHARACTERS );
	private static final ProxyEnumerator<Character> lettersProxy = proxy( LETTERS );

	public static final UtileEnumerator<Integer> numbers = utile( numbersProxy, INTEGERS );
	public static final UtileEnumerator<Integer> digits = utile( digitsProxy, DIGITS );
	public static final UtileEnumerator<Character> characters = utile( charactersProxy, CHARACTERS );
	public static final UtileEnumerator<Character> letters = utile( lettersProxy, LETTERS );

	private static <T> ProxyEnumerator<T> proxy( Enum<T> type ) {
		return new ProxyEnumerator<T>( enumerator.enumerates( type ) );
	}

	private static <T> UtileEnumerator<T> utile( Enumerator<T> e, Enum<T> type ) {
		return new UtileEnumerator<T>( e, type );
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

	//OPEN move to some kind of math util ?
	public static int nextHighestPowerOf2( int number ) {
		number--;
		number |= number >> 1;
		number |= number >> 2;
		number |= number >> 4;
		number |= number >> 8;
		number |= number >> 16;
		number++;
		return number;
	}

	static final class ProxyEnumeratorFactory
			implements EnumeratorFactory {

		EnumeratorFactory proxied;

		ProxyEnumeratorFactory( EnumeratorFactory proxied ) {
			super();
			this.proxied = proxied;
		}

		@Override
		public <E> Enumerator<E> enumerates( Enum<E> type ) {
			return proxied.enumerates( type );
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

		Lister factory = InitList.LISTER;

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
}
