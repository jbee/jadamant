package de.jbee.lang;

public interface TailedSequence<E>
		extends Sequence<E> {

	/**
	 * A tail-list of this {@link Sequence}. It depends on this lists internal structure how many
	 * elements are part of the tail. The tail can be the empty list too. In any case the tail is
	 * the ending of this list. So it will refer to the same elements in the same sequence as the
	 * same length sublist at the end of this list.
	 */
	TailedSequence<E> tail();
}
