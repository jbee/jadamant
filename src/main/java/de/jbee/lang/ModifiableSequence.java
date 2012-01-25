package de.jbee.lang;

public interface ModifiableSequence<E> {

	/*
	 * reducing modifications
	 */

	ModifiableSequence<E> deleteAt( int index );

	ModifiableSequence<E> drop( int count );

	ModifiableSequence<E> replaceAt( int index, E e );

	ModifiableSequence<E> take( int count );

	/*
	 * expanding modifications
	 */

	ModifiableSequence<E> insertAt( int index, E e );

}
