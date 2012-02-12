package de.jbee.data;

import de.jbee.data.DataProperty.ObjectProperty;
import de.jbee.data.DataProperty.RangeProperty;
import de.jbee.data.DataProperty.ValueProperty;
import de.jbee.lang.List;
import de.jbee.lang.ListIndex;
import de.jbee.lang.Map;
import de.jbee.lang.Ord;
import de.jbee.lang.Order;
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
		public <S> Data<S> object( ObjectProperty<? super T, S> property ) {
			return property.resolveIn( prefix, this );
		}

		@Override
		public <S> List<Data<S>> subs( RangeProperty<? super T, S> path ) {
			//The list actually has to resolve the result list using this objects data because there can be any kind of selection applied - this object cannot and shouln't know about the way the list is computed. 

			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public <V> V value( ValueProperty<? super T, V> property ) {
			return property.resolveIn( prefix, this );
		}

		@Override
		public boolean isEmpty() {
			return false;
		}

		@Override
		public int indexFor( Key key ) {
			return properties.indexFor( key ) - start; //TODO just search between start and end
		}

		@Override
		public int length() {
			return end - start;
		}

		@Override
		public Object at( int index ) {
			return properties.at( start + index ).value();
		}

		@Override
		public Data<Object> slice( Path prefix, int start, int end ) {
			if ( start >= end ) {
				return empty();
			}
			return new ObjectData<Object>( prefix, start, end, properties );
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
		public Ord<Object> order() {
			return properties.order();
		}
	}

	static final class NoData<T>
			implements Data<T>, Table<Object> {

		@Override
		public <S> Data<S> object( ObjectProperty<? super T, S> property ) {
			return empty();
		}

		@Override
		public <S> List<Data<S>> subs( RangeProperty<? super T, S> path ) {
			return List.with.noElements();
		}

		@Override
		public <V> V value( ValueProperty<? super T, V> property ) {
			return property.resolveIn( Path.ROOT, this );
		}

		@Override
		public String toString() {
			return "[nothing]";
		}

		@Override
		public boolean isEmpty() {
			return true;
		}

		@Override
		public int indexFor( Key key ) {
			return ListIndex.NOT_CONTAINED;
		}

		@Override
		public int length() {
			return 0;
		}

		@Override
		public Object at( int index )
				throws IndexOutOfBoundsException {
			return List.with.noElements().at( index );
		}

		@Override
		public Ord<Object> order() {
			return Order.inherent;
		}

	}
}