package de.jbee.core;

public final class Cast {

	private Cast() {
		throw new UnsupportedOperationException( "util" );
	}

	/**
	 * Its save since the operation just needs a super-type of T to operate well.
	 */
	@SuppressWarnings ( "unchecked" )
	public static <T> Op<T> genericDowncast( Op<? super T> op ) {
		return (Op<T>) op;
	}
}
