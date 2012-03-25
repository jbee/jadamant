package de.jbee.lang;

public interface Accumulator<E, T> {

	void accumulate( E element );

	T result();
}
