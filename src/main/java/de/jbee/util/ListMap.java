package de.jbee.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public final class ListMap {

	private ListMap() {
		// util
	}

	static final class ClassicListMap<K, V>
			implements IListMap<K, V> {

		final Map<K, IMutableList<V>> lists = new HashMap<K, IMutableList<V>>();

		@Override
		public IListMap<K, V> append( K key, V value ) {
			IMutableList<V> list = lists.get( key );
			if ( list == null ) {
				list = List.mutable( 5 );
				lists.put( key, list );
			}
			list.append( value );
			return this;
		}

		@Override
		public ISet<K> keys() {
			return Set.readonly( lists.keySet() );
		}

		@Override
		public IList<V> values( K key ) {
			IMutableList<V> list = lists.get( key );
			return list == null
				? List.<V> empty()
				: list.immutable();
		}

		@Override
		public Iterator<IList<V>> iterator() {
			return new Iterator<IList<V>>() {

				Iterator<IMutableList<V>> iterator = lists.values().iterator();

				@Override
				public boolean hasNext() {
					return iterator.hasNext();
				}

				@Override
				public IList<V> next() {
					return iterator.next().immutable();
				}

				@Override
				public void remove() {
					iterator.remove();
				}
			};
		}

		@Override
		public IListMap<K, V> appendEach( V value ) {
			return appendEach( Fulfills.always(), value );
		}

		@Override
		public IListMap<K, V> appendEach( ICondition<? super K> keyCondition, V value ) {
			for ( Entry<K, IMutableList<V>> e : lists.entrySet() ) {
				if ( keyCondition.fulfilledBy( e.getKey() ) ) {
					e.getValue().append( value );
				}
			}
			return this;
		}

		@Override
		public IList<V> merge() {
			IMutableList<V> res = List.mutable( size() );
			for ( IMutableList<V> l : lists.values() ) {
				res.append( l );
			}
			return res.immutable();
		}

		@Override
		public int size() {
			int c = 0;
			for ( IMutableList<V> l : lists.values() ) {
				c += l.size();
			}
			return c;
		}
	}

}
