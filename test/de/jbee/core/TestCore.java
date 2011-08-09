package de.jbee.core;

import static de.jbee.core.Core._;
import static de.jbee.core.list.ListMatcher.hasEqualElementsAsIn;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import de.jbee.core.list.List;

public class TestCore {

	@Test
	public void testList6() {
		assertThat( _( 12 ), hasEqualElementsAsIn( List.with.elements( 12 ) ) );
		assertThat( _( 11, 12 ), hasEqualElementsAsIn( List.with.elements( 11, 12 ) ) );
		assertThat( _( 22, 11, 12 ), hasEqualElementsAsIn( List.with.elements( 22, 11, 12 ) ) );
		assertThat( _( 11, 22, 11, 12 ),
				hasEqualElementsAsIn( List.with.elements( 11, 22, 11, 12 ) ) );
		assertThat( _( 19, 11, 22, 11, 12 ), hasEqualElementsAsIn( List.with.elements( 19, 11, 22,
				11, 12 ) ) );
		assertThat( _( 11, 19, 11, 22, 11, 12 ), hasEqualElementsAsIn( List.with.elements( 11, 19,
				11, 22, 11, 12 ) ) );
	}
}
