package de.jbee.core;

import static de.jbee.core.Core.list;
import static de.jbee.core.list.ListMatcher.hasEqualElementsAsIn;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import de.jbee.core.list.List;

public class TestCore {

	@Test
	public void testList6() {
		assertThat( list( 12 ), hasEqualElementsAsIn( List.with.elements( 12 ) ) );
		assertThat( list( 11, 12 ), hasEqualElementsAsIn( List.with.elements( 11, 12 ) ) );
		assertThat( list( 22, 11, 12 ), hasEqualElementsAsIn( List.with.elements( 22, 11, 12 ) ) );
		assertThat( list( 11, 22, 11, 12 ), hasEqualElementsAsIn( List.with.elements( 11, 22, 11,
				12 ) ) );
		assertThat( list( 19, 11, 22, 11, 12 ), hasEqualElementsAsIn( List.with.elements( 19, 11,
				22, 11, 12 ) ) );
		assertThat( list( 11, 19, 11, 22, 11, 12 ), hasEqualElementsAsIn( List.with.elements( 11,
				19, 11, 22, 11, 12 ) ) );
	}

	@Test
	public void testNextHighestPowerOf2() {
		assertThat( Core.nextHighestPowerOf2( 1 ), is( 1 ) );
		assertThat( Core.nextHighestPowerOf2( 2 ), is( 2 ) );
		assertThat( Core.nextHighestPowerOf2( 3 ), is( 4 ) );
		assertThat( Core.nextHighestPowerOf2( 4 ), is( 4 ) );
		assertThat( Core.nextHighestPowerOf2( 5 ), is( 8 ) );
		assertThat( Core.nextHighestPowerOf2( 6 ), is( 8 ) );
		assertThat( Core.nextHighestPowerOf2( 7 ), is( 8 ) );
		assertThat( Core.nextHighestPowerOf2( 8 ), is( 8 ) );
		assertThat( Core.nextHighestPowerOf2( 9 ), is( 16 ) );
	}
}
