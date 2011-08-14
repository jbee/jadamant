package de.jbee.util;

import java.util.IdentityHashMap;
import java.util.Map;
import java.util.regex.Pattern;

import de.jbee.dying.IDecoder;
import de.jbee.dying.IList;
import de.jbee.dying.IMutableList;
import de.jbee.dying.IMutableSet;
import de.jbee.dying.ISet;
import de.jbee.dying.ListUtil;
import de.jbee.dying.Set;

/**
 * Acts as a Service Locator for {@link IDecoder}s. The target type of decoding is used as key for
 * the locator.
 * 
 * @author Jan Bernitt (jan.bernitt@gmx.de)
 */
public final class Decoder {

	private static final IDecoder<?> UNSUPPORTED = new UnsupportedDecoder();

	private static final Map<Class<?>, IDecoder<?>> INSTANCES = new IdentityHashMap<Class<?>, IDecoder<?>>();

	static {
		IDecoder<?> d = new IntegerDecoder();
		INSTANCES.put( int.class, d );
		INSTANCES.put( Integer.class, d );
		INSTANCES.put( short.class, d );
		INSTANCES.put( Short.class, d );
		INSTANCES.put( byte.class, d );
		INSTANCES.put( Byte.class, d );
		d = new LongDecoder();
		INSTANCES.put( long.class, d );
		INSTANCES.put( Long.class, d );
		d = new FloatDecoder();
		INSTANCES.put( float.class, d );
		INSTANCES.put( Float.class, d );
		d = new DoubleDecoder();
		INSTANCES.put( double.class, d );
		INSTANCES.put( Double.class, d );
		d = new StringDecoder();
		INSTANCES.put( String.class, d );
		d = new CharacterDecoder();
		INSTANCES.put( char.class, d );
		INSTANCES.put( Character.class, d );
		d = new PatternDecoder();
		INSTANCES.put( Pattern.class, d );
	}

	public static <T> void register( IDecoder<T> decoder, Class<? super T> type ) {
		INSTANCES.put( type, decoder );
	}

	public static <T> void register( IDecoder<T> decoder, Class<? super T> first,
			Class<? super T> second ) {
		register( decoder, first );
		register( decoder, second );
	}

	public static <T> void register( IDecoder<T> decoder, Class<? super T> first,
			Class<? super T> second, Class<? super T> thrid ) {
		register( decoder, first );
		register( decoder, second );
		register( decoder, thrid );
	}

	private Decoder() {
		// util
	}

	public static <T> IDecoder<T> getInstance( Class<? extends T> simpleType ) {
		return getInstance( simpleType, false );
	}

	@SuppressWarnings ( "unchecked" )
	public static <T> IDecoder<T> getInstance( Class<? extends T> simpleType, boolean required ) {
		if ( simpleType.isEnum() ) {
			return new EnumDecoder( simpleType );
		}
		final IDecoder<?> res = INSTANCES.get( simpleType );
		if ( res == null ) {
			if ( required ) {
				throw new UnsupportedOperationException( "No decoder available for type: "
						+ simpleType );
			}
			return (IDecoder<T>) UNSUPPORTED;
		}
		return (IDecoder<T>) res;
	}

	@SuppressWarnings ( "unchecked" )
	public static <T, E> IDecoder<T> getCollectionInstance( Class<? extends T> collectionType,
			Class<E> elementType ) {
		final IDecoder<E> elementDecoder = getInstance( elementType, true );
		if ( IList.class.isAssignableFrom( collectionType ) ) {
			return (IDecoder<T>) new ListDecoder<E>( elementDecoder, "," );
		}
		if ( ISet.class.isAssignableFrom( collectionType ) ) {
			return (IDecoder<T>) new SetDecoder<E>( elementDecoder, ",", elementType );
		}
		throw new UnsupportedOperationException( "No decoder available for type: " + collectionType );
	}

	static final class UnsupportedDecoder
			implements IDecoder<Object> {

		@Override
		public Object decode( String value ) {
			throw new UnsupportedOperationException( "no decoder available!" );
		}
	}

	static final class LongDecoder
			implements IDecoder<Long> {

		@Override
		public Long decode( String value ) {
			return Long.decode( value );
		}
	}

	static final class IntegerDecoder
			implements IDecoder<Integer> {

		@Override
		public Integer decode( String value ) {
			return Integer.decode( value );
		}
	}

