package de.jbee.util;

import java.util.regex.Pattern;

public final class Filename
		extends SpecificString<Filename> {

	private static final long serialVersionUID = 1L;

	private static final Pattern FILENAME = Pattern.compile( "^[a-zA-Z][-_.a-zA-Z0-9]*\\.?[a-z0-9]*$" );

	private Filename( String fileName ) {
		super( fileName );
	}

	public static Filename valueOf( String fileName ) {
		validateFilename( fileName );
		return new Filename( fileName );
	}

	private static void validateFilename( String fileName ) {
		if ( !FILENAME.matcher( fileName ).matches() ) {
			throw new IllegalArgumentException( "Invalid file name: " + fileName );
		}
	}

	public String basename() {
		final int ext = value.lastIndexOf( '.' );
		return ext < 0
			? value
			: value.substring( 0, ext );
	}

	public String extension() {
		final int ext = value.lastIndexOf( '.' );
		return ext < 0
			? ""
			: value.substring( ext + 1 );
	}
}
