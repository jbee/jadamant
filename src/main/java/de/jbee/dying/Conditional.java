package de.jbee.dying;

public class Conditional<T> {

	public final T positive;
	public final T negative;

	private Conditional( T positive, T negative ) {
		this.positive = positive;
		this.negative = negative;
	}

	public static <T> Conditional<T> of( T positive, T negative ) {
		return new Conditional<T>( positive, negative );
	}
}
