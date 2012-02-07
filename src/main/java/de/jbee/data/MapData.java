package de.jbee.data;

import de.jbee.data.DataProperty.ObjectProperty;
import de.jbee.data.DataProperty.RangeProperty;
import de.jbee.data.DataProperty.ValueProperty;
import de.jbee.lang.List;
import de.jbee.lang.ListIndex;
import de.jbee.lang.Map;
import de.jbee.lang.Table;
import de.jbee.lang.Map.Key;

public class MapData {

	private static final Data<?> EMPTY = new NoData<Object>();

	@SuppressWarnings ( "unchecked" )
	static <T> Data<T> empty() {
		return (Data<T>) EMPTY;
	}

	static final class Type {

		final Class<?> type;
		final int length;

		Type( Class<?> type, int length ) {
			super();
			assert ( length >= 0 );
			this.type = type;
			this.length = length;
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
			implements Data<T>, Table<Object> {

		private final ObjectProperty<?, T> parent;
		private final int start;
		private final int end;
		private final Map<Object> properties;

		ObjectData( ObjectProperty<?, T> parent, int start, int end, Map<Object> properties ) {
			super();
			this.parent = parent;
			this.start = start;
			this.end = end;
			this.properties = properties;
		}

		@Override
		public <S> Data<S> sub( ObjectProperty<? super T, S> path ) {

			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public <S> List<Data<S>> subs( RangeProperty<? super T, S> path ) {
			//The list actually has to resolve the result list using this objects data because there can be any kind of selection applied - this object cannot and shouln't know about the way the list is computed. 

			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public <V> V value( ValueProperty<? super T, V> property ) {
			return property.resolveIn( this );
		}

		@Override
		public boolean isEmpty() {
			return false;
		}

		@Override
		public int indexFor( Key key ) {
			return properties.indexFor( key ) - start;
		}

		@Override
		public int length() {
			return end - start;
		}

		@Override
		public Object at( int index ) {
			return properties.at( start + index );
		}
	}

	static final class NoData<T>
			implements Data<T>, Table<Object> {

		@Override
		public <S> Data<S> sub( ObjectProperty<? super T, S> path ) {
			return empty();
		}

		@Override
		public <S> List<Data<S>> subs( RangeProperty<? super T, S> path ) {
			return List.with.noElements();
		}

		@Override
		public <V> V value( ValueProperty<? super T, V> path ) {
			return path.resolveIn( this );
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

	}
}