package de.jbee.core.type;

import de.jbee.core.Null;
import de.jbee.core.Nullsave;

public final class Equal {

	private Equal() {
		throw new UnsupportedOperationException( "util" );
	}

	public static final Eq<Object> identity = new IdenticalEqual();
	public static final Eq<Object> equality = nullsave( new EqualityEqual() );
	public static final Eq<Object> objectId = nullsave( new ObjectIdEqual() );

	public static <T> Eq<T> nullsave( Eq<T> eq ) {
		return Null.isSave( eq )
			? eq
			: new NullsaveEqual<T>( eq );
	}

	static final class IdenticalEqual
			implements Eq<Object>, Nullsave {

		@Override
		public boolean eq( Object one, Object other ) {
			return one == other;
		}

	}

	static final class ObjectIdEqual
			implements Eq<Object> {

		@Override
		public boolean eq( Object one, Object other ) {
			return System.identityHashCode( one ) == System.identityHashCode( other )
					&& one.getClass() == other.getClass();
		}
	}

	static final class EqualityEqual
			implements Eq<Object> {

		@Override
		public boolean eq( Object one, Object other ) {
			return one.equals( other );
		}

	}

	static final class NullsaveEqual<T>
			implements Eq<T>, Nullsave {

		final Eq<T> eq;

		NullsaveEqual( Eq<T> eq ) {
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
