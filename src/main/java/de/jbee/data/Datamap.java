package de.jbee.data;

import static de.jbee.data.Path.itemPath;
import static de.jbee.lang.seq.IndexFor.insertionIndex;
import de.jbee.data.Dataset.Itemised;
import de.jbee.data.Dataset.Items;
import de.jbee.data.Dataset.Records;
import de.jbee.data.Dataset.Values;
import de.jbee.lang.List;
import de.jbee.lang.ListIndex;
import de.jbee.lang.Map;
import de.jbee.lang.Ord;
import de.jbee.lang.Order;
import de.jbee.lang.Map.Entry;
import de.jbee.lang.Map.Key;

public class Datamap {

	private static final Dataset<?> EMPTY = new EmptyDataset<Object>();
	private static final Items<?> NO_ITEMS = new NoItems<Object>();

	@SuppressWarnings ( "unchecked" )
	static <T> Dataset<T> empty() {
		return (Dataset<T>) EMPTY;
	}

	@SuppressWarnings ( "unchecked" )
	static <E> Items<E> noItems() {
		return (Items<E>) NO_ITEMS;
	}

	static <T> Dataset<T> dataset( Map<Object> properties ) {
		return new ObjectDataset<T>( Path.ROOT, 0, properties.length(), properties );
	}

	static final class EmptyDataset<T>
			implements Dataset<T>, Records, Values<T> {

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
		public <I> I items( ItemProperty<? super T, I> property ) {
			return property.resolveIn( Datamap.<T> noItems() );
		}

		@Override
		public int length() {
			return 0;
		}

		@Override
		public <E> Dataset<E> noneAs( Class<E> type ) {
			return empty();
		}

		@Override
		public Ord<Object> order() {
			return Order.inherent;
		}

		@Override
		public <S> Dataset<S> record( RecordProperty<? super T, S> property ) {
			return empty();
		}

		@Override
		public <E> Dataset<E> recordAt( Path format, Class<E> type ) {
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

	}

	private static final class ListItems<E>
			implements Items<E> {

		private final Itemised<E> items;

		ListItems( Itemised<E> items ) {
			super();
			this.items = items;
		}

		@Override
		public Dataset<E> at( int index ) {
			return items.item( itemPath( index ) );
		}

		@Override
		public Items<E> drop( int count ) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public int indexFor( Key key ) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public int indexFor( Key key, int startInclusive, int endExclusive ) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public boolean isEmpty() {
			return false;
		}

		@Override
		public int length() {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public Items<E> range( Path start, Path end ) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Items<E> take( int count ) {
			// TODO Auto-generated method stub
			return null;
		}

	}

	private static final class VirtualItems<E, D extends Dataset<E> & Records>
			implements Items<E> {

		private final D dataset;

		VirtualItems( D dataset ) {
			super();
			this.dataset = dataset;
		}

		@Override
		public Items<E> drop( int count ) {
			return count > 0
				? Datamap.<E> noItems()
				: this;
		}

		@Override
		public Items<E> range( Path start, Path end ) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Items<E> take( int count ) {
			return count < 1
				? Datamap.<E> noItems()
				: this;
		}

		@Override
		public int indexFor( Key key ) {
			return dataset.indexFor( key );
		}

		@Override
		public int indexFor( Key key, int startInclusive, int endExclusive ) {
			return dataset.indexFor( key, startInclusive, endExclusive );
		}

		@Override
		public boolean isEmpty() {
			return false;
		}

		@Override
		public int length() {
			return 1;
		}

		@Override
		public Dataset<E> at( int index ) {
			return index == 0
				? dataset
				: Datamap.<E> empty();
		}

	}

	private static final class NoItems<E>
			implements Items<E> {

		NoItems() {
			// hide
		}

		@Override
		public Dataset<E> at( int index )
				throws IndexOutOfBoundsException {
			throw new IndexOutOfBoundsException( "No such item: " + index );
		}

		@Override
		public Items<E> drop( int count ) {
			return this;
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
		public Items<E> range( Path start, Path end ) {
			return this;
		}

		@Override
		public Items<E> take( int count ) {
			return this;
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
	private static final class ObjectDataset<T>
			implements Itemised<T>, Dataset<T>, Records, Values<T> {

		private final Path root;
		private final int start;
		private final int end;
		private final Map<Object> properties;

		ObjectDataset( Path root, int start, int end, Map<Object> properties ) {
			super();
			this.root = root;
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
		public <I> I items( ItemProperty<? super T, I> property ) {
			return root.endsWithItem()
				? property.resolveIn( new ListItems<T>( this ) )
				: property.resolveIn( new VirtualItems<T, ObjectDataset<T>>( this ) );
		}

		@Override
		public int length() {
			return end - start;
		}

		@Override
		public <E> Dataset<E> noneAs( Class<E> type ) {
			return empty();
		}

		@Override
		public Ord<Object> order() {
			return properties.order();
		}

		@Override
		public <S> Dataset<S> record( RecordProperty<? super T, S> property ) {
			return property.resolveIn( root, this );
		}

		@Override
		public <E> Dataset<E> recordAt( Path format, Class<E> type ) {
			return recordAt( format, type, start, end );
		}

		private <E> Dataset<E> recordAt( Path format, Class<E> type, int start, int end ) {
			int formatIndex = properties.indexFor( format );
			if ( formatIndex < start || formatIndex >= end
					|| !isRecordOfType( type, properties.at( formatIndex ).value() ) ) {
				return empty();
			}
			Path record = format.parent();
			int recordEnd = insertionIndex( properties.indexFor( record.last() ) );
			final int low = Math.min( start + formatIndex, end );
			final int high = Math.min( start + recordEnd, end );
			if ( low >= high ) {
				return empty();
			}
			return new ObjectDataset<E>( record, low, high, properties );
		}

		@Override
		public String toString() {
			StringBuilder b = new StringBuilder();
			b.append( root );
			for ( int i = start; i < end; i++ ) {
				b.append( '\n' );
				Entry<Object> e = properties.at( i );
				b.append( String.format( "%-20s => %s", e.key(), e.value() ) );
			}
			return b.toString();
		}

		@Override
		public <V> V value( ValueProperty<? super T, V> property ) {
			return property.resolveIn( root, this );
		}

		private <E> boolean isRecordOfType( Class<E> required, Object value ) {
			return required == value
					|| ( value instanceof Class<?> && ( (Class<?>) value ).isAssignableFrom( required ) );
		}

		@Override
		public Dataset<T> item( Path item ) {
			Object type = at( 0 );
			if ( type instanceof Class<?> ) {
				return recordAt( root.itemParent().dot( item ).dot( TYPE ), (Class<T>) type, 0,
						properties.length() );
			}
			return empty();
		}

	}
}