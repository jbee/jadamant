package de.jbee.core.type;

import java.util.NoSuchElementException;

import de.jbee.core.Nullsave;

public final class Enumerate {

	public static final Enum<Boolean> BOOLEANS = new EnumerateBoolean();
	public static final Enum<Integer> INTEGERS = numbers( Integer.MIN_VALUE, Integer.MAX_VALUE );
	public static final Enum<Integer> NATURALS = numbers( 0, Integer.MAX_VALUE );
	public static final Enum<Integer> POSITIVES = numbers( 1, Integer.MAX_VALUE );
	public static final Enum<Integer> NUMERARY = numbers( 0, 9 );
	public static final Enum<Character> LETTERS = characters( 'A', 'Z' );
	public static final Enum<Character> CHARACTERS = characters( Character.MIN_VALUE,
			Character.MAX_VALUE );

	public static <E extends java.lang.Enum<?>> Enum<E> type( Class<E> type ) {
		return new EnumerateEnum<E>( type );
	}

	public static Enum<Integer> numbers( int minBound, int maxBound ) {
		return new EnumerateInteger( minBound, maxBound );
	}

	public static Enum<Character> characters( char minBound, char maxBound ) {
		return new EnumerateCharacter( minBound, maxBound );
	}

	public static <T> Enum<T> stepwise( Enum<T> type, T start, int increment ) {
		return new EnumerateStepwise<T>( type, start, increment );
	}

	private Enumerate() {
		throw new UnsupportedOperationException( "util" );
	}

	public static <E> void validateBounds( Enum<E> type, E e ) {
		final int eOrdinal = type.toOrdinal( e );
		if ( eOrdinal < type.toOrdinal( type.minBound() )
				|| eOrdinal > type.toOrdinal( type.maxBound() ) ) {
			throw new NoSuchElementException( "The type covers " //
					+ type.show( type.minBound() ) + "["
					+ type.toOrdinal( type.minBound() )
					+ "] to " + type.show( type.maxBound() )
					+ "["
					+ type.toOrdinal( type.maxBound() ) + "] but not:" + type.show( e )
					+ "["
					+ type.toOrdinal( e ) + "]" );
		}
	}

	static final class EnumerateStepwise<T>
			implements Enum<T> {

		private final Enum<T> unstepped;
		private final int increment;
		private final int offset;

		EnumerateStepwise( Enum<T> unstepped, T start, int increment ) {
			super();
			this.unstepped = unstepped;
			this.increment = Math.abs( increment );
			this.offset = unstepped.toOrdinal( start ) % this.increment;
		}

		@Override
		public T pred( T value ) {
			return unstepped.toEnum( unstepped.toOrdinal( value ) - increment );
		}

		@Override
		public T succ( T value ) {
			return unstepped.toEnum( unstepped.toOrdinal( value ) + increment );
		}

		@Override
		public T toEnum( int ord ) {
			return unstepped.toEnum( ( ord * increment ) + offset );
		}

		@Override
		public int toOrdinal( T value ) {
			return ( unstepped.toOrdinal( value ) - offset ) / increment;
		}

		@Override
		public T maxBound() {
			final int max = unstepped.toOrdinal( unstepped.maxBound() );
			return unstepped.toEnum( max - ( ( max - offset ) % increment ) );
		}

		@Override
		public T minBound() {
			return unstepped.toEnum( offset );
		}

		@Override
		public String show( T e ) {
			return unstepped.show( e );
		}

	}

	static final class EnumerateEnum<E extends java.lang.Enum<?>>
			implements Enum<E> {

		private final E[] values;

		EnumerateEnum( Class<E> type ) {
			super();
			this.values = type.getEnumConstants();
		}

		@Override
		public E pred( E value ) {
			return values[value.ordinal() - 1];
		}

		@Override
		public E succ( E value ) {
			return values[value.ordinal() + 1];
		}

		@Override
		public E toEnum( int ord ) {
			return values[ord];
		}

		@Override
		public int toOrdinal( E value ) {
			return value.ordinal();
		}

		@Override
		public E maxBound() {
			return values[values.length - 1];
		}

		@Override
		public E minBound() {
			return values[0];
		}

		@Override
		public String show( E e ) {
			return e == null
				? ""
				: e.name();
		}

	}

