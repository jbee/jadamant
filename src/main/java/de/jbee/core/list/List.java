package de.jbee.core.list;

import de.jbee.core.Appendable;
import de.jbee.core.Core;
import de.jbee.core.IndexAccessible;
import de.jbee.core.ModifiableSequence;
import de.jbee.core.Prepandable;
import de.jbee.core.type.Enumerate;
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
		ModifiableSequence<E> {

	ListFactory with = Core.list;

	ListTransition reverse = ListTransition.reverse;

	ListTransition tail = ListTransition.tail;

	EnumerableListFactory<Integer> numbers = CoreList.factory( Enumerate.INTEGERS );

	/*
	 * override inherited methods with extending return types
	 */

	/**
	 * Expect this to be the fastest way to add elements. Almost O(1) in every case.
	 */
	List<E> prepand( E e );

	/**
	 * Expect this to be much slower than {@link #prepand(Object)}. Only use this when
	 * {@link CoreList#reverse(List)} a list so a <code>append</code> is in fact a
	 * <code> prepand</code>.
	 */
	List<E> append( E e );

	/*
	 * override inherited methods with extending return types
	 * 
	 * FROM: ModifiableSequence
	 */

	List<E> insertAt( int index, E e );

	List<E> deleteAt( int index );

	List<E> replaceAt( int index, E e );

	List<E> takeL( int beginning );

	List<E> takeR( int ending );

	List<E> dropL( int beginning );

	List<E> dropR( int ending );

	/*
	 * additional methods
	 */

	List<E> tidyUp();

	//TODO head - tail

	//TODO IElasticList<E> prepand(ICluster<E> elems);

	//TODO IElasticList<E> append(ICluster<E> elems);

	//TODO IElasticList<E> cutout( int from, int to );

	/**
	 * Why is {@link #swap(int, int)} a API method ?
	 * <p>
	 * Many sort algorithms use the swap operation as a basic step to do their job. As part of the
	 * API swaps can be done through a list wrapper by simply build a index mapping table and change
	 * the indices therein. Therefore swap can become a very cheap O(1) operation which allows to
	 * implement sort algorithms directly on the {@link List} API instead of copying data to another
	 * data structure (that seamed appropriate for fast swaps) sort it and build a new result list.
	 * </p>
	 * <p>
	 * FIXME bad idea. the mapping of that swapping list would have to behave immutable too. but
	 * than its no cheap operation to change something in the mapping. has to be a in-place solution
	 * that makes this very clear - not the List interface.
	 * </p>
	 */
	//List<E> swap( int idx1, int idx2 );

	List<E> concat( List<E> other );

	//TODO boolean any(Eq<? super E> eq, E e);

}
