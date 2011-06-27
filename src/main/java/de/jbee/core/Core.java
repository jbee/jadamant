package de.jbee.core;

import static de.jbee.core.type.Enumerate.INTEGERS;
import de.jbee.core.list.CoreList;
import de.jbee.core.list.CoreListTransition;
import de.jbee.core.list.Enumerator;
import de.jbee.core.list.EnumeratorFactory;
import de.jbee.core.list.List;
import de.jbee.core.list.ListTransition;
import de.jbee.core.list.Lister;
import de.jbee.core.list.UtileEnumerator;
import de.jbee.core.list.UtileEnumeratorFactory;
import de.jbee.core.type.Enum;
import de.jbee.util.ICluster;

/**
 * Provides the basis utilities.
 * 
 * <h3>Lists</h3>
 * <p>
 * The {@link Lister} creates {@link List} along with a bunch of static factory methods {@link #_()}, {@link #_(Object)}, ...
 * </p>
 * 
 * @author Jan Bernitt (jan.bernitt@gmx.de)
 * 
 */
public final class Core {

	private Core() {
		throw new UnsupportedOperationException( "util" );
	}

	public static final Lister list = new ProxyLister();

	private static final ProxyEnumeratorFactory enumeratorProxy = new ProxyEnumeratorFactory(
			CoreList.LISTER_FACTORY );
	public static final UtileEnumeratorFactory enumerator = new UtileEnumeratorFactory(
			enumeratorProxy );
	private static final ProxyEnumerator<Integer> numbersProxy = new ProxyEnumerator<Integer>(
			enumerator.enumerates( INTEGERS ) );
	public static final UtileEnumerator<Integer> numbers = new UtileEnumerator<Integer>(
			numbersProxy, INTEGERS );

	/**
	 * Change the list implementation used by changing the general list factory.
	 */
	static void setUp( Lister lister ) {
		( (ProxyLister) list ).factory = lister;
	}

	/**
	 * Change the lister used for number lists when created through the general {@link Enumerator}.
	 */
	static void setUp( Enumerator<Integer> numberLister ) {
		numbersProxy.proxied = numberLister;
	}

	/**
	 * Change the factory creating new listers for custom {@link Enum}s.
	 */
	static void setUp( EnumeratorFactory factory ) {
		enumeratorProxy.proxied = factory;
	}

	public static List<Integer> I() {
		return list.noElements();
	}

	public static List<Character> C() {
		return list.noElements();
	}

	public static List<Float> F() {
		return list.noElements();
	}

	public static List<Double> D() {
		return list.noElements();
	}

	public static List<Long> L() {
		return list.noElements();
	}

	public static List<String> S() {
		return list.noElements();
	}

	public static List<Boolean> B() {
		return list.noElements();
	}

	public static <E> List<E> _() {
		return list.noElements();
	}

	public static <E> List<E> _( E e ) {
		return list.element( e );
	}

	public static <E> List<E> _( E e1, E e2 ) {
		return list.element( e2 ).prepand( e1 );
	}

	public static <E> List<E> _( E e1, E e2, E e3 ) {
		return list.element( e3 ).prepand( e2 ).append( e1 );
	}

	public static <E> List<E> _( E e1, E e2, E e3, E e4 ) {
		return list.element( e4 ).prepand( e3 ).prepand( e2 ).append( e1 );
	}

	public static <E> List<E> _( E e1, E e2, E e3, E e4, E e5 ) {
		return list.element( e5 ).prepand( e4 ).prepand( e3 ).prepand( e2 ).append( e1 );
	}

	public static <E> List<E> _( E e1, E e2, E e3, E e4, E e5, E e6 ) {
		return list.element( e6 ).prepand( e5 ).prepand( e4 ).prepand( e3 ).prepand( e2 ).append(
				e1 ); //FIXME seams not to work correct for 11, 19, 11, 22, 11, 12 
	}

	public static <E> List<E> _( E e1, E e2, E e3, E e4, E e5, E e6, E e7 ) {
		return list.element( e7 ).prepand( e6 ).prepand( e5 ).prepand( e4 ).prepand( e3 ).prepand(
				e2 ).append( e1 );
	}

	public static <E> List<E> _( E e1, E e2, E e3, E e4, E e5, E e6, E e7, E e8 ) {
		return list.element( e8 ).prepand( e7 ).prepand( e6 ).prepand( e5 ).prepand( e4 ).prepand(
				e3 ).prepand( e2 ).append( e1 );
	}

	public static <E> List<E> _( E e1, E e2, E e3, E e4, E e5, E e6, E e7, E e8, E e9 ) {
		return list.element( e9 ).prepand( e8 ).prepand( e7 ).prepand( e6 ).prepand( e5 ).prepand(
				e4 ).prepand( e3 ).prepand( e2 ).append( e1 );
	}

	public static ListTransition ſ( ListTransition fst, ListTransition snd ) {
		return CoreListTransition.consec( fst, snd );
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

		Lister factory = CoreList.LISTER;

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
		public <E> List<E> elements( ICluster<E> elems ) {
			return factory.elements( elems );
		}

		@Override
		public <E> List<E> noElements() {
			return factory.noElements();
		}

	}
}
