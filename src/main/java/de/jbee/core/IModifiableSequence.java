package de.jbee.core;

public interface IModifiableSequence<E> {

	IModifiableSequence<E> insert( E e, int index );

	IModifiableSequence<E> delete( int index );

	IModifiableSequence<E> takeL( int beginning );

	IModifiableSequence<E> takeR( int ending );

	IModifiableSequence<E> dropL( int beginning );

	IModifiableSequence<E> dropR( int ending );
}
