package de.jbee.data;

public final class Path
		implements CharSequence {

	public static final Path ROOT = new Path( "" );

	private final String path;

	public static final char SEPARATOR = '.';

	private Path( String path ) {
		super();
		this.path = path;
	}

	public static Path path( String value ) {
		return new Path( value );
	}

	public Path dot( Path suffix ) {
		return path.isEmpty()
			? suffix
			: new Path( path + Path.SEPARATOR + suffix.path );
	}

	@Override
	public String toString() {
		return path;
	}

	@Override
	public char charAt( int index ) {
		return path.charAt( index );
	}

	@Override
	public int length() {
		return path.length();
	}

	@Override
	public Path subSequence( int start, int end ) {
		return new Path( path.substring( start, end ) );
	}
}
