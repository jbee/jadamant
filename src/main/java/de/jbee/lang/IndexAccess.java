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
		return sequence instanceof PartialSequence<?>
			? iterator( (PartialSequence<E>) sequence, start, end, increment )
			: new IndexAccessIterator<E>( sequence, start, end, increment );
	}

	public static <E> Iterator<E> iterator( PartialSequence<E> sequence, int start, int end,
			int increment ) {
		return new ForwardIterator<E>( sequence, start, end, increment );
	}

	public static <E> Iterable<E> iterable( final Sequence<E> seq ) {
		return new IndexAccessIterable<E>( seq, 0, seq.length(), 1 );
	}

	public static <E> Iterable<E> reverseIterable( final Sequence<E> seq ) {
		return new IndexAccessIterable<E>( seq, seq.length(), -1, -1 );
	}

	private static final class ForwardIterator<E>
			implements Iterator<E> {

		private final int increment;

		private PartialSequence<? extends E> sequence;
		private int partialLength;
		private int length;
		private int index;

		ForwardIterator( PartialSequence<? extends E> seq, int start, int end, int increment ) {
			super();
			this.sequence = seq;
			this.length = end - start;
			this.index = start;
			this.increment = increment;
			this.partialLength = seq.length() - seq.subsequent().length();
		}

		@Override
		public boolean hasNext() {
			return index + increment < length;
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
				? index + increment > end
				: index + increment < end;
		}

		@Override
		public E next() {
			E res = sequence.at( index );
			index = index + increment;
			return res;
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException( "Not a mutable data structure!" );
		}

	}

}