	static final class FloatDecoder
			implements IDecoder<Float> {

		@Override
		public Float decode( String value ) {
			return Float.valueOf( value );
		}
	}

	static final class DoubleDecoder
			implements IDecoder<Double> {

		@Override
		public Double decode( String value ) {
			return Double.valueOf( value );
		}
	}

	static final class BooleanDecoder
			implements IDecoder<Boolean> {

		@Override
		public Boolean decode( String value ) {
			return Boolean.valueOf( value );
		}
	}

	static final class StringDecoder
			implements IDecoder<String> {

		@Override
		public String decode( String value ) {
			return value;
		}
	}

	static final class PatternDecoder
			implements IDecoder<Pattern> {

		@Override
		public Pattern decode( String value ) {
			return Pattern.compile( value );
		}
	}

	static final class CharacterDecoder
			implements IDecoder<Character> {

		@Override
		public Character decode( String value ) {
			return value == null || value.isEmpty()
				? null
				: Character.valueOf( value.charAt( 0 ) );
		}
	}

	static final class EnumDecoder<E extends Enum<E>>
			implements IDecoder<E> {

		private final Class<E> enumType;

		public EnumDecoder( Class<E> enumType ) {
			this.enumType = enumType;
		}

		@Override
		public E decode( String value ) {
			return Enum.valueOf( enumType, value );
		}
	}

	static abstract class CollectionDecoder<E, C, R>
			implements IDecoder<R> {

		private final IDecoder<E> elementDecoder;
		private final String delimiterRegEx;

		protected CollectionDecoder( IDecoder<E> elementDecoder, String delimiterRegEx ) {
			this.elementDecoder = elementDecoder;
			this.delimiterRegEx = delimiterRegEx;
		}

		@Override
		public final R decode( String value ) {
			value = value.trim();
			if ( value.isEmpty() ) {
				return empty();
			}
			if ( value.startsWith( "[" ) && value.endsWith( "]" ) ) {
				value = value.substring( 1, value.length() - 1 );
			}
			final String[] elements = value.split( delimiterRegEx );
			if ( elements.length == 0 ) {
				return empty();
			}
			final C collection = empty( elements.length );
			for ( final String e : elements ) {
				append( elementDecoder.decode( e ), collection );
			}
			return readonly( collection );
		}

		protected abstract R empty();

		protected abstract C empty( int size );

		protected abstract void append( E element, C collection );

		protected abstract R readonly( C collection );
	}

	static final class ListDecoder<E>
			extends CollectionDecoder<E, IMutableList<E>, IList<E>> {

		ListDecoder( IDecoder<E> elementDecoder, String delimiterRegEx ) {
			super( elementDecoder, delimiterRegEx );
		}

		@Override
		protected void append( E element, IMutableList<E> collection ) {
			collection.append( element );
		}

		@Override
		protected IList<E> readonly( IMutableList<E> collection ) {
			return collection.immutable();
		}

		@Override
		protected IList<E> empty() {
			return ListUtil.empty();
		}

		@Override
		protected IMutableList<E> empty( int size ) {
			return ListUtil.mutable( size );
		}
	}

	static final class SetDecoder<E>
			extends CollectionDecoder<E, IMutableSet<E>, ISet<E>> {

		private final Class<E> elementType;

		SetDecoder( IDecoder<E> elementDecoder, String delimiterRegEx, Class<E> elementType ) {
			super( elementDecoder, delimiterRegEx );
			this.elementType = elementType;
		}

		@Override
		protected void append( E element, IMutableSet<E> collection ) {
			collection.add( element );
		}

		@Override
		protected ISet<E> empty() {
			return de.jbee.dying.Set.empty();
		}

		@Override
		protected IMutableSet<E> empty( int size ) {
			return Set.mutable( size, elementType );
		}

		@Override
		protected ISet<E> readonly( IMutableSet<E> collection ) {
			return collection.immutable();
		}

	}

	public static <T> T decode( String value, IDecoder<T> decoder, T errorValue,
			IOutputStream errorOut ) {
		try {
			return decoder.decode( value );
		} catch ( final RuntimeException e ) {
			if ( errorOut != null ) {
				errorOut.write( "Error during decode of value `" + value + "`" );
			}
			return errorValue;
		}
	}
}
