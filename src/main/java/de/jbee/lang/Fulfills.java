package de.jbee.lang;

import de.jbee.util.Equal;
import de.jbee.util.IEquality;

public class Fulfills {

	private Fulfills() {
		// util
	}

	public static final Predicate<Object> TRUE = new Predicate<Object>() {

		@Override
		public boolean fulfilledBy( Object obj ) {
			return true;
		}
	};

	public static final Predicate<Object> FALSE = new Predicate<Object>() {

		@Override
		public boolean fulfilledBy( Object obj ) {
			return false;
		}
	};

	private static final class NotCondition<T>
			implements Predicate<T> {

		private final Predicate<T> condition;

		NotCondition( Predicate<T> condition ) {
			this.condition = condition;
		}

		public boolean fulfilledBy( T obj ) {
			return !condition.fulfilledBy( obj );
		}
	}

	private static final class AndCondition<T>
			implements Predicate<T> {

		private final Predicate<T> left;
		private final Predicate<T> right;

		AndCondition( Predicate<T> left, Predicate<T> right ) {
			this.left = left;
			this.right = right;
		}

		public boolean fulfilledBy( T obj ) {
			return left.fulfilledBy( obj ) && right.fulfilledBy( obj );
		}
	}

	private static final class OrCondition<T>
			implements Predicate<T> {

		private final Predicate<T> left;
		private final Predicate<T> right;

		OrCondition( Predicate<T> left, Predicate<T> right ) {
			this.left = left;
			this.right = right;
		}

		public boolean fulfilledBy( T obj ) {
			return left.fulfilledBy( obj ) || right.fulfilledBy( obj );
		}
	}

	private static final class EqualityCondition<T>
			implements Predicate<T> {

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
			implements Predicate<T> {

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
	public static <T> Predicate<T> never() {
		return (Predicate<T>) FALSE;
	}

	@SuppressWarnings ( "unchecked" )
	public static <T> Predicate<T> always() {
		return (Predicate<T>) TRUE;
	}

	public static <T> Predicate<T> not( Predicate<T> condition ) {
		return new NotCondition<T>( condition );
	}

	public static <T> Predicate<T> any( Predicate<T> left, Predicate<T> right ) {
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

	public static <T> Predicate<T> both( Predicate<T> left, Predicate<T> right ) {
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

	public static <T> Predicate<T> equalTo( T reference ) {
		return equality( Equal.equals(), reference );
	}

	public static <T> Predicate<T> eqTo( Eq<? super T> eq, T sample ) {
		return new EqCondition<T>( eq, sample );
	}

	public static <T> Predicate<T> differentTo( T reference ) {
		return not( equalTo( reference ) );
	}

	public static <T> Predicate<T> equality( IEquality<? super T> equality, T reference ) {
		return new EqualityCondition<T>( equality, reference );
	}
}
