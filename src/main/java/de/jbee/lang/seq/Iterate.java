package de.jbee.lang.seq;

import de.jbee.lang.IndexAccess;
import de.jbee.lang.Sequence;

public class Iterate {

	public static final Iterate instance = new Iterate();

	public <E> Iterable<E> forward( Sequence<E> seq ) {
		return IndexAccess.iterable( seq );
	}
}
