package de.jbee.util;

import java.io.Serializable;

public class SpecificString<T extends SpecificString<T>>
		implements Comparable<T>, CharSequence, Serializable {

	private static final long serialVersionUID = 1L;
	protected static final String SEPARATOR = "-";

	protected final String value;

	protected SpecificString( String value ) {
		this.value = value;
	}

	@Override
	public int compareTo( T other ) {
		return value.compareTo( other.value );
	}

	@Override
	public char charAt( int index ) {
		return value.charAt( index );
	}

	@Override
	public int length() {
		return value.length();
	}

	@Override
	public CharSequence subSequence( int start, int end ) {
		return value.subSequence( start, end );
	}

	@Override
	public String toString() {
		return value;
	}

	@Override
	public boolean equals( Object obj ) {
		if ( obj != null && obj.getClass() == getClass() ) {
			return ( (SpecificString<?>) obj ).value.equals( value );
		}
		return false;
	}

	@Override
	public int hashCode() {
		return value.hashCode();
	}
}
