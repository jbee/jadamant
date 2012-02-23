package de.jbee.lang;

public interface ReducibleSequence<E>
		extends Sequence<E> {

	ReducibleSequence<E> drop( int count );

	ReducibleSequence<E> take( int count );

}
