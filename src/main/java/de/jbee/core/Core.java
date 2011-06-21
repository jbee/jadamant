package de.jbee.core;

import static de.jbee.core.type.Enumerate.INTEGERS;
import de.jbee.core.list.CoreList;
import de.jbee.core.list.CoreListTransition;
import de.jbee.core.list.EnumLister;
import de.jbee.core.list.EnumListerFactory;
import de.jbee.core.list.List;
import de.jbee.core.list.ListTransition;
import de.jbee.core.list.Lister;
import de.jbee.core.list.RichLister;
import de.jbee.core.type.Enum;

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
	public static final EnumListerFactory lister = new ProxyEnumListerFactory(
			CoreList.LISTER_FACTORY );
	private static final ProxyEnumLister<Integer> numbersProxy = new ProxyEnumLister<Integer>(
			lister.enumerates( INTEGERS ) );
	public static final RichLister<Integer> numbers = new RichLister<Integer>( numbersProxy,
			INTEGERS );

	/**
	 * Change the list implementation used by changing the general list factory.
	 */
	static void setUp( Lister lister ) {
		( (ProxyLister) list ).factory = lister;
	}

	/**
	 * Change the lister used for number lists when created through the general {@link EnumLister}.
	 */
	static void setUp( EnumLister<Integer> numberLister ) {
		numbersProxy.proxied = numberLister;
	}

	/**
	 * Change the factory creating new listers for custom {@link Enum}s.
	 */
	static void setUp( EnumListerFactory factory ) {
		( (ProxyEnumListerFactory) lister ).proxied = factory;
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
				e1 );
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
		return CoreListTransition.concat( fst, snd );
	}

	static final class ProxyEnumListerFactory
			implements EnumListerFactory {

		EnumListerFactory proxied;

		ProxyEnumListerFactory( EnumListerFactory proxied ) {
			super();
			this.proxied = proxied;
		}

		@Override
		public <E> RichLister<E> enumerates( Enum<E> type ) {
			return proxied.enumerates( type );
		}
	}

	static final class ProxyEnumLister<E>
			implements EnumLister<E> {

		EnumLister<E> proxied;

		ProxyEnumLister( EnumLister<E> proxied ) {
			super();
			this.proxied = proxied;
		}

		@Override
		public List<E> stepwiseFromTo( E start, E end, int increment ) {
			return proxied.stepwiseFromTo( start, end, increment );
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
		public <E> List<E> elements( Iterable<E> elems ) {
			return factory.elements( elems );
		}

		@Override
		public <E> List<E> noElements() {
			return factory.noElements();
		}

	}
}
