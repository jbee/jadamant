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
	public void testTakeR() {
		IElasticList<Integer> l = ElasticList.emptyForPrepanding();
		IElasticList<Integer> stack1 = null;
		IElasticList<Integer> stack2 = null;
		IElasticList<Integer> stack3 = null;
		final int size = 30;
		for ( int i = 1; i <= size; i++ ) {
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
		verifyTakeR( l );
	}

	private void verifyTakeR( IElasticList<Integer> l ) {
		final int size = l.size();
		assertThat( l, sameInstance( l.takeR( size ) ) );
		assertThat( l, sameInstance( l.takeR( size + 1 ) ) );
		assertThat( l, not( sameInstance( l.takeR( size - 1 ) ) ) );
		for ( int i = 1; i < size; i++ ) {
			IElasticList<Integer> taken = l.takeR( i );
			assertThat( i, is( taken.size() ) );
			assertThat( i, is( taken.at( 0 ) ) );
		}
	}

	@Test
	public void testTakeL() {
		IElasticList<Integer> l = ElasticList.emptyForPrepanding();
		final int size = 30;
		for ( int i = 1; i <= size; i++ ) {
			l = l.prepand( i );
		}
		verifyTakeL( l );
	}

	private void verifyTakeL( IElasticList<Integer> l ) {
		final int size = l.size();
		assertThat( l, sameInstance( l.takeL( size ) ) );
		assertThat( l, sameInstance( l.takeL( size + 1 ) ) );
		assertThat( l, not( sameInstance( l.takeL( size - 1 ) ) ) );
		for ( int i = 1; i < size; i++ ) {
			IElasticList<Integer> taken = l.takeL( i );
			assertThat( i, is( taken.size() ) );
			assertThat( l.at( 0 ), is( taken.at( 0 ) ) );
			int lastIndex = i - 1;
			assertThat( l.at( lastIndex ), is( taken.at( lastIndex ) ) );
		}
	}

	@Test
	public void testDropR() {
		IElasticList<Integer> l = ElasticList.emptyForPrepanding();
		final int size = 30;
		for ( int i = 1; i <= size; i++ ) {
			l = l.prepand( i );
		}
		verifyDropR( l );
	}

	private void verifyDropR( IElasticList<Integer> l ) {
		final int size = l.size();
		assertThat( 0, is( l.dropR( size ).size() ) );
		assertThat( 0, is( l.dropR( size + 1 ).size() ) );
		assertThat( 1, is( l.dropR( size - 1 ).size() ) );
		for ( int i = 1; i < size; i++ ) {
			IElasticList<Integer> dropped = l.dropR( i );
			assertThat( size - i, is( dropped.size() ) );
			assertThat( l.at( 0 ), is( dropped.at( 0 ) ) );
			int lastIndex = size - i - 1;
			assertThat( l.at( lastIndex ), is( dropped.at( lastIndex ) ) );
		}
	}

	@Test
	public void testDropL() {
		IElasticList<Integer> l = ElasticList.emptyForPrepanding();
		final int size = 30;
		for ( int i = 1; i <= size; i++ ) {
			l = l.prepand( i );
		}
		verifyDropL( l );
	}

	private void verifyDropL( IElasticList<Integer> l ) {
		final int size = l.size();
		assertThat( 0, is( l.dropL( size ).size() ) );
		assertThat( 0, is( l.dropL( size + 1 ).size() ) );
		assertThat( 1, is( l.dropL( size - 1 ).size() ) );
		for ( int i = 1; i < size; i++ ) {
			IElasticList<Integer> dropped = l.dropL( i );
			assertThat( size - i, is( dropped.size() ) );
			assertThat( l.at( 0 ), not( is( dropped.at( 0 ) ) ) );
			assertThat( l.at( size - 1 ), is( dropped.at( size - i - 1 ) ) );
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
