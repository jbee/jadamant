package de.jbee.dying;

import java.util.EnumSet;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

final class ImmutableSet<T>
		extends AbstractSet<T, Set<T>> {

	private final Set<T> set;

	ImmutableSet( Set<T> set ) {
		this.set = set;
	}

	@Override
	protected void add( Set<T> mutableCollection, T e ) {
		mutableCollection.add( e );
	}

	@Override
	protected void addAll( Set<T> dest, Set<T> src ) {
		dest.addAll( src );
	}

	@SuppressWarnings ( "unchecked" )
	@Override
	protected Set<T> clone( int expectedAdditionalSize ) {
		if ( set instanceof EnumSet<?> ) {
			return (Set<T>) ( (EnumSet<?>) set ).clone();
		}
		final Set<T> clone = new HashSet<T>( set.size() + expectedAdditionalSize );
		clone.addAll( set );
		return clone;
	}

	@Override
	protected boolean contains( Set<T> mutableCollection, T e ) {
		return mutableCollection.contains( e );
	}

	@Override
	protected Set<T> empty( int expectedSize ) {
		return new HashSet<T>( expectedSize );
	}

	@Override
	protected ISet<T> readonly( Set<T> set ) {
		return new ImmutableSet<T>( set );
	}

	@Override
	public boolean any( T e ) {
		return set.contains( e );
	}

	@Override
	public boolean isEmpty() {
		return set.isEmpty();
	}

	@Override
	public Iterator<T> iterator() {
		return set.iterator();
	}

	@Override
	public int size() {
		return set.size();
	}

	@Override
	public String toString() {
		return set.toString();
	}

	@Override
	public IMutableSet<T> mutable() {
		// TODO Auto-generated method stub
		return null;
	}

}
