package de.jbee.data;

import de.jbee.data.DataProperty.ObjectProperty;
import de.jbee.data.DataProperty.RangeProperty;
import de.jbee.data.DataProperty.ValueProperty;
import de.jbee.lang.List;
import de.jbee.lang.ListIndex;
import de.jbee.lang.Map;
import de.jbee.lang.Ord;
import de.jbee.lang.Order;
import de.jbee.lang.Sequence;
import de.jbee.lang.Table;
import de.jbee.lang.Map.Entry;
import de.jbee.lang.Map.Key;

public class MapData {

	private static final Data<?> EMPTY = new NoData<Object>();

	@SuppressWarnings ( "unchecked" )
	static <T> Data<T> empty() {
		return (Data<T>) EMPTY;
	}

	static <T> Data<T> object( Map<Object> properties ) {
		return new ObjectData<T>( Path.ROOT, 0, properties.length(), properties );
	}

	static final class NoData<T>
			implements Data<T>, Table<Object> {

		@Override
		public Object at( int index )
				throws IndexOutOfBoundsException {
			return List.with.noElements().at( index );
		}

		@Override
		public int indexFor( Key key ) {
			return ListIndex.NOT_CONTAINED;
		}

		@Override
		public int indexFor( Key key, int startInclusive, int endExclusive ) {
			return ListIndex.NOT_CONTAINED;
		}

		@Override
		public boolean isEmpty() {
			return true;
		}

		@Override
		public int length() {
			return 0;
		}

		@Override
		public <S> Data<S> object( ObjectProperty<? super T, S> property ) {
			return empty();
		}

		@Override
		public Ord<Object> order() {
			return Order.inherent;
		}

		@Override
		public <S> Data<S> objects( RangeProperty<? super T, S> property ) {
			return empty();
		}

		@Override
		public String toString() {
			return "[nothing]";
		}

		@Override
		public <V> V value( ValueProperty<? super T, V> property ) {
			return property.resolveIn( Path.ROOT, this );
		}

		@Override
		public Sequence<Data<T>> each() {
			return List.with.noElements();
		}

	}

	/**
	 * <pre>
	 * .class CDBox
	 * disc1..class CD
	 * disc1.title 'Worst of ... CD1'
	 * </pre>
	 * 
	 * @author Jan Bernitt (jan.bernitt@gmx.de)
	 * 
	 */
	static final class ObjectData<T>
			implements Data<T>, Table<Object>, Data.DataTable<Object> {

		private final Path prefix;
		private final int start;
		private final int end;
		private final Map<Object> properties;

		ObjectData( Path prefix, int start, int end, Map<Object> properties ) {
			super();
			this.prefix = prefix;
			this.start = start;
			this.end = end;
			this.properties = properties;
		}

		@Override
		public Object at( int index ) {
			return properties.at( start + index ).value();
		}

		@Override
		public int indexFor( Key key ) {
			return properties.indexFor( key, start, end ) - start;
		}

		@Override
		public int indexFor( Key key, int startInclusive, int endExclusive ) {
			final int low = Math.min( start + startInclusive, end );
			final int high = Math.min( start + endExclusive, end );
			return properties.indexFor( key, low, high ) - start;
		}

		@Override
		public boolean isEmpty() {
			return false;
		}

		@Override
		public int length() {
			return end - start;
		}

		@Override
		public <S> Data<S> object( ObjectProperty<? super T, S> property ) {
			return property.resolveIn( prefix, this );
		}

		@Override
		public Ord<Object> order() {
			return properties.order();
		}

		@Override
		public Data<Object> slice( Path prefix, int startInclusive, int endExclusive ) {
			final int low = Math.min( start + startInclusive, end );
			final int high = Math.min( start + endExclusive, end );
			if ( low >= high ) {
				return empty();
			}
			return new ObjectData<Object>( prefix, low, high, properties );
		}

		@Override
		public <S> Data<S> objects( RangeProperty<? super T, S> property ) {
			return property.resolveIn( prefix, this );
		}

		@Override
		public String toString() {
			StringBuilder b = new StringBuilder();
			b.append( prefix );
			for ( int i = start; i < end; i++ ) {
				b.append( '\n' );
				Entry<Object> e = properties.at( i );
				b.append( String.format( "%-20s => %s", e.key(), e.value() ) );
			}
			return b.toString();
		}

		@Override
		public <V> V value( ValueProperty<? super T, V> property ) {
			return property.resolveIn( prefix, this );
		}

		@Override
		public Sequence<Data<T>> each() {
			return List.with.<Data<T>> element( this );
		}
	}
}