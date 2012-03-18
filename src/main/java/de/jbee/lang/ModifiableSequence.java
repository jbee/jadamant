package de.jbee.lang;

/**
 * A {@link Sequence} that can be modified.
 * 
 * @author Jan Bernitt (jan.bernitt@gmx.de)
 */
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
