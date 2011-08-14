package de.jbee.util;

import de.jbee.core.type.Eq;

public class Fulfills {

	private Fulfills() {
		// util
	}

	public static final ICondition<Object> TRUE = new ICondition<Object>() {

		@Override
		public boolean fulfilledBy( Object obj ) {
			return true;
		}
	};

	public static final ICondition<Object> FALSE = new ICondition<Object>() {

		@Override
		public boolean fulfilledBy( Object obj ) {
			return false;
		}
	};

	private static final class NotCondition<T>
			implements ICondition<T> {

		private final ICondition<T> condition;

		NotCondition( ICondition<T> condition ) {
			this.condition = condition;
		}

		public boolean fulfilledBy( T obj ) {
			return !condition.fulfilledBy( obj );
		}
	}

	private static final class AndCondition<T>
			implements ICondition<T> {

		private final ICondition<T> left;
		private final ICondition<T> right;

		AndCondition( ICondition<T> left, ICondition<T> right ) {
			this.left = left;
			this.right = right;
		}

		public boolean fulfilledBy( T obj ) {
			return left.fulfilledBy( obj ) && right.fulfilledBy( obj );
		}
	}

	private static final class OrCondition<T>
			implements ICondition<T> {

		private final ICondition<T> left;
		private final ICondition<T> right;

		OrCondition( ICondition<T> left, ICondition<T> right ) {
			this.left = left;
			this.right = right;
		}

		public boolean fulfilledBy( T obj ) {
			return left.fulfilledBy( obj ) || right.fulfilledBy( obj );
		}
	}

	private static final class EqualityCondition<T>
			implements ICondition<T> {

		private final IEquality<? super T> equality;
		private final T reference;

		EqualityCondition( IEquality<? super T> equality, T reference ) {
			super();
			this.equality = equality;
			this.reference = reference;
		}

		public boolean fulfilledBy( T obj ) {
			return equality.is( reference, obj );
		}
	}

	private static final class EqCondition<T>
			implements ICondition<T> {

		private final Eq<? super T> eq;
		private final T sample;

		EqCondition( Eq<? super T> eq, T sample ) {
			super();
			this.eq = eq;
			this.sample = sample;
		}

		@Override
		public boolean fulfilledBy( T obj ) {
			return eq.holds( sample, obj );
		}

	}

	@SuppressWarnings ( "unchecked" )
	public static <T> ICondition<T> never() {
		return (ICondition<T>) FALSE;
	}

	@SuppressWarnings ( "unchecked" )
	public static <T> ICondition<T> always() {
		return (ICondition<T>) TRUE;
	}

	public static <T> ICondition<T> not( ICondition<T> condition ) {
		return new NotCondition<T>( condition );
	}

	public static <T> ICondition<T> any( ICondition<T> left, ICondition<T> right ) {
		if ( left == FALSE ) {
			return right;
		}
		if ( right == FALSE ) {
			return left;
		}
		return ( left == TRUE || right == TRUE )
			? Fulfills.<T> always()
			: new OrCondition<T>( left, right );
	}

	public static <T> ICondition<T> both( ICondition<T> left, ICondition<T> right ) {
		if ( left == TRUE ) {
			return right;
		}
		if ( right == TRUE ) {
			return left;
		}
		return ( left == FALSE || right == FALSE )
			? Fulfills.<T> never()
			: new AndCondition<T>( left, right );
	}

	public static <T> ICondition<T> equalTo( T reference ) {
		return equality( Equal.equals(), reference );
	}

	public static <T> ICondition<T> eqTo( Eq<? super T> eq, T sample ) {
		return new EqCondition<T>( eq, sample );
	}

	public static <T> ICondition<T> differentTo( T reference ) {
		return not( equalTo( reference ) );
	}

	public static <T> ICondition<T> equality( IEquality<? super T> equality, T reference ) {
		return new EqualityCondition<T>( equality, reference );
	}
}
