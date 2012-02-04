package de.jbee.lang;


public interface EnumeratorFactory {

	<E> Enumerator<E> enumerate( Enum<E> type );
}
