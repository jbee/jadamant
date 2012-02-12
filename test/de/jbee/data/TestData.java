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

		ValueProperty<FlatObject, Integer> total = Property.value( "total", 1 );
	}

	static interface DeepObject {

		ObjectProperty<DeepObject, FlatObject> flat = Property.object( "flat", FlatObject.class );
	}

	@Test
	public void testFlatObject() {
		Data<FlatObject> obj = MapData.empty();
		assertThat( obj.value( FlatObject.total ), is( 1 ) );

		Map<Object> properties = Map.with.noEntries();
		properties = properties.insert( Sequences.key( "total" ), 2 );
		obj = MapData.object( properties );
		assertThat( obj.value( FlatObject.total ), is( 2 ) );
	}

	@Test
	public void testDeepObject() {
		Data<DeepObject> obj = MapData.empty();
		assertThat( obj.object( DeepObject.flat ).value( FlatObject.total ), is( 1 ) );

		Map<Object> properties = Map.with.noEntries();
		properties = properties.insert( Sequences.key( "flat.total" ), 2 );
		properties = properties.insert( Sequences.key( "flat." + Property.OBJECT_TYPE ),
				FlatObject.class );
		obj = MapData.object( properties );
		Data<FlatObject> flatObj = obj.object( DeepObject.flat );
		assertFalse( flatObj.isEmpty() );
		assertThat( flatObj.value( FlatObject.total ), is( 2 ) );
	}
}
