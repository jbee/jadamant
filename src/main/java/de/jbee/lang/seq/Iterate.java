package de.jbee.lang.seq;

import de.jbee.lang.IndexAccess;
import de.jbee.lang.Sequence;

public class Iterate {

	//OPEN move to lang package ? 

	public static final Iterate instance = new Iterate();

	public <E> Iterable<E> forwards( Sequence<E> seq ) {
		return IndexAccess.iterable( seq );
	}

	public <E> Iterable<E> backwards( Sequence<E> seq ) {
		return IndexAccess.reverseIterable( seq );
	}

}
