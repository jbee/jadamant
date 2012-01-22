package de.jbee.lang;

public interface PartialSequence<E>
		extends Sequence<E> {

	/**
	 * A tail-list of this {@link Sequence} omitting an undefined count (>=0) of leading elements.
	 * 
	 * It depends on this lists internal structure how many elements are part of the sub-sequence.
	 * It may be the empty list too. In any case the sequence returned is also the ending of this
	 * list. So it will refer to the same elements in the same sequence as the same length sublist
	 * at the end of this list.
	 */
	PartialSequence<E> subsequent();
}
