package de.jbee.core.dev;

public final class Nonnull {

	private Nonnull() {
		throw new UnsupportedOperationException( "util" );
	}

	public static void element( Object e ) {
		if ( e == null ) {
			throw new NullPointerException( "null is not supported as an element of this list" );
		}
	}
}
