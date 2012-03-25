package de.jbee.lang;

import java.util.Iterator;

public class IndexAccess {

	public static <E> Iterator<E> iterator( IndexAccessible<E> sequence, int start, int end ) {
		return iterator( sequence, start, end, 1 );
	}

	public static <E> Iterator<E> reverseIterator( IndexAccessible<E> sequence, int start, int end ) {
		return iterator( sequence, end, start, -1 );
	}

	public static <E> Iterator<E> iterator( IndexAccessible<E> sequence, int start, int end,
			int increment ) {
		return sequence instanceof Segment<?>
			? iterator( (Segment<E>) sequence, start, end, increment )
			: new IndexAccessIterator<E>( sequence, start, end, increment );
	}

	public static <E> Iterator<E> iterator( Segment<E> sequence, int start, int end,
			int increment ) {
		return new ForwardsIterator<E>( sequence, start, end, increment );
	}

	public static <E> Iterable<E> iterable( final Sequence<E> seq ) {
		return new IndexAccessIterable<E>( seq, 0, seq.length(), 1 );
	}

	public static <E> Iterable<E> reverseIterable( final Sequence<E> seq ) {
		return new IndexAccessIterable<E>( seq, seq.length(), -1, -1 );
	}

	/**
	 * A {@link Iterator} utilizes {@link Segment#subsequent()}-method so that the index
	 * access used to read the {@link #next()} element will not require a dispatch to a subsequent
	 * tail list. This is achieved by change the referred {@link #sequence} to the next subsequent
	 * list as soon as the index points to its first element.
	 * 
	 * @author Jan Bernitt (jan.bernitt@gmx.de)
	 */
	private static final class ForwardsIterator<E>
			implements Iterator<E> {

		private final int increment;

		private Segment<? extends E> sequence;
		private int partialLength;
		private int length;
		private int index;

		ForwardsIterator( Segment<? extends E> seq, int start, int end, int increment ) {
			super();
			this.sequence = seq;
			this.length = end - start;
			this.index = start;
			this.increment = increment;
			this.partialLength = seq.length() - seq.subsequent().length();
		}

		@Override
		public boolean hasNext() {
			return index < length;
		}

		@Override
		public E next() {
			while ( index >= partialLength && !sequence.isEmpty() ) {
				index -= partialLength;
				length -= partialLength;
				sequence = sequence.subsequent();
				partialLength = sequence.length() - sequence.subsequent().length();
			}
			E e = sequence.at( index );
			index += increment;
			return e;
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException( "Not a mutable data structure!" );
		}

	}

	private static final class IndexAccessIterable<E>
			implements Iterable<E> {

		private final Sequence<E> sequence;
		private final int start;
		private final int end;
		private final int increment;

		IndexAccessIterable( Sequence<E> seq, int start, int end, int increment ) {
			super();
			this.sequence = seq;
			this.start = start;
			this.end = end;
			this.increment = increment;
		}

		@Override
		public Iterator<E> iterator() {
			return IndexAccess.iterator( sequence, start, end, increment );
		}
	}

	private static final class IndexAccessIterator<E>
			implements Iterator<E> {

		private final IndexAccessible<E> sequence;
		private final int end;
		private final int increment;

		private int index;

		IndexAccessIterator( IndexAccessible<E> sequence, int start, int end, int increment ) {
			super();
			this.sequence = sequence;
			this.index = start;
			this.end = end;
			this.increment = increment;
		}

		@Override
		public boolean hasNext() {
			return increment < 0
				? index > end
				: index < end;
		}

		@Override
		public E next() {
			E res = sequence.at( index );
			index += increment;
			return res;
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException( "Not a mutable data structure!" );
		}

	}

}
