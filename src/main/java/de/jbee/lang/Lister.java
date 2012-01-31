package de.jbee.lang;

public interface Lister {

	<E> List<E> noElements();

	<E> List<E> element( E e );

	<E> List<E> elements( E... elems );

	<E> List<E> elements( Sequence<E> elems );

	interface BagLister
			extends Lister {

		@Override
		<E> Bag<E> noElements();

		/**
		 * @return a empty bag/set using the <code>order</code> given.
		 */
		<E> Bag<E> noElements( Ord<Object> order );

		@Override
		<E> Bag<E> element( E e );

		@Override
		<E> Bag<E> elements( E... elems );

		/**
		 * @return a bag or set created from the <code>elems</code> given. In case they are (an
		 *         instance of) {@link Ordered} the order is kept. Otherwise the lister will apply a
		 *         default order chosen by the lister itself.
		 */
		@Override
		<E> Bag<E> elements( Sequence<E> elems );

		/**
		 * @return a bag or set created from the <code>elems</code> given. Those are re-sorted to be
		 *         in the <code>order</code> given (if necessary).
		 */
		<E> Bag<E> elements( Ord<Object> order, List<E> elems );
	}

	interface SetLister
			extends BagLister {

		@Override
		<E> Set<E> noElements();

		@Override
		<E> Set<E> noElements( Ord<Object> order );

		@Override
		<E> Set<E> element( E e );

		@Override
		<E> Set<E> elements( E... elems );

		@Override
		<E> Set<E> elements( Sequence<E> elems );

		@Override
		<E> Set<E> elements( Ord<Object> order, List<E> elems );
	}
}
