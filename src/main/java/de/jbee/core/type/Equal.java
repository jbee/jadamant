package de.jbee.core.type;

import de.jbee.core.dev.Null;
import de.jbee.core.dev.Nullproof;
import de.jbee.core.dev.Nullsave;

public final class Equal {

	private Equal() {
		throw new UnsupportedOperationException( "util" );
	}

	/**
	 * Two objects count as equal if <code>one == other</code> is <code>true</code>.
	 */
	public static final Eq<Object> identity = new SameIdentityEquality();
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

	static final class SameIdentityEquality
			implements Eq<Object>, Nullsave {

		@Override
		public boolean eq( Object one, Object other ) {
			return one == other;
		}

	}

	static final class ObjectIdEquality
			implements Eq<Object> {

		@Override
		public boolean eq( Object one, Object other ) {
			return System.identityHashCode( one ) == System.identityHashCode( other )
					&& one.getClass() == other.getClass();
		}
	}

	static final class EqualsEquality
			implements Eq<Object> {

		@Override
		public boolean eq( Object one, Object other ) {
			return one.equals( other );
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
		public boolean eq( T one, T other ) {
			return !eq.eq( one, other );
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
		public boolean eq( T one, T other ) {
			if ( one == null ) {
				return other == null;
			}
			if ( other == null ) {
				return false;
			}
			return eq.eq( one, other );
		}
	}
}
