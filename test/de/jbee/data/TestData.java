package de.jbee.data;

import static de.jbee.lang.seq.Sequences.key;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import de.jbee.data.DataProperty.ObjectProperty;
import de.jbee.data.DataProperty.RangeProperty;
import de.jbee.data.DataProperty.ValueProperty;
import de.jbee.lang.Map;

public class TestData {

	static interface FlatObject {

		ValueProperty<FlatObject, String> name = Property.value( "name", String.class, "unnamed" );
		ValueProperty<FlatObject, Integer> total = Property.value( "total", 1 );
	}

	static interface DeepObject {

		ValueProperty<DeepObject, Float> percent = Property.value( "percent", Float.class, 0.1f );
		ObjectProperty<DeepObject, FlatObject> flat = Property.object( "flat", FlatObject.class );
	}

	static interface DeeperObject {

		ObjectProperty<DeeperObject, DeepObject> deep = Property.object( "deep", DeepObject.class );
	}

	static interface RangeObject {

		RangeProperty<RangeObject, FlatObject> members = Property.objects( "members",
				FlatObject.class );

		RangeProperty<RangeObject, FlatObject> bestmembers = Property.objects( "members",
				FlatObject.class, 0, 1 );
	}

	@Test
	public void testFlatObject() {
		Data<FlatObject> obj = MapData.empty();
		assertThat( obj.value( FlatObject.total ), is( 1 ) );
		assertThat( obj.value( FlatObject.name ), is( "unnamed" ) );

		Map<Object> properties = Map.with.noEntries( Data.ORDER );
		properties = properties.insert( key( "total" ), 2 );
		properties = properties.insert( key( "name" ), "erni" );
		obj = MapData.object( properties );
		assertThat( obj.value( FlatObject.total ), is( 2 ) );
		assertThat( obj.value( FlatObject.name ), is( "erni" ) );
	}

	@Test
	public void testDeepObject() {
		Data<DeepObject> obj = MapData.empty();
		assertThat( obj.object( DeepObject.flat ).value( FlatObject.total ), is( 1 ) );
		assertThat( obj.value( DeepObject.percent ), is( 0.1f ) );

		Map<Object> properties = Map.with.noEntries( Data.ORDER );
		properties = properties.insert( key( "percent" ), 100f );
		properties = properties.insert( key( "flat.total" ), 2 );
		properties = properties.insert( key( "flat.name" ), "erni" );
		properties = properties.insert( key( "flat." + Property.OBJECT_TYPE ), FlatObject.class );
		obj = MapData.object( properties );
		Data<FlatObject> flatObj = obj.object( DeepObject.flat );
		assertFalse( flatObj.isEmpty() );
		assertThat( flatObj.value( FlatObject.total ), is( 2 ) );
		assertThat( flatObj.value( FlatObject.name ), is( "erni" ) );
		assertThat( obj.value( DeepObject.percent ), is( 100f ) );
	}

	@Test
	public void testDeeperObject() {
		Map<Object> properties = Map.with.noEntries( Data.ORDER );
		properties = properties.insert( key( "deep." + Property.OBJECT_TYPE ), DeepObject.class );
		properties = properties.insert( key( "deep.percent" ), 100f );
		properties = properties.insert( key( "deep.flat.total" ), 2 );
		properties = properties.insert( key( "deep.flat.name" ), "erni" );
		properties = properties.insert( key( "deep.flat." + Property.OBJECT_TYPE ),
				FlatObject.class );

		Data<DeeperObject> obj = MapData.object( properties );
		Data<DeepObject> deepObj = obj.object( DeeperObject.deep );
		assertThat( deepObj.length(), is( 5 ) );
		assertThat( deepObj.value( DeepObject.percent ), is( 100f ) );
		Data<FlatObject> flatObj = deepObj.object( DeepObject.flat );
		assertThat( flatObj.length(), is( 3 ) );
		assertThat( flatObj.value( FlatObject.name ), is( "erni" ) );
	}

	@Test
	public void testRangeObjects() {
		Map<Object> properties = Map.with.noEntries( Data.ORDER );
		properties = properties.insert( key( "members." + Property.OBJECT_TYPE ), FlatObject.class );
		properties = properties.insert( key( "members.1.name" ), "erni" );
		properties = properties.insert( key( "members.1.total" ), 42 );
		properties = properties.insert( key( "members.2.name" ), "bert" );
		properties = properties.insert( key( "members.2.total" ), 23 );
		properties = properties.insert( key( "members.3.name" ), "tiffi" );
		properties = properties.insert( key( "members.3.total" ), 5 );
		properties = properties.insert( key( "members.4.name" ), "samson" );
		properties = properties.insert( key( "members.4.total" ), 1 );
		Data<RangeObject> obj = MapData.object( properties );
		Data<FlatObject> rangeObj = obj.objects( RangeObject.bestmembers );
		assertThat( rangeObj.length(), is( 4 ) );
		Data<FlatObject> members = obj.objects( RangeObject.members );
		assertThat( members.length(), is( 8 ) );
	}
}
