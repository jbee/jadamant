package de.jbee.lang;

import de.jbee.lang.dev.Null;
import de.jbee.lang.dev.Nullproof;
import de.jbee.lang.dev.Nullsave;

public final class Operator {

	public static final Op<Object> minimum = minimumBy( Order.inherent );
	public static final Op<Object> maximim = maximumBy( Order.inherent );
	public static final Op<Object> elvis = new ElvisOp<Object>();

	private Operator() {
		throw new UnsupportedOperationException( "util" );
	}

	public static <T> Op<T> maximumBy( Ord<T> ord ) {
		return ord == Order.inherent
			? Cast.<T> genericDowncast( maximim )
			: new MaximumOp<T>( ord );
	}

	public static <T> Op<T> minimumBy( Ord<T> ord ) {
		return ord == Order.inherent
			? Cast.<T> genericDowncast( minimum )
			: new MinimumOp<T>( ord );
	}

	public static <T> RelationalOp<T> not( RelationalOp<T> op ) {
		return op instanceof NegateRelationalOp<?>
			? ( (NegateRelationalOp<T>) op ).op
			: new NegateRelationalOp<T>( op );
	}

	public static <T> RelationalOp<T> ltBy( Ord<? super T> ord ) {
		return new OrderRelationalOp<T>( ord, Ordering.GT );
	}

	public static <T> RelationalOp<T> gtBy( Ord<? super T> ord ) {
		return new OrderRelationalOp<T>( ord, Ordering.LT );
	}

	public static <T> RelationalOp<T> eqBy( Ord<? super T> ord ) {
		return new OrderRelationalOp<T>( ord, Ordering.EQ );
	}

	public static <T> RelationalOp<T> notEqBy( Ord<? super T> ord ) {
		return not( eqBy( ord ) );
	}

	public static <T> RelationalOp<T> geBy( Ord<? super T> ord ) {
		return not( ltBy( ord ) );
	}

	public static <T> RelationalOp<T> leBy( Ord<? super T> ord ) {
		return not( gtBy( ord ) );
	}

	public static <T> Predicate<T> apply( T left, RelationalOp<T> op ) {
		return new PartiallyAppliedRelationalOp<T>( left, op );
	}

	static final class ElvisOp<T>
			implements Op<T>, Nullsave {

		@Override
		public T operate( T left, T right ) {
			return left == null
				? right
				: left;
		}

	}

	static final class MaximumOp<T>
			implements Op<T>, Nullproof {

		private final Ord<T> ord;

		MaximumOp( Ord<T> ord ) {
			super();
			this.ord = ord;
		}

		@Override
		public T operate( T left, T right ) {
			return ord.ord( left, right ).isGt()
				? left
				: right;
		}

		@Override
		public boolean isNullsave() {
			return Null.isSave( ord );
		}

	}

	static final class MinimumOp<T>
			implements Op<T>, Nullproof {

		private final Ord<T> ord;

		MinimumOp( Ord<T> ord ) {
			super();
			this.ord = ord;
		}

		@Override
		public T operate( T left, T right ) {
			return ord.ord( left, right ).isLe()
				? left
				: right;
		}

		@Override
		public boolean isNullsave() {
			return Null.isSave( ord );
		}
	}

	static final class PartiallyAppliedRelationalOp<T>
			implements Predicate<T>, Nullproof {

		private final T left;
		private final RelationalOp<T> op;

		PartiallyAppliedRelationalOp( T left, RelationalOp<T> op ) {
			super();
			this.left = left;
			this.op = op;
		}

		@Override
		public boolean fulfilledBy( T right ) {
			return op.holds( left, right );
		}

		@Override
		public boolean isNullsave() {
			return Null.isSave( op );
		}

	}

	static final class OrderRelationalOp<T>
			implements RelationalOp<T>, Nullproof {

		private final Ord<? super T> ord;
		private final Ordering expected;

		OrderRelationalOp( Ord<? super T> ord, Ordering expected ) {
			super();
			this.ord = ord;
			this.expected = expected;
		}

		@Override
		public boolean holds( T left, T right ) {
			return ord.ord( left, right ) == expected;
		}

		@Override
		public boolean isNullsave() {
			return Null.isSave( ord );
		}
	}

	static final class NegateRelationalOp<T>
			implements RelationalOp<T>, Nullproof {

		final RelationalOp<T> op;

		NegateRelationalOp( RelationalOp<T> op ) {
			super();
			this.op = op;
		}

		@Override
		public boolean holds( T left, T right ) {
			return !op.holds( left, right );
		}

		@Override
		public boolean isNullsave() {
			return Null.isSave( op );
		}

	}
}
