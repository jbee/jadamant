package de.jbee.core;

public final class Res<T> {

	public static <T> Res<T> of( T init ) {
		return new Res<T>( init );
	}

	public static <T> Res<T> newInstance() {
		return new Res<T>( null );
	}

	public T value;

	private Res( T init ) {
		this.value = init;
	}
}
