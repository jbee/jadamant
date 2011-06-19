package de.jbee.core.type;

public interface Eq<T> {

	//TODO instead of boolean result use an enum called Equality /Equaling ? 
	boolean eq( T one, T other );
}
