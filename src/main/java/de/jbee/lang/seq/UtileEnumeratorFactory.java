package de.jbee.lang.seq;

import de.jbee.lang.Enum;
import de.jbee.lang.Enumerate;
import de.jbee.lang.EnumeratorFactory;

public final class UtileEnumeratorFactory
		implements EnumeratorFactory {

	private final EnumeratorFactory factory;

	public UtileEnumeratorFactory( EnumeratorFactory factory ) {
		super();
		this.factory = factory;
	}

	@Override
	public <E> UtileEnumerator<E> enumerates( Enum<E> type ) {
		return new UtileEnumerator<E>( factory.enumerates( type ), type );
	}

	public <E extends java.lang.Enum<E>> UtileEnumerator<E> enumerates( Class<E> type ) {
		return enumerates( Enumerate.type( type ) );
	}

}
