package de.jbee.lang;

/**
 * A {@link Sequence} consists of a chain of sub-sequences. The next sub-sequence in that chain can
 * be received using the {@link #subsequent()} method.
 * 
 * If there is no further element in that chain the subsequence is a empty sequence. The subsequence
 * of an empty sequence is also a empty sequence.
 * 
 * @author Jan Bernitt (jan.bernitt@gmx.de)
 */
public interface PartialSequence<E>
		extends Sequence<E> {

	/**
	 * A tail-list of this {@link Sequence} omitting an undefined count (>=0) of leading elements.
	 * 
	 * It depends on this sequence's internal structure how many elements are part of the
	 * sub-sequence. It may be a empty sequence too.
	 * 
	 * In any case the sequence returned is also the ending of this sequence. So it will refer to
	 * the same elements in the same order as the subsequence at the end of this sequence having the
	 * same length as the result value.
	 */
	PartialSequence<E> subsequent();
}
