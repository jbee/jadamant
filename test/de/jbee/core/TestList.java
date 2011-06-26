package de.jbee.core;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertThat;

import java.util.Random;

import org.junit.Test;

import de.jbee.core.list.List;
import de.jbee.core.list.CoreList.EnumList;

public class TestList {

	private static final Random RANDOM = new Random();
	private static final int LIST_SIZE = 30;

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

	@Test
	public void testDescendingDropL() {
		verifyDropL( descendingTo1From( LIST_SIZE ) );
	}

	@Test
	public void testDescendingDropR() {
		verifyDropR( descendingTo1From( LIST_SIZE ) );
	}

	@Test
	public void testDescendingTakeL() {
		verifyTakeL( descendingTo1From( LIST_SIZE ) );
	}

	@Test
	public void testDescendingTakeR() {
		verifyTakeR( descendingTo1From( LIST_SIZE ) );
	}

	@Test
	public void testRandomDropL() {
		verifyDropL( randomized( LIST_SIZE ) );
	}

	@Test
	public void testRandomDropR() {
		verifyDropR( randomized( LIST_SIZE ) );
	}

	@Test
	public void testRandomTakeL() {
		verifyTakeL( randomized( LIST_SIZE ) );
	}

	@Test
	public void testRandomTakeR() {
		verifyTakeR( randomized( LIST_SIZE ) );
	}

	@Test
	public void testDescendingFollowdByRandomTakeR() {
		verifyTakeR( List.with.elements( 11, 19, 11, 22, 11, 12 ) );
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
	public void testTakeRStackList() {
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

	private List<Integer> descendingTo1From( final int start ) {
		List<Integer> l = List.with.noElements();
		for ( int i = 10; i <= start; i++ ) {
			l = l.prepand( i );
		}
		List<Integer> l2 = List.with.noElements();
		for ( int i = 1; i < 10; i++ ) {
			l2 = l2.prepand( i );
		}
		return l.concat( l2 );
	}

	private List<Integer> randomized( int size ) {
		List<Integer> l = List.with.noElements();
		for ( int i = 0; i < size; i++ ) {
			l = l.prepand( RANDOM.nextInt( 30 ) );
		}
		return l;
	}

	private void verifyDropL( List<Integer> l ) {
		final int size = l.size();
		assertThat( l.dropL( size ).size(), is( 0 ) );
		assertThat( l.dropL( size + 1 ).size(), is( 0 ) );
		assertThat( l.dropL( size - 1 ).size(), is( 1 ) );
		for ( int i = 1; i < size; i++ ) {
			List<Integer> dropped = l.dropL( i );
			assertThat( dropped.size(), is( size - i ) );
			assertThat( dropped.at( 0 ), is( l.at( i ) ) );
			assertThat( dropped.at( size - i - 1 ), is( l.at( size - 1 ) ) );
		}
	}

	private void verifyDropR( List<Integer> l ) {
		final int size = l.size();
		assertThat( l.dropR( size ).size(), is( 0 ) );
		assertThat( l.dropR( size + 1 ).size(), is( 0 ) );
		assertThat( l.dropR( size - 1 ).size(), is( 1 ) );
		for ( int i = 1; i < size; i++ ) {
			List<Integer> dropped = l.dropR( i );
			assertThat( dropped.size(), is( size - i ) );
			assertThat( dropped.at( 0 ), is( l.at( 0 ) ) );
			int lastIndex = size - i - 1;
			assertThat( dropped.at( lastIndex ), is( l.at( lastIndex ) ) );
		}
	}

	private void verifyTakeL( List<Integer> l ) {
		final int size = l.size();
		assertThat( l.takeL( size ), sameInstance( l ) );
		assertThat( l.takeL( size + 1 ), sameInstance( l ) );
		assertThat( l.takeL( size - 1 ), not( sameInstance( l ) ) );
		for ( int i = 1; i < size; i++ ) {
			List<Integer> taken = l.takeL( i );
			assertThat( taken.size(), is( i ) );
			assertThat( taken.at( 0 ), is( l.at( 0 ) ) );
			int lastIndex = i - 1;
			assertThat( taken.at( lastIndex ), is( l.at( lastIndex ) ) );
		}
	}

	private void verifyTakeR( List<Integer> l ) {
		final int size = l.size();
		assertThat( l.takeR( size ), sameInstance( l ) );
		assertThat( l.takeR( size + 1 ), sameInstance( l ) );
		assertThat( l.takeR( size - 1 ), not( sameInstance( l ) ) );
		for ( int i = 1; i < size; i++ ) {
			List<Integer> taken = l.takeR( i );
			assertThat( taken.size(), is( i ) );
			assertThat( taken.at( 0 ), is( l.at( size - i ) ) );
		}
	}

}
