package de.jbee.lang;

public interface ModifiableSequence<E>
		extends ReducibleSequence<E> {

	/*
	 * Covariant return type overrides for inherited methods from ReducibleSequence
	 */

	ModifiableSequence<E> drop( int count );

	ModifiableSequence<E> take( int count );

	/*
	 * reducing modifications
	 */

	ModifiableSequence<E> deleteAt( int index );

	ModifiableSequence<E> replaceAt( int index, E e );

	/*
	 * expanding modifications
	 */

	ModifiableSequence<E> insertAt( int index, E e );

}
