package de.jbee.core;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class TestReverseElasticList {

	@Test
	public void testAppend() {
		IElasticList<Integer> l = ElasticList.emptyForAppending();
		for ( int i = 1; i < 100; i++ ) {
			l = l.append( i );
			assertThat( i, is( l.size() ) );
			for ( int j = 0; j < i; j++ ) {
				assertThat( j + 1, is( l.at( j ) ) );
			}
		}
	}
}
