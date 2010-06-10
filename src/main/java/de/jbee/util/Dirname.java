package de.jbee.util;

import java.util.regex.Pattern;

public final class Dirname
		extends SpecificString<Dirname> {

	private static final long serialVersionUID = 1L;

	private static final Pattern DIRNAME = Pattern.compile( "^([-._a-zA-Z0-9]+/?)+$" );

	private Dirname( String value ) {
		super( value );
	}

	public static Dirname valueOf( String dirname ) {
		validateDirname( dirname );
		return new Dirname( dirname );
	}

	private static void validateDirname( String dirname ) {
		if ( !DIRNAME.matcher( dirname ).matches() ) {
			throw new IllegalArgumentException( "Invalid path: " + dirname );
		}
	}
}
