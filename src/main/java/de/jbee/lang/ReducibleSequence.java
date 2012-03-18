package de.jbee.lang;

/**
 * A {@link Sequence} whose elements can be narrowed using {@link #take(int)} or {@link #drop(int)}.
 * 
 * Narrowing can be implemented by providing 'views' to the unchanged sequence below.
 * 
 * @author Jan Bernitt (jan.bernitt@gmx.de)
 * 
 */
public interface ReducibleSequence<E>
		extends Sequence<E> {

	ReducibleSequence<E> drop( int count );

	ReducibleSequence<E> take( int count );

}
