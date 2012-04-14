package de.jbee.lang;

import java.io.Serializable;
import java.util.RandomAccess;

import de.jbee.lang.seq.AlterBy;
import de.jbee.lang.seq.IndexFor;
import de.jbee.lang.seq.Iterate;
import de.jbee.lang.seq.ModifyBy;
import de.jbee.lang.seq.Range;
import de.jbee.lang.seq.Sequences;
import de.jbee.lang.seq.UtileLister;
import de.jbee.lang.seq.Range.RangeTo;

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
 * <br>
 * <h4>Index Based API</h4>
 * <p>
 * The collection API is index based. This means elements are removed, replaced or inserted at/from
 * a specific index. If this depends on a element's properties the index has to be computed first.
 * Thereby the overuse of generic type arguments is avoided when dealing with lists since the index
 * has no generic. Since all {@linkplain List} implementations are {@link RandomAccess}able by
 * definition using indexes will not make a relevant difference in performance. Because Lists are in
 * general chained sublists ({@link Segment}s) the decision over random access is a general one that
 * cannot be different from implementation to implementation.
 * </p>
 * 
 * @author Jan Bernitt (jan.bernitt@gmx.de)
 * 
 */
public interface List<E>
		extends Prepandable<E>, Appendable<E>, Segment<E>, Sequence<E>, ModifiableSequence<E>,
		Arrayable, Traversable<E>, RandomAccess, Serializable {

	UtileLister with = Sequences.list;
	RangeTo rangeTo = Sequences.enumerator;
	IndexFor indexFor = IndexFor.indexFor;
	AlterBy alterBy = AlterBy.alterBy;
	ModifyBy modifyBy = ModifyBy.modifyBy;
	Iterate iterate = Iterate.instance;
	Traverse traverse = Traverse.instance;

	Range<Integer> numbers = Sequences.numbers;
	Range<Character> letters = Sequences.letters;
	Range<Character> characters = Sequences.characters;

	List<Object> empty = List.with.noElements();

	//OPEN instead of contains use consistsOf ?

	//TODO a util for string handling strings as list of characters

	/**
	 * <h5>What does {@link #tidyUp()} do ?</h5>
	 * <p>
	 * <b>It can avoid possible memory leaks</b> that can be caused by references to other objects
	 * than just the elements of the current list and the list structure itself. Any kind of lazy
	 * evaluated wrapper could introduce or cause such references.
	 * </p>
	 * <p>
	 * Persistent data-structures might also use something that can be described as 'shared memory'.
	 * A list that is still reachable may internally refer to elements that aren't members of the
	 * actual list and wouldn't be reachable if this list woundn't exists. Thereby those elements
	 * aren't garbage collected as long as the list referring itself is reachable as it is.
	 * </p>
	 * <p>
	 * This operation will make sure, that only those elements are refereed further on which are
	 * part of the reachable list and that any operation on that list cannot introduce references to
	 * non-member elements. Often this requires to restructure the list internally. This can be a
	 * expensive operation in some cases.
	 * </p>
	 * <br/>
	 * <h5>When is it necessary to do a {@link #tidyUp()} ?</h5>
	 * <p>
	 * As a simple rule of thumb you can call {@link #tidyUp()} always when a list becomes
	 * <i>state</i>, that is when assigning it to a field / constant. This will definitely avoid
	 * memory leaks but might be unnecessary in a lot of cases.
	 * </p>
	 * <p>
	 * If one of the following is true, it is normally not necessary to tidy up on assignment:
	 * <ul>
	 * <li>The elements are simple value objects (like primitive type wrappers)</li>
	 * <li>It is known that the object containing the field will have a short lifetime</li>
	 * <li>It is known that the list is constructed once and will not be modified further on</li>
	 * </ul>
	 * </p>
	 */
	List<E> tidyUp();

	/**
	 * @return a list having the <code>other</code> list append to this list's elements.
	 */
	List<E> concat( List<E> other );

	/*
	 * Covariant return type overrides for inherited methods from PartialSequence
	 */

	@Override
	List<E> subsequent();

	/*
	 * Covariant return type overrides for inherited methods from Appendable and Prepandable
	 */

	/**
	 * Expect this to be the fastest way to add elements. Almost O(1) in every case.
	 */
	@Override
	List<E> prepand( E e );

	/**
	 * Expect this to be slower than using {@link #prepand(Object)}.
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

	interface Lister {

		<E> List<E> noElements();

		<E> List<E> element( E e );

		<E> List<E> elements( E... elems );

		<E> List<E> elements( Sequence<E> elems );
	}

	interface ListBuilder<E> {

	}
}
