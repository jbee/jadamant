package de.jbee.util;

import java.util.Collections;
import java.util.EnumSet;
import java.util.HashSet;

import de.jbee.util.Collection.MutableCollection;

public final class Set {

	private Set() {
		// util
	}

	static final class MutableSet<T>
			extends MutableCollection<T, java.util.Set<T>>
			implements IMutableSet<T> {

		MutableSet( java.util.Set<T> set ) {
			super( set );
		}

		@Override
		public IMutableSet<T> add( T e ) {
			getCollection().add( e );
			return this;
		}

		@Override
		public ISet<T> immutable() {
			return readonly( Set.clone( getCollection() ) );
		}
	}

	private static final ISet<?> EMPTY = new ImmutableSet<Object>( Collections.emptySet() );

	@SuppressWarnings ( "unchecked" )
	public static <T> ISet<T> empty() {
		return (ISet<T>) EMPTY;
	}

	public static <T> IMutableSet<T> mutable( ISet<T> set ) {
		if ( set instanceof ImmutableSet<?> ) {
			@SuppressWarnings ( "unchecked" )
			final java.util.Set<T> clone = (java.util.Set<T>) ( (ImmutableSet<?>) set ).clone( 0 );
			return new MutableSet<T>( clone );
		}
		final HashSet<T> mutable = new HashSet<T>( set.size() );
		for ( final T e : set ) {
			mutable.add( e );
		}
		return new MutableSet<T>( mutable );
	}

	@SuppressWarnings ( "unchecked" )
	public static <T> IMutableSet<T> mutable( int size, Class<T> elementType ) {
		return ( elementType.isEnum() )
			? new MutableSet<T>( (java.util.Set<T>) mutable( elementType ) )
			: new MutableSet<T>( new HashSet<T>( size ) );
	}

	@SuppressWarnings ( "unchecked" )
	private static <T> java.util.Set<T> mutable( Class enumType ) {
		return EnumSet.noneOf( enumType );
	}

	public static <T> ISet<T> readonly( java.util.Set<T> set ) {
		return new ImmutableSet<T>( set );
	}

	public static <T> ISet<T> readonly( T[] elements ) {
		if ( elements == null || elements.length == 0 ) {
			return empty();
		}
		final java.util.Set<T> set = new HashSet<T>();
		for ( final T e : elements ) {
			set.add( e );
		}
		return readonly( set );
	}

	public static <T extends Enum<T>> ISet<T> readonly( T... elements ) {
		return elements == null || elements.length == 0
			? Set.<T> empty()
			: readonly( EnumSet.of( elements[0], elements ) );
	}

	@SuppressWarnings ( "unchecked" )
	static <T> java.util.Set<T> clone( java.util.Collection<T> c ) {
		if ( c instanceof EnumSet<?> ) {
			return ( (EnumSet) c ).clone();
		}
		return new HashSet<T>( c );
	}
}
