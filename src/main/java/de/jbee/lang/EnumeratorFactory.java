package de.jbee.lang;


public interface EnumeratorFactory {

	<E> Enumerator<E> enumerates( Enum<E> type );
}
