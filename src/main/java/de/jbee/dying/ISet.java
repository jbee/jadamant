package de.jbee.dying;

import java.util.Comparator;

public interface ISet<T>
		extends IBag<T, ISet<T>> {

	/**
	 * @see #any(Object)
	 */
	boolean member( T e );

	/**
	 * @see #any(Object)
	 */
	boolean noMember( T e );

	boolean isSubsetOf( ISet<T> other );

	List<T> toList();

	ISet<T> unions( ICluster<ISet<T>> others );

	List<T> toAscList( Comparator<? super T> c );

	IMutableSet<T> mutable();
}
