package de.jbee.lang;

/**
 * A {@link Sequence} consists of a chain of {@linkplain Segment}s. The next segment in that chain
 * can be received using the {@link #subsequent()} method.
 * 
 * If there is no further element in a chain the subsequent {@linkplain Segment} is a empty
 * sequence. The subsequence of an empty sequence is also a empty sequence.
 * 
 * @author Jan Bernitt (jan.bernitt@gmx.de)
 */
public interface Segment<E>
		extends Sequence<E> {

	/**
	 * The following {@link Segment} in the chain of segments in this {@link Sequence}.
	 * 
	 * The elements of the this segment will be bypassed. The element count bypassed depends on this
	 * sequence's internal structure and will be >=0. It may be a empty sequence too.
	 * 
	 * In any case the sequence returned is also the ending of this sequence. So it will contain the
	 * same elements in the same order as the subsequence at the end of this sequence having the
	 * same length as the result value.
	 */
	Segment<E> subsequent();
}
