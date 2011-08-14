package de.jbee.draft;

import de.jbee.lang.List;

public interface ListPredicate<E> {

	boolean eval( List<E> l );

	//TODO boolean any(Eq<? super E> eq, E e);
}
