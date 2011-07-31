package de.jbee.core.list;

public interface ListPredicate<E> {

	boolean eval( List<E> l );

	//TODO boolean any(Eq<? super E> eq, E e);
}
