package de.jbee.core;

public interface ModifiableSequence<E> {

	ModifiableSequence<E> deleteAt( int index );

	ModifiableSequence<E> dropL( int beginning );

	ModifiableSequence<E> dropR( int ending );

	ModifiableSequence<E> insertAt( int index, E e );

	ModifiableSequence<E> replaceAt( int index, E e );

	ModifiableSequence<E> takeL( int beginning );

	ModifiableSequence<E> takeR( int ending );
}
