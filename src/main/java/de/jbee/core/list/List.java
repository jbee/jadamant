package de.jbee.core.list;

import de.jbee.core.Appendable;
import de.jbee.core.Arrayable;
import de.jbee.core.Core;
import de.jbee.core.IndexAccessible;
import de.jbee.core.ModifiableSequence;
import de.jbee.core.Prepandable;
import de.jbee.util.ICluster;

/**
 * Lists are immutable and use a persistent data-structure whereby they are thread-save by
 * definition. There is no need to offer another concurrent collection API. Just use {@link List}s
 * for everything everywhere.
 * <p>
 * This interface is intentionally not a minimal class interface. It provides several functions
 * whose results could be achieved by combine several other functions. This decision was made since
 * the list structure is a core functionality that could be used as easy, compact and readable as
 * possible.
 * </p>
 * 
 * @author Jan Bernitt (jan.bernitt@gmx.de)
 * 
 */
public interface List<E>
		extends ICluster<E>, IndexAccessible<E>, Prepandable<E>, Appendable<E>,
		ModifiableSequence<E>, Arrayable {

	/**
	 * The operator to use in a {@link Object#toString()} when visualizing concatenation of lists.
	 * Example:
	 * 
	 * <pre>
	 * [1,2]+[36,9]
	 * </pre>
	 * 
	 * is a list containing the sequence 1,2,36,9 in 2 sublists chained together.
	 */
	char CONCAT_OPERATOR_SYMBOL = '+';

	UtileLister with = Core.list;
	UtileEnumeratorFactory does = Core.enumerator;
	UtileListTransition which = UtileListTransition.instance;

	UtileEnumerator<Integer> numbers = Core.numbers;
	UtileEnumerator<Character> letters = Core.letters;
	UtileEnumerator<Character> characters = Core.characters;

	//TODO a util for string handling strings as list of characters

	ListTransition reverse = ListTransition.reverse;

	ListTransition tail = ListTransition.tail;

	/*
	 * override inherited methods with extending return types
	 */

	/**
	 * Expect this to be the fastest way to add elements. Almost O(1) in every case.
	 */
	@Override
	List<E> prepand( E e );

	/**
	 * Expect this to be much slower than {@link #prepand(Object)}. Only use this when
	 * {@link CoreList#reverse(List)} a list so a <code>append</code> is in fact a
	 * <code>prepand</code>.
	 */
	@Override
	List<E> append( E e );

	/*
	 * override inherited methods with extending return types
	 * 
	 * FROM: ModifiableSequence
	 */

	@Override
	List<E> insertAt( int index, E e );

	@Override
	List<E> deleteAt( int index );

	@Override
	List<E> replaceAt( int index, E e );

	@Override
	List<E> take( int count );

	@Override
	List<E> drop( int count );

	/*
	 * additional methods
	 */

	List<E> tidyUp();

	List<E> concat( List<E> other );

	//TODO head 

	//TODO boolean any(Eq<? super E> eq, E e);

}
