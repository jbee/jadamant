package de.jbee.core;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class TestElasticList {

	@Test
	public void testPrepandArrayList() {
		List<Integer> l = new ArrayList<Integer>();
		for ( int i = 1; i < 100; i++ ) {
			l.add( 0, i );
			assertThat( i, is( l.size() ) );
			for ( int j = 0; j < i; j++ ) {
				assertThat( i - j, is( l.get( j ) ) );
			}
		}
	}

	@Test
	public void testPlainPrepandArrayList() {
		List<Integer> l = new ArrayList<Integer>();
		for ( int i = 1; i < 10000; i++ ) {
			l.add( 0, i );
		}
	}

	@Test
	public void testPrepand() {
		IElasticList<Integer> l = ElasticList.emptyForPrepanding();
		for ( int i = 1; i < 100; i++ ) {
			l = l.prepand( i );
			assertThat( i, is( l.size() ) );
			for ( int j = 0; j < i; j++ ) {
				assertThat( i - j, is( l.at( j ) ) );
			}
		}
	}

	@Test
	public void testPlainPrepand() {
		IElasticList<Integer> l = ElasticList.emptyForPrepanding();
		for ( int i = 1; i < 10000; i++ ) {
			l = l.prepand( i );
		}
	}

	@Test
	public void testAppendThroughReverse() {
		IElasticList<Integer> l = ElasticList.emptyForAppending();
		for ( int i = 1; i < 100; i++ ) {
			l = l.append( i );
			assertThat( i, is( l.size() ) );
			for ( int j = 0; j < i; j++ ) {
				assertThat( j + 1, is( l.at( j ) ) );
			}
		}
	}

	@Test ( expected = IndexOutOfBoundsException.class )
	public void testAtOnEmptyListOutOfBoundsException() {
		IElasticList<Integer> l = ElasticList.emptyForPrepanding();
		l.at( 0 );
	}

	@Test ( expected = IndexOutOfBoundsException.class )
	public void testAtOnOneElementListOutOfBoundsException() {
		IElasticList<Integer> l = ElasticList.emptyForPrepanding();
		l = l.prepand( 1 );
		l.at( 1 );
	}

	@Test ( expected = IndexOutOfBoundsException.class )
	public void testAtOnTwoElementListOutOfBoundsException() {
		IElasticList<Integer> l = ElasticList.emptyForPrepanding();
		l = l.prepand( 1 ).prepand( 2 );
		l.at( 2 );
	}

}
