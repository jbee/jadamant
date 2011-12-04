package de.jbee.lang;

/**
 * {@link ListTransition}s are manipulations on a {@link List} that doesn't change the type of
 * elements but their composition in the resulting list.
 * 
 * A {@linkplain ListTransition} doesn't take any additional state. It has no internal state that
 * can change whereby same input always causes same output. Nevertheless they might have a fix
 * internal state that controls their algorithm.
 * 
 * @author Jan Bernitt (jan.bernitt@gmx.de)
 */
public interface ListTransition {

	<E> List<E> from( List<E> list );

	/**
	 * Any {@link ListTransition} results in a {@link Bag} may expose itself as a
	 * {@linkplain BagTransition} to hide its implementation type but guarantee the more precise
	 * {@linkplain Bag} result type.
	 * 
	 * @author Jan Bernitt (jan.bernitt@gmx.de)
	 */
	interface BagTransition
			extends ListTransition {

		@Override
		<E> Bag<E> from( List<E> list );
	}

	/**
	 * Any {@link ListTransition} results in a {@link Set} may expose itself as a
	 * {@linkplain SetTransition} to hide its implementation type but guarantee the more precise
	 * {@linkplain Set} result type.
	 * 
	 * @author Jan Bernitt (jan.bernitt@gmx.de)
	 */
	interface SetTransition
			extends BagTransition {

		@Override
		<E> Set<E> from( List<E> list );
	}

}
