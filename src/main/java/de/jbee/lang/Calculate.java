package de.jbee.lang;

/**
 * 
 * @author Jan Bernitt (jan.bernitt@gmx.de)
 * 
 */
public final class Calculate {

	private Calculate() {
		throw new UnsupportedOperationException( "util" );
	}

	public static int nextHighestPowerOf2( int number ) {
		number--;
		number |= number >> 1;
		number |= number >> 2;
		number |= number >> 4;
		number |= number >> 8;
		number |= number >> 16;
		number++;
		return number;
	}
}
