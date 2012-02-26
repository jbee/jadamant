package de.jbee.data;

import static java.lang.Character.isDigit;

public final class Path
		implements CharSequence {

	public static final Path ROOT = new Path( "" );

	private final String path;

	public static final char SEPARATOR = '.';
	public static final char ITEM_SEPARATOR = ':';

	private Path( String path ) {
		super();
		this.path = path;
	}

	public static Path recordPath( String value ) {
		return value == null || value.isEmpty()
			? ROOT
			: new Path( value );
	}

	public Path dot( Path suffix ) {
		return path.isEmpty()
			? suffix
			: suffix.startsWithItem()
				? new Path( path + Path.ITEM_SEPARATOR + suffix.path )
				: new Path( path + Path.SEPARATOR + suffix.path );
	}

	public Path parent() {
		if ( path.isEmpty() ) {
			return this;
		}
		int separatorIndex = path.lastIndexOf( SEPARATOR );
		return separatorIndex < 0
			? ROOT
			: subSequence( 0, separatorIndex );
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

	public boolean startsWithItem() {
		return length() > 0 && isDigit( path.charAt( 0 ) );
	}

	public boolean endsWithItem() {
		final int dotIndex = path.lastIndexOf( ITEM_SEPARATOR );
		return dotIndex >= 0 && isDigit( path.charAt( dotIndex + 1 ) );
	}

	public Path itemParent() {
		return endsWithItem()
			? subSequence( 0, path.lastIndexOf( ITEM_SEPARATOR ) )
			: this;
	}

	@Override
	public Path subSequence( int start, int end ) {
		return recordPath( path.substring( start, end ) );
	}

	public static Path itemPath( int index ) {
		return new Path( index + "" );
	}
}
