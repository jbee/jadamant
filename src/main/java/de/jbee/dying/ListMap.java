package de.jbee.dying;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import de.jbee.lang.Is;
import de.jbee.lang.List;
import de.jbee.lang.Predicate;

public final class ListMap {

	private ListMap() {
		// util
	}

	public static <K, V> IListMap<K, V> empty() {
		return new ClassicListMap<K, V>();
	}

	static final class ClassicListMap<K, V>
			implements IListMap<K, V> {

		final Map<K, List<V>> lists = new HashMap<K, List<V>>();

		@Override
		public IListMap<K, V> append( K key, V value ) {
			List<V> list = lists.get( key );
			if ( list == null ) {
				list = List.with.element( value );
			} else {
				list = list.append( value );
			}
			lists.put( key, list );
			return this;
		}

		@Override
		public List<K> keys() {
			return List.with.readonly( lists.keySet() );
		}

		@Override
		public de.jbee.lang.List<V> values( K key ) {
			List<V> list = lists.get( key );
			return list == null
				? List.with.<V> noElements()
				: list;
		}

		@Override
		public Iterator<List<V>> iterator() {
			return new Iterator<List<V>>() {

				Iterator<List<V>> iterator = lists.values().iterator();

				@Override
				public boolean hasNext() {
					return iterator.hasNext();
				}

				@Override
				public List<V> next() {
					return iterator.next();
				}

				@Override
				public void remove() {
					iterator.remove();
				}
			};
		}

		@Override
		public IListMap<K, V> appendEach( V value ) {
			return appendEach( Is.true_(), value );
		}

		@Override
		public IListMap<K, V> appendEach( Predicate<? super K> keyCondition, V value ) {
			for ( K key : lists.keySet() ) {
				if ( keyCondition.is( key ) ) {
					lists.put( key, lists.get( key ).append( value ) );
				}
			}
			return this;
		}

		@Override
		public List<V> merge() {
			List<V> res = List.with.noElements();
			for ( List<V> l : lists.values() ) {
				res = res.concat( l );
			}
			return List.which.nubs().from( res );
		}

		@Override
		public int size() {
			int c = 0;
			for ( List<V> l : lists.values() ) {
				c += l.length();
			}
			return c;
		}

		@Override
		public String toString() {
			return lists.toString();
		}
	}

}
