package de.jbee.lang;

public interface Enum<T>
		extends Bounded<T>, Show<T> {

	T succ( T value );

	T pred( T value );

	T toEnum( int ord );

	int toOrdinal( T value );

	Class<T> elementType();

}
