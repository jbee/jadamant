package de.jbee.util;

import de.jbee.lang.Eq;

public final class Equal {

	private Equal() {
		// util
	}

	public static final Eq<?> IDENTITY = new Eq<Object>() {

		@Override
		public boolean holds( Object e1, Object e2 ) {
			return e1 == e2;
		}

	};

	public static final Eq<?> EQUALS = new Eq<Object>() {

		@Override
		public boolean holds( Object e1, Object e2 ) {
			if ( e1 == e2 ) {
				return true;
			}
			if ( e1 == null || e2 == null ) {
				return false;
			}
			return e1.equals( e2 );
		}
	};

	@SuppressWarnings ( "unchecked" )
	public static <T> Eq<T> identity() {
		return (Eq<T>) IDENTITY;
	}

	@SuppressWarnings ( "unchecked" )
	public static <T> Eq<T> equals() {
		return (Eq<T>) EQUALS;
	}
}
