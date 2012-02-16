package de.jbee.lang;

/**
 * {@link ListAlteration}s are manipulations on a {@link List} that doesn't change the type of
 * elements but their composition in the resulting list.
 * 
 * A {@linkplain ListAlteration} doesn't take any additional state. It has no internal state that
 * can change whereby same input always causes same output. Nevertheless they might have a fix
 * internal state that controls their algorithm.
 * 
 * @author Jan Bernitt (jan.bernitt@gmx.de)
 */
public interface ListAlteration {

	<E> List<E> from( List<E> list );

	/**
	 * Any {@link ListAlteration} results in a {@link Bag} may expose itself as a
	 * {@linkplain BagAlteration} to hide its implementation type but guarantee the more precise
	 * {@linkplain Bag} result type.
	 * 
	 * @author Jan Bernitt (jan.bernitt@gmx.de)
	 */
	interface BagAlteration
			extends ListAlteration {

		@Override
		<E> Bag<E> from( List<E> list );
	}

	/**
	 * Any {@link ListAlteration} results in a {@link Set} may expose itself as a
	 * {@linkplain SetAlteration} to hide its implementation type but guarantee the more precise
	 * {@linkplain Set} result type.
	 * 
	 * @author Jan Bernitt (jan.bernitt@gmx.de)
	 */
	interface SetAlteration
			extends BagAlteration {

		@Override
		<E> Set<E> from( List<E> list );
	}

}
