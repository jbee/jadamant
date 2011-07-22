package de.jbee.core.dev;

public final class Null {

	private Null() {
		throw new UnsupportedOperationException( "util" );
	}

	public static boolean isSave( Object obj ) {
		return obj instanceof Nullsave
				|| ( obj instanceof Nullproof && ( (Nullproof) obj ).isNullsave() );
	}
}
