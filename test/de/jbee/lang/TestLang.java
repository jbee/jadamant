package de.jbee.lang;

import static de.jbee.lang.Lang.list;
import static de.jbee.lang.seq.ListMatcher.hasEqualElementsAsIn;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import de.jbee.lang.Lang;
import de.jbee.lang.List;

public class TestLang {

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
		assertThat( Lang.nextHighestPowerOf2( 1 ), is( 1 ) );
		assertThat( Lang.nextHighestPowerOf2( 2 ), is( 2 ) );
		assertThat( Lang.nextHighestPowerOf2( 3 ), is( 4 ) );
		assertThat( Lang.nextHighestPowerOf2( 4 ), is( 4 ) );
		assertThat( Lang.nextHighestPowerOf2( 5 ), is( 8 ) );
		assertThat( Lang.nextHighestPowerOf2( 6 ), is( 8 ) );
		assertThat( Lang.nextHighestPowerOf2( 7 ), is( 8 ) );
		assertThat( Lang.nextHighestPowerOf2( 8 ), is( 8 ) );
		assertThat( Lang.nextHighestPowerOf2( 9 ), is( 16 ) );
	}
}
