package de.jbee.core.list;

import de.jbee.core.type.Enum;
import de.jbee.core.type.Enumerate;

public final class UtileEnumeratorFactory
		implements EnumeratorFactory {

	private final EnumeratorFactory factory;

	public UtileEnumeratorFactory( EnumeratorFactory factory ) {
		super();
		this.factory = factory;
	}

	@Override
	public <E> UtileEnumerator<E> enumerate( Enum<E> type ) {
		return new UtileEnumerator<E>( factory.enumerate( type ), type );
	}

	public <E extends java.lang.Enum<E>> UtileEnumerator<E> enumerate( Class<E> type ) {
		return enumerate( Enumerate.type( type ) );
	}

}
