package de.jbee.lang;

import de.jbee.lang.dev.Null;
import de.jbee.lang.dev.Nullfragile;
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

	public static final Predicate<Object> NULL = new NullPredicate();

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

	public static Predicate<Object> instanceOf( Class<?> type ) {
		return new InstanceOfPredicate( type );
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

	public static <T> Predicate<T> between( T min, T max ) {
		return and( ge( min ), le( max ) );
	}

	public static Predicate<String> containing( String infix ) {
		return new InfixPredicate( infix );
	}

	public static Predicate<String> endingWith( String suffix ) {
		return new EndsWithPredicate( suffix );
	}

	public static Predicate<String> startingWith( String prefix ) {
		return new StartsWithPredicate( prefix );
	}

	private Is() {
		throw new UnsupportedOperationException( "util" );
	}

	private static final class PartiallyAppliedRelationalOpPredicate<T>
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

	private static final class InfixPredicate
			implements Predicate<String>, Nullfragile {

		private final String infix;

		InfixPredicate( String infix ) {
			super();
			this.infix = infix;
		}

		@Override
		public boolean is( String obj ) {
			return obj.contains( infix );
		}

	}

	private static final class EndsWithPredicate
			implements Predicate<String>, Nullfragile {

		private final String suffix;

		EndsWithPredicate( String suffix ) {
			super();
			this.suffix = suffix;
		}

		@Override
		public boolean is( String obj ) {
			return obj.endsWith( suffix );
		}

	}

	private static final class NullPredicate
			implements Predicate<Object> {

		NullPredicate() {
			// make visible
		}

		@Override
		public boolean is( Object obj ) {
			return obj == null;
		}

	}

	private static final class StartsWithPredicate
			implements Predicate<String>, Nullfragile {

		private final String prefix;

		StartsWithPredicate( String prefix ) {
			super();
			this.prefix = prefix;
		}

		@Override
		public boolean is( String obj ) {
			return obj.startsWith( prefix );
		}

	}

	private static final class InstanceOfPredicate
			implements Predicate<Object> {

		private final Class<?> type;

		InstanceOfPredicate( Class<?> type ) {
			super();
			this.type = type;
		}

		@Override
		public boolean is( Object obj ) {
			return type.isInstance( obj );
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
