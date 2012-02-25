package de.jbee.data;

import static de.jbee.data.Property.type;
import static de.jbee.lang.seq.Sequences.key;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import de.jbee.data.Dataset.RecordProperty;
import de.jbee.data.Dataset.Records;
import de.jbee.data.Dataset.ValueProperty;
import de.jbee.lang.Map;

public class TestDataset {

	static interface FlatObject {

		ValueProperty<FlatObject, String> name = Property.value( "name", String.class, "unnamed" );
		ValueProperty<FlatObject, Integer> total = Property.value( "total", 1 );
	}

	static interface DeepObject {

		ValueProperty<DeepObject, Float> percent = Property.value( "percent", Float.class, 0.1f );
		RecordProperty<DeepObject, FlatObject> flat = Property.record( "flat", FlatObject.class );
	}

	static interface DeeperObject {

		RecordProperty<DeeperObject, DeepObject> deep = Property.record( "deep", DeepObject.class );
	}

	static interface RangeObject {

		RecordProperty<RangeObject, FlatObject> members = Property.record( "members",
				FlatObject.class );

	}

	@Test
	public void testFlatObject() {
		Dataset<FlatObject> obj = Datamap.empty();
		assertThat( obj.value( FlatObject.total ), is( 1 ) );
		assertThat( obj.value( FlatObject.name ), is( "unnamed" ) );

		Map<Object> properties = Map.with.noEntries( Dataset.ORDER );
		properties = properties.insert( key( "total" ), 2 );
		properties = properties.insert( key( "name" ), "erni" );
		obj = Datamap.object( properties );
		assertThat( obj.value( FlatObject.total ), is( 2 ) );
		assertThat( obj.value( FlatObject.name ), is( "erni" ) );
	}

	@Test
	public void testDeepObject() {
		Dataset<DeepObject> obj = Datamap.empty();
		assertThat( obj.record( DeepObject.flat ).value( FlatObject.total ), is( 1 ) );
		assertThat( obj.value( DeepObject.percent ), is( 0.1f ) );

		Map<Object> properties = Map.with.noEntries( Dataset.ORDER );
		properties = properties.insert( key( "percent" ), 100f );
		properties = properties.insert( key( "flat.total" ), 2 );
		properties = properties.insert( key( "flat.name" ), "erni" );
		properties = properties.insert( key( "flat." + Records.TYPE ), type( FlatObject.class ) );
		obj = Datamap.object( properties );
		Dataset<FlatObject> flatObj = obj.record( DeepObject.flat );
		assertFalse( flatObj.isEmpty() );
		assertThat( flatObj.value( FlatObject.total ), is( 2 ) );
		assertThat( flatObj.value( FlatObject.name ), is( "erni" ) );
		assertThat( obj.value( DeepObject.percent ), is( 100f ) );
	}

	@Test
	public void testDeeperObject() {
		Map<Object> properties = Map.with.noEntries( Dataset.ORDER );
		properties = properties.insert( key( "deep." + Records.TYPE ), type( DeepObject.class ) );
		properties = properties.insert( key( "deep.percent" ), 100f );
		properties = properties.insert( key( "deep.flat.total" ), 2 );
		properties = properties.insert( key( "deep.flat.name" ), "erni" );
		properties = properties.insert( key( "deep.flat." + Records.TYPE ), type( FlatObject.class ) );

		Dataset<DeeperObject> obj = Datamap.object( properties );
		Dataset<DeepObject> deepObj = obj.record( DeeperObject.deep );
		assertThat( deepObj.length(), is( 5 ) );
		assertThat( deepObj.value( DeepObject.percent ), is( 100f ) );
		Dataset<FlatObject> flatObj = deepObj.record( DeepObject.flat );
		assertThat( flatObj.length(), is( 3 ) );
		assertThat( flatObj.value( FlatObject.name ), is( "erni" ) );
	}

	@Test
	public void testRangeObjects() {
		Map<Object> properties = Map.with.noEntries( Dataset.ORDER );
		properties = properties.insert( key( "members:1." + Records.TYPE ), type( FlatObject.class ) );
		properties = properties.insert( key( "members:1.name" ), "erni" );
		properties = properties.insert( key( "members:1.total" ), 42 );
		properties = properties.insert( key( "members:2." + Records.TYPE ), type( FlatObject.class ) );
		properties = properties.insert( key( "members:2.name" ), "bert" );
		properties = properties.insert( key( "members:2.total" ), 23 );
		properties = properties.insert( key( "members:3." + Records.TYPE ), type( FlatObject.class ) );
		properties = properties.insert( key( "members:3.name" ), "tiffi" );
		properties = properties.insert( key( "members:3.total" ), 5 );
		properties = properties.insert( key( "members:4." + Records.TYPE ), type( FlatObject.class ) );
		properties = properties.insert( key( "members:4.name" ), "samson" );
		properties = properties.insert( key( "members:4.total" ), 1 );
		Dataset<RangeObject> obj = Datamap.object( properties );
		Dataset<FlatObject> members = obj.record( RangeObject.members );
		assertThat( members.length(), is( 3 ) );
		assertThat( members.value( FlatObject.name ), is( "erni" ) );
	}
}