	static final class EnumerateInteger
			implements Enum<Integer>, Nullsave {

		final int minBound;
		final int maxBound;

		EnumerateInteger( int minBound, int maxBound ) {
			super();
			this.minBound = minBound;
			this.maxBound = maxBound;
		}

		@Override
		public int toOrdinal( Integer value ) {
			return value;
		}

		@Override
		public Integer pred( Integer value ) {
			if ( value == null ) {
				return -1;
			}
			if ( value.intValue() == minBound ) {
				throw new IllegalArgumentException( "No smaller integer than " + value );
			}
			return value - 1;
		}

		@Override
		public Integer succ( Integer value ) {
			if ( value == 0 ) {
				return 1;
			}
			if ( value.intValue() == maxBound ) {
				throw new IllegalArgumentException( "No bigger interger than " + value );
			}
			return value + 1;
		}

		@Override
		public Integer toEnum( int ord ) {
			return ord;
		}

		@Override
		public Integer maxBound() {
			return maxBound;
		}

		@Override
		public Integer minBound() {
			return minBound;
		}

		@Override
		public String show( Integer e ) {
			return e == null
				? "0"
				: e.toString();
		}

	}

	static final class EnumerateCharacter
			implements Enum<Character>, Nullsave {

		private final Character minBound;
		private final Character maxBound;

		EnumerateCharacter( Character minBound, Character maxBound ) {
			super();
			this.minBound = minBound;
			this.maxBound = maxBound;
		}

		@Override
		public Character pred( Character value ) {
			final char c = value == null
				? minBound.charValue()
				: value.charValue();
			return Character.valueOf( (char) ( c - 1 ) );
		}

		@Override
		public Character succ( Character value ) {
			final char c = value == null
				? minBound.charValue()
				: value.charValue();
			return Character.valueOf( (char) ( c + 1 ) );
		}

		@Override
		public Character toEnum( int ord ) {
			return Character.valueOf( (char) ord );
		}

		@Override
		public int toOrdinal( Character value ) {
			return value.charValue();
		}

		@Override
		public Character maxBound() {
			return maxBound;
		}

		@Override
		public Character minBound() {
			return minBound;
		}

		@Override
		public String show( Character e ) {
			return e == null
				? minBound.toString()
				: e.toString();
		}

	}

	static final class EnumerateBoolean
			implements Enum<Boolean>, Nullsave {

		@Override
		public int toOrdinal( Boolean value ) {
			return value == Boolean.TRUE
				? 1
				: 0;
		}

		@Override
		public Boolean pred( Boolean value ) {
			if ( value == Boolean.FALSE ) {
				throw new IllegalArgumentException( "No pred element!" );
			}
			return Boolean.TRUE;
		}

		@Override
		public Boolean succ( Boolean value ) {
			if ( value == Boolean.FALSE ) {
				throw new IllegalArgumentException( "No succ element!" );
			}
			return Boolean.TRUE;
		}

		@Override
		public Boolean toEnum( int ord ) {
			if ( ord < 0 || ord > 1 ) {
				throw new IllegalArgumentException(
						"Just 0 => false and 1 => true are possible but got: " + ord );
			}
			return ord == 0
				? Boolean.FALSE
				: Boolean.TRUE;
		}

		@Override
		public Boolean maxBound() {
			return Boolean.TRUE;
		}

		@Override
		public Boolean minBound() {
			return Boolean.FALSE;
		}

		@Override
		public String show( Boolean e ) {
			return e == Boolean.FALSE
				? e.toString()
				: Boolean.TRUE.toString(); // to be nullsave
		}

	}
}
