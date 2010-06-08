package de.jbee.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

final class ImmutableList<T>
		extends AbstractList<T, List<T>> {

	private static final long serialVersionUID = 1L;

	private final List<T> list;

	ImmutableList( List<T> list ) {
		this.list = list;
	}

	@Override
	protected void add( List<T> list, T e ) {
		list.add( e );
	}

	@Override
	protected void addAll( List<T> dest, List<T> src ) {
		dest.addAll( src );
	}

	@Override
	protected T atValidated( int index ) {
		return list.get( index );
	}

	@Override
	protected List<T> clone( int expectedAdditionalSize ) {
		final ArrayList<T> res = new ArrayList<T>( list.size() + expectedAdditionalSize );
		res.addAll( list );
		return res;
	}

	@Override
	protected boolean contains( List<T> mutableCollection, T e ) {
		return mutableCollection.contains( e );
	}

	@Override
	protected List<T> empty( int expectedSize ) {
		return new ArrayList<T>( expectedSize );
	}

	@Override
	protected IList<T> readonly( List<T> innerList ) {
		return new ImmutableList<T>( innerList );
	}

	@Override
	protected Iterator<T> reverseIterator() {
		return de.jbee.util.List.reverseIterator( list );
	}

	@Override
	protected List<T> sublist( int fromInclusive, int toExclusive ) {
		return list.subList( fromInclusive, toExclusive );
	}

	@Override
	public boolean any( T e ) {
		return list.contains( e );
	}

	@Override
	public boolean isEmpty() {
		return list.isEmpty();
	}

	@Override
	public Iterator<T> iterator() {
		return list.iterator();
	}

	@Override
	public int size() {
		return list.size();
	}

	@Override
	public IList<T> sort( Comparator<? super T> c ) {
		final List<T> sortedList = clone( 0 );
		Collections.sort( sortedList, c );
		return readonly( sortedList );
	}

	final List<T> list() {
		return list;
	}

	@Override
	public String toString() {
		return list.toString();
	}
}
