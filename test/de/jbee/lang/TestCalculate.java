package de.jbee.lang;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class TestCalculate {

	@Test
	public void testNextHighestPowerOf2() {
		assertThat( Calculate.nextHighestPowerOf2( 1 ), is( 1 ) );
		assertThat( Calculate.nextHighestPowerOf2( 2 ), is( 2 ) );
		assertThat( Calculate.nextHighestPowerOf2( 3 ), is( 4 ) );
		assertThat( Calculate.nextHighestPowerOf2( 4 ), is( 4 ) );
		assertThat( Calculate.nextHighestPowerOf2( 5 ), is( 8 ) );
		assertThat( Calculate.nextHighestPowerOf2( 6 ), is( 8 ) );
		assertThat( Calculate.nextHighestPowerOf2( 7 ), is( 8 ) );
		assertThat( Calculate.nextHighestPowerOf2( 8 ), is( 8 ) );
		assertThat( Calculate.nextHighestPowerOf2( 9 ), is( 16 ) );
	}
}
