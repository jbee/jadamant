package de.jbee.lang;

import java.io.Serializable;
import java.util.RandomAccess;

import de.jbee.lang.seq.Iterate;
import de.jbee.lang.seq.UtileEnumerator;
import de.jbee.lang.seq.UtileEnumeratorFactory;
import de.jbee.lang.seq.UtileListIndex;
import de.jbee.lang.seq.UtileListTransition;
import de.jbee.lang.seq.UtileLister;

/**
 * <p>
 * Lists are immutable and use a persistent data-structure whereby they are thread-save by design.
 * There is no need to offer/have or use another concurrent collection API. Just use {@link List}s
 * for everything everywhere!
 * </p>
 * <p>
 * This interface is intentionally not a <i>minimal class interface</i>. It provides several methods
 * whose results could be achieved by combine others. This decision was made since the list
 * structure is a core functionality that should be usable as easy, compact and readable as
 * possible.
 * </p>
 * <p>
 * The collection API is index based. This means elements are removed, replaced or inserted at/from
 * a specific index. If this depends on a element's properties the index has to be computed first.
 * Thereby the overuse of generic type arguments is avoided when dealing with lists since the index
 * is generic-free. Since all {@linkplain List} implementations are {@link RandomAccess}able by
 * definition this will not make a relevant difference in performance. Because Lists are chains of
 * sublist in general the decision over random access is a general one that cannot be different from
 * implementation to implementation.
 * </p>
 * 
 * @author Jan Bernitt (jan.bernitt@gmx.de)
 * 
 */
public interface List<E>
		extends Prepandable<E>, Appendable<E>, Sequence<E>, ModifiableSequence<E>, Arrayable,
		Traversable<E>, RandomAccess, Serializable {

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

	UtileLister with = Lang.list;
	UtileListIndex indexFor = new UtileListIndex();
	UtileEnumeratorFactory does = Lang.enumerator;
	//TODO proxy from Lang.
	UtileListTransition that = UtileListTransition.instance;

	UtileEnumerator<Integer> numbers = Lang.numbers;
	UtileEnumerator<Character> letters = Lang.letters;
	UtileEnumerator<Character> characters = Lang.characters;

	//TODO a util for string handling strings as list of characters

	Iterate iterate = Iterate.instance;

	Traverse traverse = Traverse.instance;

	ListTransition reverse = UtileListTransition.reverse;

	ListTransition tail = UtileListTransition.tail;

	/*
	 * Covariant return type overrides for inherited methods from Appendable and Prepandable
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
	 * Covariant return type overrides for inherited methods from ModifiableSequence
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

	/**
	 * <h5>What does {@link #tidyUp()} do ?</h5>
	 * <p>
	 * It can avoids possible memory leaks. Persistent data-structures use something that can be
	 * described as 'shared memory'. A list that is still alive (reachable) may refer also to
	 * elements that would be dead (not reachable) if this list woundn't exists. Thereby elements
	 * refereed in list may no be garbage collected as long as the list referring it is alive or
	 * tidied up. The operation will make sure, that only those elements are refereed further on
	 * which are part of the list alive. In some cases therefore array copies as necessary so this
	 * is *not* a cheap operation in every case.
	 * </p>
	 * <br/>
	 * <h5>When is it necessary to do a {@link #tidyUp()} ?</h5>
	 * <p>
	 * As long as you just use {@link #append(Object)}, {@link #prepand(Object)} or
	 * {@link #concat(List)} to construct a list you are fine. You don't need to tidy-up and if you
	 * do anyway (just to be save) it will be a cheap operation.
	 * </p>
	 * <p>
	 * But as soon as you make use of any of the other 'modifying' operations from
	 * {@link ModifiableSequence} the resulting list might such a case but doesn't have to be. To be
	 * save you should *alawys* call {@link #tidyUp()} once when you are finished changing a list
	 * (end of method call that calls those methods and returns a changed list). Of cause you don't
	 * have to tidy-up if all lists result from such operations are temporary objects which itself
	 * will be dead soon and garbage-collected themselfs.
	 * </p>
	 */
	List<E> tidyUp();

	List<E> concat( List<E> other );

}
