package de.jbee.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public abstract class AbstractSet<T, M extends Iterable<T>>
		extends AbstractBag<T, ISet<T>, M>
		implements ISet<T> {

	@Override
	public final boolean member( T e ) {
		return any( e );
	}

	@Override
	public final boolean noMember( T e ) {
		return !any( e );
	}

	@Override
	public final int countBy( IEquality<? super T> equality, T e ) {
		return anyBy( equality, e )
			? 1
			: 0;
	}

	@Override
	public final int count( T e ) {
		return any( e )
			? 1
			: 0;
	}

	@Override
	protected final ISet<T> self() {
		return this;
	}

	@Override
	protected final ISet<T> empty() {
		return de.jbee.util.Set.empty();
	}

	@Override
	public final boolean isSubsetOf( ISet<T> other ) {
		return other.allOf( self() );
	}

	@Override
	public IList<T> toAscList( Comparator<? super T> c ) {
		final ArrayList<T> list = toStandardList();
		Collections.sort( list, c );
		return List.readonly( list );
	}

	@Override
	public IList<T> toList() {
		return List.readonly( toStandardList() );
	}

	private ArrayList<T> toStandardList() {
		final ArrayList<T> list = new ArrayList<T>( size() );
		for ( final T e : this ) {
			list.add( e );
		}
		return list;
	}

	@Override
	public ISet<T> unions( ICluster<ISet<T>> others ) {
		int size = size();
		for ( final ISet<T> s : others ) {
			size += s.size();
		}
		final M set = empty( size );
		for ( final ISet<T> s : others ) {
			for ( final T e : s ) {
				add( set, e );
			}
		}
		return readonly( set );
	}
}
