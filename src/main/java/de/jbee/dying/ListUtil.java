package de.jbee.dying;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.ListIterator;

import de.jbee.dying.Collection.MutableCollection;

public final class ListUtil {

	private ListUtil() {
		// util
	}

	static final class MutableList<T>
			extends MutableCollection<T, ArrayList<T>>
			implements IMutableList<T> {

		public MutableList( ArrayList<T> list ) {
			super( list );
		}

		@SuppressWarnings ( "unchecked" )
		@Override
		public IList<T> immutable() {
			// to be able to continue work with the mutable list without changing the immutable created 
			return ListUtil.readonly( (ArrayList<T>) getCollection().clone() );
		}

		@Override
		public IMutableList<T> append( T e ) {
			getCollection().add( e );
			return this;
		}

		@Override
		public IMutableList<T> insert( int index, T e ) {
			getCollection().add( index, e );
			return this;
		}

		@Override
		public IMutableList<T> append( ICluster<T> cluster ) {
			ArrayList<T> list = getCollection();
			for ( T e : cluster ) {
				list.add( e );
			}
			return this;
		}

	}

	static final class ReverseIterator<T>
			implements Iterator<T> {

		private final ListIterator<T> iterator;

		ReverseIterator( java.util.List<T> list ) {
			this.iterator = list.listIterator( list.size() - 1 );
		}

		@Override
		public boolean hasNext() {
			return iterator.hasPrevious();
		}

		@Override
		public T next() {
			return iterator.previous();
		}

		@Override
		public void remove() {
			iterator.remove();
		}
	}

	private static final IList<?> EMPTY = new ImmutableList<Object>( Collections.emptyList() );

	public static <T> Iterator<T> reverseIterator( java.util.List<T> list ) {
		return new ReverseIterator<T>( list );
	}

	public static <T> IList<T> readonly( java.util.Iterator<T> listIterator ) {
		final java.util.List<T> list = new ArrayList<T>();
		while ( listIterator.hasNext() ) {
			list.add( listIterator.next() );
		}
		return new ImmutableList<T>( list );
	}

	public static <T> IList<T> readonly( java.util.List<T> list ) {
		return list == null || list.isEmpty()
			? ListUtil.<T> empty()
			: new ImmutableList<T>( list );
	}

	public static <T> IList<T> readonly1( T... list ) {
		return readonly( list );
	}

	public static <T> IList<T> readonly( T[] list ) {
		return list == null || list.length == 0
			? ListUtil.<T> empty()
			: ListUtil.readonly( Arrays.asList( list ) );
	}

	public static StringList readonly( java.util.List<String> list ) {
		return new StringList( new ImmutableList<String>( list ) );
	}

	public static StringList readonly( String[] list ) {
		return list == null || list.length == 0
			? extend( ListUtil.<String> empty() )
			: ListUtil.readonly( Arrays.asList( list ) );
	}

	public static <T extends Number & Comparable<T>> NumberList<T> readonly( java.util.List<T> list ) {
		return new NumberList<T>( new ImmutableList<T>( list ) );
	}

	public static StringList extend( IList<String> list ) {
		return new StringList( list );
	}

	public static <T extends Number & Comparable<T>> NumberList<T> extend( IList<T> list ) {
		return new NumberList<T>( list );
	}

	@SuppressWarnings ( "unchecked" )
	public static <T> IList<T> empty() {
		return (IList<T>) EMPTY;
	}

	/**
	 * @see IMutableList
	 */
	public static <T> IMutableList<T> mutable( IList<T> list ) {
		if ( list.isEmpty() ) {
			return new MutableList<T>( new ArrayList<T>() );
		}
		final ArrayList<T> mutable = new ArrayList<T>( list.size() );
		for ( final T e : list ) {
			mutable.add( e );
		}
		return new MutableList<T>( mutable );
	}

	public static <T> IMutableList<T> mutable( int size ) {
		return new MutableList<T>( new ArrayList<T>( size ) );
	}

	/**
	 * creates an array of string from the original one, white space characters serving as
	 * separators.
	 * 
	 * <pre>
	 * Input: words "aa bb cc \t dd \n ee"
	 * Output: ["aa","bb","cc","dd","ee"]
	 * </pre>
	 */
	public static StringList words( String words ) {
		return split( words, "\\s+" );
	}

	public static StringList split( String list, String splitRegex ) {
		return ListUtil.readonly( Arrays.asList( list.split( splitRegex ) ) );
	}

	/**
	 * creates a list of length given by the first argument and the items having value of the second
	 * argument
	 */
	@SuppressWarnings ( "unchecked" )
	public static <T> IList<T> replicate( int length, T e ) {
		final Object[] list = new Object[length];
		Arrays.fill( list, e );
		return ListUtil.readonly( Arrays.<T> asList( (T[]) list ) );
	}

	public static <T> java.util.List<T> unmodifyable( IList<T> list ) {
		final java.util.List<T> res = new ArrayList<T>( list.size() );
		for ( final T e : list ) {
			res.add( e );
		}
		return res;
	}
}
