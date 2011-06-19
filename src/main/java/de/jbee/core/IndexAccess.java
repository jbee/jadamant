package de.jbee.core;

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
		return new IndexAccessIterator<E>( sequence, start, end, increment );
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
			index = index + increment;
			return sequence.at( index );
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException( "Not a mutable data structure!" );
		}

	}

}
