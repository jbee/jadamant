package de.jbee.util;

import java.util.IdentityHashMap;
import java.util.Map;
import java.util.regex.Pattern;

import de.jbee.dying.IEncoder;

public final class Encoder {

	private static final IEncoder<?> UNSUPPORTED = new UnsupportedEncoder();

	private static final Map<Class<?>, IEncoder<?>> INSTANCES = new IdentityHashMap<Class<?>, IEncoder<?>>();

	static {
		IEncoder<?> d = new IntegerEncoder();
		INSTANCES.put( int.class, d );
		INSTANCES.put( Integer.class, d );
		INSTANCES.put( short.class, d );
		INSTANCES.put( Short.class, d );
		INSTANCES.put( byte.class, d );
		INSTANCES.put( Byte.class, d );
		d = new LongEncoder();
		INSTANCES.put( long.class, d );
		INSTANCES.put( Long.class, d );
		d = new FloatEncoder();
		INSTANCES.put( float.class, d );
		INSTANCES.put( Float.class, d );
		d = new DoubleEncoder();
		INSTANCES.put( double.class, d );
		INSTANCES.put( Double.class, d );
		d = new StringEncoder();
		INSTANCES.put( String.class, d );
		d = new CharacterEncoder();
		INSTANCES.put( char.class, d );
		INSTANCES.put( Character.class, d );
		d = new PatternEncoder();
		INSTANCES.put( Pattern.class, d );
	}

	public static <T> void register( final IEncoder<T> encoder, final Class<? super T> type ) {
		INSTANCES.put( type, encoder );
	}

	public static <T> void register( final IEncoder<T> encoder, final Class<? super T> first,
			final Class<? super T> second ) {
		register( encoder, first );
		register( encoder, second );
	}

	public static <T> void register( final IEncoder<T> encoder, final Class<? super T> first,
			final Class<? super T> second, final Class<? super T> thrid ) {
		register( encoder, first );
		register( encoder, second );
		register( encoder, thrid );
	}

	private Encoder() {
		// util
	}

	public static <T> IEncoder<T> getInstance( final Class<? extends T> simpleType ) {
		return getInstance( simpleType, false );
	}

	@SuppressWarnings ( "unchecked" )
	public static <T> IEncoder<T> getInstance( final Class<? extends T> simpleType,
			final boolean required ) {
		if ( simpleType.isEnum() ) {
			return new EnumEncoder();
		}
		final IEncoder<?> res = INSTANCES.get( simpleType );
		if ( res == null ) {
			if ( required ) {
				throw new UnsupportedOperationException( "No encoder available for type: "
						+ simpleType );
			}
			return (IEncoder<T>) UNSUPPORTED;
		}
		return (IEncoder<T>) res;
	}

	static final class UnsupportedEncoder
			implements IEncoder<Object> {

		@Override
		public String encode( final Object value ) {
			throw new UnsupportedOperationException( "no encoder available for: "
					+ value.getClass().getCanonicalName() );
		}
	}

	static final class LongEncoder
			implements IEncoder<Long> {

		@Override
		public String encode( final Long value ) {
			return value.toString();
		}
	}

	static final class IntegerEncoder
			implements IEncoder<Integer> {

		@Override
		public String encode( final Integer value ) {
			return value.toString();
		}
	}

	static final class FloatEncoder
			implements IEncoder<Float> {

		@Override
		public String encode( final Float value ) {
			return value.toString();
		}
	}

	static final class DoubleEncoder
			implements IEncoder<Double> {

		@Override
		public String encode( final Double value ) {
			return value.toString();
		}
	}

	static final class BooleanEncoder
			implements IEncoder<Boolean> {

		@Override
		public String encode( final Boolean value ) {
			return value.toString();
		}
	}

	static final class StringEncoder
			implements IEncoder<String> {

		@Override
		public String encode( final String value ) {
			return value;
		}
	}

	static final class PatternEncoder
			implements IEncoder<Pattern> {

		@Override
		public String encode( final Pattern value ) {
			return value.toString();
		}
	}

	static final class CharacterEncoder
			implements IEncoder<Character> {

		@Override
		public String encode( final Character value ) {
			return value.toString();
		}
	}

	static final class EnumEncoder<E extends Enum<E>>
			implements IEncoder<E> {

		@Override
		public String encode( final E value ) {
			return value.toString();
		}
	}

	public static <T> String encode( final T value, final IEncoder<T> encoder,
			final IOutputStream errorOut ) {
		try {
			return encoder.encode( value );
		} catch ( final RuntimeException e ) {
			if ( errorOut != null ) {
				errorOut.write( "Error during encode of value `" + value + "`" );
			}
			return null;
		}
	}
}
