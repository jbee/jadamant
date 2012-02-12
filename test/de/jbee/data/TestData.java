package de.jbee.data;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import de.jbee.data.DataProperty.ObjectProperty;
import de.jbee.data.DataProperty.ValueProperty;
import de.jbee.lang.Map;
import de.jbee.lang.seq.Sequences;

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

	@Test
	public void testFlatObject() {
		Data<FlatObject> obj = MapData.empty();
		assertThat( obj.value( FlatObject.total ), is( 1 ) );
		assertThat( obj.value( FlatObject.name ), is( "unnamed" ) );

		Map<Object> properties = Map.with.noEntries( Data.ORDER );
		properties = properties.insert( Sequences.key( "total" ), 2 );
		properties = properties.insert( Sequences.key( "name" ), "erni" );
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
		properties = properties.insert( Sequences.key( "percent" ), 100f );
		properties = properties.insert( Sequences.key( "flat.total" ), 2 );
		properties = properties.insert( Sequences.key( "flat.name" ), "erni" );
		properties = properties.insert( Sequences.key( "flat." + Property.OBJECT_TYPE ),
				FlatObject.class );
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
		properties = properties.insert( Sequences.key( "deep." + Property.OBJECT_TYPE ),
				DeepObject.class );
		properties = properties.insert( Sequences.key( "deep.percent" ), 100f );
		properties = properties.insert( Sequences.key( "deep.flat.total" ), 2 );
		properties = properties.insert( Sequences.key( "deep.flat.name" ), "erni" );
		properties = properties.insert( Sequences.key( "deep.flat." + Property.OBJECT_TYPE ),
				FlatObject.class );

		Data<DeeperObject> obj = MapData.object( properties );
		Data<DeepObject> deepObj = obj.object( DeeperObject.deep );
		assertThat( deepObj.length(), is( 5 ) );
		assertThat( deepObj.value( DeepObject.percent ), is( 100f ) );
		Data<FlatObject> flatObj = deepObj.object( DeepObject.flat );
		assertThat( flatObj.length(), is( 3 ) );
		assertThat( flatObj.value( FlatObject.name ), is( "erni" ) );
	}
}
