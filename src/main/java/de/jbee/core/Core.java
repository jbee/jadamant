package de.jbee.core;

import de.jbee.core.list.CoreList;
import de.jbee.core.list.CoreListTransition;
import de.jbee.core.list.List;
import de.jbee.core.list.ListFactory;
import de.jbee.core.list.ListTransition;

/**
 * Provides the basis utilities.
 * 
 * <h3>Lists</h3>
 * <p>
 * The {@link ListFactory} creates {@link List} along with a bunch of static factory methods
 * {@link #_()}, {@link #_(Object)}, ...
 * </p>
 * 
 * @author Jan Bernitt (jan.bernitt@gmx.de)
 * 
 */
public final class Core {

	private Core() {
		throw new UnsupportedOperationException( "util" );
	}

	public static final ListFactory list = new ProxyListFactory();

	static void setUp( ListFactory factory ) {
		( (ProxyListFactory) list ).factory = factory;
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

	public static ListTransition $( ListTransition fst, ListTransition snd ) {
		return CoreListTransition.concat( fst, snd );
	}

	static final class ProxyListFactory
			implements ListFactory {

		ListFactory factory = CoreList.FACTORY;

		ProxyListFactory() {
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
