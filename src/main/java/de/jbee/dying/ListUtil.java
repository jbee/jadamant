package de.jbee.dying;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.ListIterator;

public final class ListUtil {

	private ListUtil() {
		// util
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

	private static final List<?> EMPTY = new ImmutableList<Object>( Collections.emptyList() );

	public static <T> Iterator<T> reverseIterator( java.util.List<T> list ) {
		return new ReverseIterator<T>( list );
	}

	public static <T> List<T> readonly( java.util.Iterator<T> listIterator ) {
		final java.util.List<T> list = new ArrayList<T>();
		while ( listIterator.hasNext() ) {
			list.add( listIterator.next() );
		}
		return new ImmutableList<T>( list );
	}

	public static <T> List<T> readonly( java.util.List<T> list ) {
		return list == null || list.isEmpty()
			? ListUtil.<T> empty()
			: new ImmutableList<T>( list );
	}

	public static <T> List<T> readonly1( T... list ) {
		return readonly( list );
	}

	public static <T> List<T> readonly( T[] list ) {
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

	public static StringList extend( List<String> list ) {
		return new StringList( list );
	}

	public static <T extends Number & Comparable<T>> NumberList<T> extend( List<T> list ) {
		return new NumberList<T>( list );
	}

	@SuppressWarnings ( "unchecked" )
	public static <T> List<T> empty() {
		return (List<T>) EMPTY;
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
	public static <T> List<T> replicate( int length, T e ) {
		final Object[] list = new Object[length];
		Arrays.fill( list, e );
		return ListUtil.readonly( Arrays.<T> asList( (T[]) list ) );
	}

	public static <T> java.util.List<T> unmodifyable( List<T> list ) {
		final java.util.List<T> res = new ArrayList<T>( list.size() );
		for ( final T e : list ) {
			res.add( e );
		}
		return res;
	}
}
