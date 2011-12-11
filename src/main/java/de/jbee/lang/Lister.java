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

		<E> Bag<E> noElements( Ord<Object> order );

		@Override
		<E> Bag<E> element( E e );

		@Override
		<E> Bag<E> elements( E... elems );

		@Override
		<E> Bag<E> elements( Sequence<E> elems );

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
