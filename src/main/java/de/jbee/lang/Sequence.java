package de.jbee.lang;

import de.jbee.lang.dev.Nonnull;

/**
 * A {@linkplain Sequence} is a fixed length data-structure that is also {@link IndexAccessible}.
 * <p>
 * <b>By convention {@linkplain Sequence}s will never accept/contain <code>null</code> -references!
 * Each implementation has to ensure this constraint!</b>
 * </p>
 * 
 * @author Jan Bernitt (jan.bernitt@gmx.de)
 * 
 * @see List, Set, Bag
 * @see ModifiableSequence
 * @see Nonnull#element(Object)
 */
public interface Sequence<E>
		extends IndexAccessible<E> {

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

	/**
	 * @return The length of this sequence. This is a value greater than or equal to <code>0</code>.
	 */
	int length();

	/**
	 * @return true in case this sequence has no elements otherwise false. This is the same as
	 *         {@link #length()} <code>== 0</code> but can be more readable and might have a
	 *         slightly higher performance.
	 */
	boolean isEmpty();
}
