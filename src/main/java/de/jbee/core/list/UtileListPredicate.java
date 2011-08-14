package de.jbee.core.list;

import de.jbee.core.type.Eq;

public class UtileListPredicate {

	// contains/any

	// all

	// endsWith(List)

	// startsWith(List)

	static final class AnyEqualElementInListPredicate<E>
			implements ListPredicate<E> {

		private final E sample;
		private final Eq<E> equality;

		AnyEqualElementInListPredicate( E sample, Eq<E> equality ) {
			super();
			this.sample = sample;
			this.equality = equality;
		}

		@Override
		public boolean eval( List<E> l ) {
			final int size = l.size();
			for ( int i = 0; i < size; i++ ) {
				if ( equality.holds( sample, l.at( i ) ) ) {
					return true;
				}
			}
			return false;
		}

		@Override
		public String toString() {
			return "(" + String.valueOf( sample ) + ")+";
		}
	}

	static final class AllEqualElementsInListPredicate<E>
			implements ListPredicate<E> {

		private final E sample;
		private final Eq<E> equality;

		AllEqualElementsInListPredicate( E sample, Eq<E> equality ) {
			super();
			this.sample = sample;
			this.equality = equality;
		}

		@Override
		public boolean eval( List<E> l ) {
			//TODO maybe replace this by !any(!eq(sample))
			final int size = l.size();
			for ( int i = 0; i < size; i++ ) {
				if ( !equality.holds( sample, l.at( i ) ) ) {
					return false;
				}
			}
			return true;
		}

		@Override
		public String toString() {
			return "(" + String.valueOf( sample ) + "){n}";
		}
	}

}
