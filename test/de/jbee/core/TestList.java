package de.jbee.core;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import de.jbee.core.list.List;
import de.jbee.core.list.CoreList.EnumList;

public class TestList {

	@Test ( expected = IndexOutOfBoundsException.class )
	public void testAtOnEmptyListOutOfBoundsException() {
		List<Integer> l = List.with.noElements();
		l.at( 0 );
	}

	@Test ( expected = IndexOutOfBoundsException.class )
	public void testAtOnOneElementListOutOfBoundsException() {
		List<Integer> l = List.with.noElements();
		l = l.prepand( 1 );
		l.at( 1 );
	}

	@Test ( expected = IndexOutOfBoundsException.class )
	public void testAtOnTwoElementListOutOfBoundsException() {
		List<Integer> l = List.with.noElements();
		l = l.prepand( 1 ).prepand( 2 );
		l.at( 2 );
	}

	@Test
	public void testDropL() {
		List<Integer> l = List.with.noElements();
		final int size = 30;
		for ( int i = 1; i <= size; i++ ) {
			l = l.prepand( i );
		}
		verifyDropL( l );
	}

	@Test
	public void testDropR() {
		List<Integer> l = List.with.noElements();
		final int size = 30;
		for ( int i = 1; i <= size; i++ ) {
			l = l.prepand( i );
		}
		verifyDropR( l );
	}

	@Test
	public void testPrepand() {
		List<Integer> l = List.with.noElements();
		for ( int i = 1; i < 100; i++ ) {
			l = l.prepand( i );
			assertThat( i, is( l.size() ) );
			for ( int j = 0; j < i; j++ ) {
				assertThat( i - j, is( l.at( j ) ) );
			}
		}
	}

	@Test
	public void testTakeL() {
		List<Integer> l = List.with.noElements();
		final int size = 30;
		for ( int i = 1; i <= size; i++ ) {
			l = l.prepand( i );
		}
		verifyTakeL( l );
	}

	@Test
	public void testTakeR() {
		List<Integer> l = List.with.noElements();
		List<Integer> stack1 = null;
		List<Integer> stack2 = null;
		List<Integer> stack3 = null;
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
		if ( ! ( l instanceof EnumList<?> ) ) {
			assertThat( stack1, sameInstance( l.takeR( 2 ) ) );
			assertThat( stack2, sameInstance( l.takeR( 6 ) ) );
			assertThat( stack3, sameInstance( l.takeR( 14 ) ) );
		}
		verifyTakeR( l );
	}

	@Test
	public void testDelete() {
		List<Integer> l = List.with.noElements();
		final int size = 14;
		for ( int i = 1; i <= size; i++ ) {
			l = l.prepand( i );
		}
		for ( int i = 0; i < size; i++ ) {
			List<Integer> deleted = l.deleteAt( i );
			assertThat( l.size() - 1, is( deleted.size() ) );
			for ( int j = 0; j < deleted.size(); j++ ) {
				if ( j < i ) {
					assertThat( l.at( j ), is( deleted.at( j ) ) );
				} else {
					assertThat( l.at( j + 1 ), is( deleted.at( j ) ) );
				}
			}
		}
	}

	private void verifyDropL( List<Integer> l ) {
		final int size = l.size();
		assertThat( 0, is( l.dropL( size ).size() ) );
		assertThat( 0, is( l.dropL( size + 1 ).size() ) );
		assertThat( 1, is( l.dropL( size - 1 ).size() ) );
		for ( int i = 1; i < size; i++ ) {
			List<Integer> dropped = l.dropL( i );
			assertThat( size - i, is( dropped.size() ) );
			assertThat( l.at( 0 ), not( is( dropped.at( 0 ) ) ) );
			assertThat( l.at( size - 1 ), is( dropped.at( size - i - 1 ) ) );
		}
	}

	private void verifyDropR( List<Integer> l ) {
		final int size = l.size();
		assertThat( 0, is( l.dropR( size ).size() ) );
		assertThat( 0, is( l.dropR( size + 1 ).size() ) );
		assertThat( 1, is( l.dropR( size - 1 ).size() ) );
		for ( int i = 1; i < size; i++ ) {
			List<Integer> dropped = l.dropR( i );
			assertThat( size - i, is( dropped.size() ) );
			assertThat( l.at( 0 ), is( dropped.at( 0 ) ) );
			int lastIndex = size - i - 1;
			assertThat( l.at( lastIndex ), is( dropped.at( lastIndex ) ) );
		}
	}

	private void verifyTakeL( List<Integer> l ) {
		final int size = l.size();
		assertThat( l, sameInstance( l.takeL( size ) ) );
		assertThat( l, sameInstance( l.takeL( size + 1 ) ) );
		assertThat( l, not( sameInstance( l.takeL( size - 1 ) ) ) );
		for ( int i = 1; i < size; i++ ) {
			List<Integer> taken = l.takeL( i );
			assertThat( i, is( taken.size() ) );
			assertThat( l.at( 0 ), is( taken.at( 0 ) ) );
			int lastIndex = i - 1;
			assertThat( l.at( lastIndex ), is( taken.at( lastIndex ) ) );
		}
	}

	private void verifyTakeR( List<Integer> l ) {
		final int size = l.size();
		assertThat( l, sameInstance( l.takeR( size ) ) );
		assertThat( l, sameInstance( l.takeR( size + 1 ) ) );
		assertThat( l, not( sameInstance( l.takeR( size - 1 ) ) ) );
		for ( int i = 1; i < size; i++ ) {
			List<Integer> taken = l.takeR( i );
			assertThat( i, is( taken.size() ) );
			assertThat( i, is( taken.at( 0 ) ) );
		}
	}

}
