package de.jbee.core;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class TestElasticList {

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
	public void testPrepandThroughReversingAppend() {
		IElasticList<Integer> l = ElasticList.emptyForAppending();
		for ( int i = 1; i < 100; i++ ) {
			l = l.append( i );
			assertThat( i, is( l.size() ) );
			for ( int j = 0; j < i; j++ ) {
				assertThat( j + 1, is( l.at( j ) ) );
			}
		}
	}

	@Test
	public void testTakeR() {
		IElasticList<Integer> l = ElasticList.emptyForPrepanding();
		IElasticList<Integer> stack1 = null;
		IElasticList<Integer> stack2 = null;
		IElasticList<Integer> stack3 = null;
		final int end = 30;
		for ( int i = 1; i <= end; i++ ) {
			l = l.prepand( i );
			if ( i == 14 ) {
				stack3 = l;
			}
			if ( i == 6 ) {
				stack2 = l;
			}
			if ( i == 2 ) {
				stack1 = l;
			}
		}
		assertThat( stack1, sameInstance( l.takeR( 2 ) ) );
		assertThat( stack2, sameInstance( l.takeR( 6 ) ) );
		assertThat( stack3, sameInstance( l.takeR( 14 ) ) );
		assertThat( l, sameInstance( l.takeR( end ) ) );
		assertThat( l, sameInstance( l.takeR( end + 1 ) ) );
		assertThat( l, not( sameInstance( l.takeR( end - 1 ) ) ) );
		for ( int i = 1; i < end; i++ ) {
			IElasticList<Integer> taken = l.takeR( i );
			assertThat( i, is( taken.size() ) );
			assertThat( i, is( taken.at( 0 ) ) );
		}
	}

	@Test
	public void testTakeL() {
		IElasticList<Integer> l = ElasticList.emptyForPrepanding();
		final int end = 30;
		for ( int i = 1; i <= end; i++ ) {
			l = l.prepand( i );
		}
		assertThat( l, sameInstance( l.takeL( end ) ) );
		assertThat( l, sameInstance( l.takeL( end + 1 ) ) );
		assertThat( l, not( sameInstance( l.takeL( end - 1 ) ) ) );
		for ( int i = 1; i < end; i++ ) {
			IElasticList<Integer> taken = l.takeL( i );
			assertThat( i, is( taken.size() ) );
			assertThat( l.at( 0 ), is( taken.at( 0 ) ) );
			assertThat( l.at( i - 1 ), is( taken.at( i - 1 ) ) );
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
