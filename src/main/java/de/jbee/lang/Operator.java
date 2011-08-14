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

	@SuppressWarnings ( "unchecked" )
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
}
