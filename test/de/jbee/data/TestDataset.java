package de.jbee.data;

import static de.jbee.data.Property.each;
import static de.jbee.lang.seq.Sequences.key;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import de.jbee.data.Dataset.Records;
import de.jbee.data.Dataset.ValueProperty;
import de.jbee.lang.Map;
import de.jbee.lang.Sequence;

public class TestDataset {

	static interface Shallow {

		ValueProperty<Shallow, String> name = Property.value( "name", String.class, "unnamed" );
		ValueProperty<Shallow, Integer> total = Property.value( "total", 1 );
	}

	static interface Deep {

		ValueProperty<Deep, Float> percent = Property.value( "percent", Float.class, 0.1f );
		Record<Deep, Shallow> shallow = Property.record( "flat", Shallow.class );
	}

	static interface Deeper {

		Record<Deeper, Deep> deep = Property.record( "deep", Deep.class );
	}

	static interface ShallowItems {

		Record<ShallowItems, Shallow> members = Property.record( "members", Shallow.class );

	}

	public void test() {
		Record<Deeper, Shallow> deeper = Deeper.deep.child( Deep.shallow );
	}

	@Test
	public void valuesOfEmptyRecordsReturningDefaultsFromProperties() {
		Dataset<Shallow> obj = Datamap.empty();
		assertThat( obj.value( Shallow.total ), is( 1 ) );
		assertThat( obj.value( Shallow.name ), is( "unnamed" ) );
	}

	@Test
	public void valuesOfShallowRecordsCanBeReadUsingDirectProperties() {
		Map<Object> properties = Map.with.noEntries( Dataset.ORDER );
		properties = properties.insert( key( "total" ), 2 );
		properties = properties.insert( key( "name" ), "erni" );
		Dataset<Shallow> obj = Datamap.object( properties );
		assertThat( obj.value( Shallow.total ), is( 2 ) );
		assertThat( obj.value( Shallow.name ), is( "erni" ) );
	}

	@Test
	public void recordsAndItsValuesInADeepRecordCanBeReadUsingDirectProperties() {
		Dataset<Deep> obj = Datamap.empty();
		assertThat( obj.record( Deep.shallow ).value( Shallow.total ), is( 1 ) );
		assertThat( obj.value( Deep.percent ), is( 0.1f ) );

		Map<Object> properties = Map.with.noEntries( Dataset.ORDER );
		properties = properties.insert( key( "percent" ), 100f );
		properties = properties.insert( key( "flat.total" ), 2 );
		properties = properties.insert( key( "flat.name" ), "erni" );
		properties = properties.insert( key( "flat." + Records.TYPE ), Shallow.class );
		obj = Datamap.object( properties );
		Dataset<Shallow> shallow = obj.record( Deep.shallow );
		assertFalse( shallow.isEmpty() );
		assertThat( shallow.value( Shallow.total ), is( 2 ) );
		assertThat( shallow.value( Shallow.name ), is( "erni" ) );
		assertThat( obj.value( Deep.percent ), is( 100f ) );
	}

	@Test
	public void nestedRecordsAndTheirValuesCanBeReadUsingDirectProperties() {
		Map<Object> properties = Map.with.noEntries( Dataset.ORDER );
		properties = properties.insert( key( "deep." + Records.TYPE ), Deep.class );
		properties = properties.insert( key( "deep.percent" ), 100f );
		properties = properties.insert( key( "deep.flat.total" ), 2 );
		properties = properties.insert( key( "deep.flat.name" ), "erni" );
		properties = properties.insert( key( "deep.flat." + Records.TYPE ), Shallow.class );

		Dataset<Deeper> obj = Datamap.object( properties );
		Dataset<Deep> deep = obj.record( Deeper.deep );
		assertThat( deep.length(), is( 5 ) );
		assertThat( deep.value( Deep.percent ), is( 100f ) );
		Dataset<Shallow> shallow = deep.record( Deep.shallow );
		assertThat( shallow.length(), is( 3 ) );
		assertThat( shallow.value( Shallow.name ), is( "erni" ) );
	}

	@Test
	public void itemsOfAListRecordsCanBeReadUsingDirectProperties() {
		Map<Object> properties = Map.with.noEntries( Dataset.ORDER );
		properties = properties.insert( key( "members:1." + Records.TYPE ), Shallow.class );
		properties = properties.insert( key( "members:1.name" ), "erni" );
		properties = properties.insert( key( "members:1.total" ), 42 );
		properties = properties.insert( key( "members:2." + Records.TYPE ), Shallow.class );
		properties = properties.insert( key( "members:2.name" ), "bert" );
		properties = properties.insert( key( "members:2.total" ), 23 );
		properties = properties.insert( key( "members:3." + Records.TYPE ), Shallow.class );
		properties = properties.insert( key( "members:3.name" ), "tiffi" );
		properties = properties.insert( key( "members:3.total" ), 5 );
		properties = properties.insert( key( "members:4." + Records.TYPE ), Shallow.class );
		properties = properties.insert( key( "members:4.name" ), "samson" );
		properties = properties.insert( key( "members:4.total" ), 1 );
		Dataset<ShallowItems> obj = Datamap.object( properties );
		Dataset<Shallow> members = obj.record( ShallowItems.members );
		assertThat( members.length(), is( 3 ) );
		assertThat( members.value( Shallow.name ), is( "erni" ) );
		Dataset<Shallow> second = members.items( each( Shallow.class ) ).at( 2 );
		assertThat( second.length(), is( 3 ) );
		assertThat( second.value( Shallow.name ), is( "bert" ) );
		Dataset<Shallow> third = members.items( each( Shallow.class ) ).at( 3 );
		assertThat( third.length(), is( 3 ) );
		assertThat( third.value( Shallow.name ), is( "tiffi" ) );
	}

	@Test
	public void normalRecordsCanBeTreatedAsItemsHavingOneItem() {
		Map<Object> properties = Map.with.noEntries( Dataset.ORDER );
		properties = properties.insert( key( "total" ), 2 );
		properties = properties.insert( key( "name" ), "erni" );
		Dataset<Shallow> obj = Datamap.object( properties );
		Sequence<Dataset<Shallow>> items = obj.items( each( Shallow.class ) );
		assertThat( items.isEmpty(), is( false ) );
		assertThat( items.length(), is( 1 ) );
		Dataset<Shallow> shallow = items.at( 0 );
		assertThat( shallow.value( Shallow.total ), is( 2 ) );
		assertThat( shallow.value( Shallow.name ), is( "erni" ) );
	}
}
