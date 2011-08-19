package de.jbee.lang;

import de.jbee.lang.dev.Null;
import de.jbee.lang.dev.Nullproof;
import de.jbee.lang.dev.Nullsave;

public final class Equal {

	private Equal() {
		throw new UnsupportedOperationException( "util" );
	}

	/**
	 * Two objects count as equal if <code>one == other</code> is <code>true</code>.
	 */
	public static final Eq<Object> identity = new IdentityEquality();
	/**
	 * Two objects count as equal if <code>one.equals(other)</code> is <code>true</code>.
	 * 
	 * @see #equals(Object)
	 */
	public static final Eq<Object> equals = nullsave( new EqualsEquality() );
	/**
	 * Two objects count as equal if {@link System#identityHashCode(Object)} results in the same
	 * hash code and both objects are instances of the same class.
	 */
	public static final Eq<Object> objectId = nullsave( new ObjectIdEquality() );

	public static <T> Eq<T> nullsave( Eq<T> eq ) {
		return Null.isSave( eq )
			? eq
			: new NullsaveEquality<T>( eq );
	}

	public static <T> Eq<T> not( Eq<T> eq ) {
		return eq instanceof NotEquality<?>
			? ( (NotEquality<T>) eq ).eq
			: new NotEquality<T>( eq );
	}

	public static <T> Eq<T> by( Ord<T> ord ) {
		return new OrdEquality<T>( ord );
	}

	static final class IdentityEquality
			implements Eq<Object>, Nullsave {

		@Override
		public boolean holds( Object one, Object other ) {
			return one == other;
		}

	}

	static final class ObjectIdEquality
			implements Eq<Object> {

		@Override
		public boolean holds( Object one, Object other ) {
			return System.identityHashCode( one ) == System.identityHashCode( other )
					&& one.getClass() == other.getClass();
		}
	}

	static final class EqualsEquality
			implements Eq<Object> {

		@Override
		public boolean holds( Object one, Object other ) {
			return one.equals( other );
		}

	}

	static final class OrdEquality<T>
			implements Eq<T> {

		private final Ord<T> ord;

		OrdEquality( Ord<T> ord ) {
			super();
			this.ord = ord;
		}

		@Override
		public boolean holds( T one, T other ) {
			return ord.ord( one, other ).isEq();
		}

	}

	static final class NotEquality<T>
			implements Eq<T>, Nullproof {

		final Eq<T> eq;

		NotEquality( Eq<T> eq ) {
			super();
			this.eq = eq;
		}

		@Override
		public boolean holds( T one, T other ) {
			return !eq.holds( one, other );
		}

		@Override
		public boolean isNullsave() {
			return Null.isSave( eq );
		}

	}

	static final class NullsaveEquality<T>
			implements Eq<T>, Nullsave {

		private final Eq<T> eq;

		NullsaveEquality( Eq<T> eq ) {
			super();
			this.eq = eq;
		}

		@Override
		public boolean holds( T one, T other ) {
			if ( one == null ) {
				return other == null;
			}
			if ( other == null ) {
				return false;
			}
			return eq.holds( one, other );
		}
	}
}
