package de.jbee.util;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;

public final class Collection {

	private Collection() {
		// util
	}

	static abstract class MutableCollection<T, C extends java.util.Collection<T>>
			implements ICluster<T> {

		private final C collection;

		public MutableCollection( C collection ) {
			super();
			this.collection = collection;
		}

		protected final C getCollection() {
			return collection;
		}

		@Override
		public final boolean isEmpty() {
			return collection.isEmpty();
		}

		@Override
		public final int size() {
			return collection.size();
		}

		@Override
		public final Iterator<T> iterator() {
			return collection.iterator();
		}

		@Override
		public String toString() {
			return collection.toString();
		}

	}

	static final class CompareableComparator<T extends Comparable<T>>
			implements Comparator<T> {

		@Override
		public int compare( T o1, T o2 ) {
			if ( o1 == null ) {
				return o2 == null
					? 0
					: -1;
			}
			if ( o2 == null ) {
				return 1;
			}
			return o1.compareTo( o2 );
		}
	}

	public static <T> boolean isEmpty( T[] array ) {
		return array == null || array.length == 0;
	}

	public static <T> T[] asArray( ICluster<T> cluster, Class<T> elementType ) {
		if ( cluster.isEmpty() ) {
			return null;
		}
		final T[] res = newArray( elementType, cluster.size() );
		final Iterator<T> iter = cluster.iterator();
		for ( int i = 0; i < res.length; i++ ) {
			res[i] = iter.next();
		}
		return res;
	}

	public static <T> java.util.Collection<T> asCollection( ICluster<T> cluster ) {
		ArrayList<T> res = new ArrayList<T>( cluster.size() );
		for ( T e : cluster ) {
			res.add( e );
		}
		return res;
	}

	@SuppressWarnings ( "unchecked" )
	public static <T> T[] newArray( Class<T> elementType, int length ) {
		return (T[]) Array.newInstance( elementType, length );
	}

	public static <T extends Comparable<T>> Comparator<T> comparator() {
		return new CompareableComparator<T>();
	}
}
