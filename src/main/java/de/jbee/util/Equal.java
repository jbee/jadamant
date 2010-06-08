package de.jbee.util;

public final class Equal {

	private Equal() {
		// util
	}

	public static final IEquality<?> IDENTITY = new IEquality<Object>() {

		@Override
		public boolean is( Object e1, Object e2 ) {
			return e1 == e2;
		}

	};

	public static final IEquality<?> EQUALS = new IEquality<Object>() {

		@Override
		public boolean is( Object e1, Object e2 ) {
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
	public static <T> IEquality<T> identity() {
		return (IEquality<T>) IDENTITY;
	}

	@SuppressWarnings ( "unchecked" )
	public static <T> IEquality<T> equals() {
		return (IEquality<T>) EQUALS;
	}
}
