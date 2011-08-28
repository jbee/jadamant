package de.jbee.lang;

import de.jbee.lang.dev.Null;
import de.jbee.lang.dev.Nullproof;

public final class Is {

	public static final Predicate<Object> TRUE = new Predicate<Object>() {

		@Override
		public boolean is( Object obj ) {
			return true;
		}
	};

	public static final Predicate<Object> FALSE = new Predicate<Object>() {

		@Override
		public boolean is( Object obj ) {
			return false;
		}
	};

	@SuppressWarnings ( "unchecked" )
	public static <T> Predicate<T> true_() {
		return (Predicate<T>) TRUE;
	}

	public static <T> Predicate<T> or( Predicate<T> left, Predicate<T> right ) {
		if ( left == FALSE ) {
			return right;
		}
		if ( right == FALSE ) {
			return left;
		}
		return ( left == TRUE || right == TRUE )
			? Is.<T> true_()
			: new OrPredicate<T>( left, right );
	}

	public static <T> Predicate<T> fulfilledBy( RelationalOp<T> op, T left ) {
		return new PartiallyAppliedRelationalOpPredicate<T>( left, op );
	}

	public static <T> Predicate<T> and( Predicate<T> left, Predicate<T> right ) {
		if ( left == TRUE ) {
			return right;
		}
		if ( right == TRUE ) {
			return left;
		}
		return ( left == FALSE || right == FALSE )
			? Is.<T> false_()
			: new AndPredicate<T>( left, right );
	}

	public static <T> Predicate<T> eqBy( T sample, Eq<? super T> eq ) {
		return new EqPredicate<T>( sample, eq );
	}

	public static <T> Predicate<T> eq( T sample ) {
		return eqBy( sample, Equal.equals );
	}

	public static <T> Predicate<T> notEq( T sample ) {
		return not( eqBy( sample, Equal.equals ) );
	}

	@SuppressWarnings ( "unchecked" )
	public static <T> Predicate<T> false_() {
		return (Predicate<T>) FALSE;
	}

	public static <T> Predicate<T> not( Predicate<T> predicate ) {
		return predicate instanceof NegatePredicate<?>
			? ( (NegatePredicate<T>) predicate ).predicate
			: new NegatePredicate<T>( predicate );
	}

	public static <T> Predicate<T> lt( T value ) {
		return fulfilledBy( Operator.<T> ltBy( Order.inherent ), value );
	}

	public static <T> Predicate<T> gt( T value ) {
		return fulfilledBy( Operator.<T> gtBy( Order.inherent ), value );
	}

	public static <T> Predicate<T> le( T value ) {
		return fulfilledBy( Operator.<T> leBy( Order.inherent ), value );
	}

	public static <T> Predicate<T> ge( T value ) {
		return fulfilledBy( Operator.<T> geBy( Order.inherent ), value );
	}

	private Is() {
		throw new UnsupportedOperationException( "util" );
	}

	static final class PartiallyAppliedRelationalOpPredicate<T>
			implements Predicate<T>, Nullproof {

		private final T left;
		private final RelationalOp<T> op;

		PartiallyAppliedRelationalOpPredicate( T left, RelationalOp<T> op ) {
			super();
			this.left = left;
			this.op = op;
		}

		@Override
		public boolean is( T right ) {
			return op.holds( left, right );
		}

		@Override
		public boolean isNullsave() {
			return Null.isSave( op );
		}

	}

	private static final class AndPredicate<T>
			implements Predicate<T> {

		private final Predicate<T> left;
		private final Predicate<T> right;

		AndPredicate( Predicate<T> left, Predicate<T> right ) {
			this.left = left;
			this.right = right;
		}

		public boolean is( T obj ) {
			return left.is( obj ) && right.is( obj );
		}
	}

	private static final class EqPredicate<T>
			implements Predicate<T> {

		private final T sample;
		private final Eq<? super T> eq;

		EqPredicate( T sample, Eq<? super T> eq ) {
			super();
			this.eq = eq;
			this.sample = sample;
		}

		@Override
		public boolean is( T obj ) {
			return eq.holds( sample, obj );
		}

	}

	private static final class NegatePredicate<T>
			implements Predicate<T> {

		final Predicate<T> predicate;

		NegatePredicate( Predicate<T> condition ) {
			this.predicate = condition;
		}

		public boolean is( T obj ) {
			return !predicate.is( obj );
		}
	}

	private static final class OrPredicate<T>
			implements Predicate<T> {

		private final Predicate<T> left;
		private final Predicate<T> right;

		OrPredicate( Predicate<T> left, Predicate<T> right ) {
			this.left = left;
			this.right = right;
		}

		public boolean is( T obj ) {
			return left.is( obj ) || right.is( obj );
		}
	}
}
