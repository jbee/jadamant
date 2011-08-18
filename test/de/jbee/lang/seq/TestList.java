package de.jbee.lang.seq;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertThat;

import java.util.Random;

import org.junit.Test;

import de.jbee.lang.List;
import de.jbee.lang.Traversal;

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
	public void testFill() {
		verifyFillAll( List.with.elements( 1, 2, 3, 4, 5 ) );
		verifyFillAll( List.with.elements( 9, 7, 6, 4, 3, 1 ) );
	}

	@Test
	public void testFillEnumStack() {
		verifyFillAll( List.numbers.fromTo( 1, 4 ).concat( List.with.elements( 6, 9, 2 ) ) );
	}

	private void verifyFillAll( List<Integer> l ) {
		Integer[] a = new Integer[l.length()];
		l.fill( 0, a, 0, l.length() );
		for ( int i = 0; i < a.length; i++ ) {
			assertThat( a[i], is( l.at( i ) ) );
		}
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
			assertThat( l.length() - 1, is( deleted.length() ) );
			for ( int j = 0; j < deleted.length(); j++ ) {
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
	public void testDescendingFollowdByMixedTakeR() {
		verifyTakeR( List.with.elements( 7, 1, 2, 11, 19, 11, 22, 11, 12 ) );
		verifyTakeR( List.with.elements( 7, 1, 2, 11, 19, 11, 22, 10, 11, 12 ) );
	}

	@Test
	public void testPrepand() {
		List<Integer> l = List.with.noElements();
		for ( int i = 1; i < 100; i++ ) {
			l = l.prepand( i );
			assertThat( i, is( l.length() ) );
			for ( int j = 0; j < i; j++ ) {
				assertThat( i - j, is( l.at( j ) ) );
			}
		}
	}

	@Test
	public void testTraverse() {
		verifyTraverseIterative( List.with.elements( 1, 4, 7, 2, 9 ) );
		verifyTraverseIterative( List.with.elements( 1, 2, 3, 4, 5 ) );
		verifyTraverseIterative( List.with.elements( 42, 20 ) );
		verifyTraverseIterative( List.with.elements( 42 ) );
		verifyTraverseIterative( List.with.<Integer> noElements() );
	}

	private void verifyTraverseIterative( final List<Integer> l ) {
		final int[] readI = new int[1];
		l.traverse( 0, new Traversal<Integer>() {

			int i = 0;

			@Override
			public int incrementOn( Integer e ) {
				assertThat( e, is( l.at( i++ ) ) );
				readI[0] = i;
				return 1;
			}

		} );
		// make sure increment on has been called 
		assertThat( readI[0], is( l.length() ) );
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
		final int size = l.length();
		assertThat( l.drop( size ).length(), is( 0 ) );
		assertThat( l.drop( size + 1 ).length(), is( 0 ) );
		assertThat( l.drop( size - 1 ).length(), is( 1 ) );
		for ( int i = 1; i < size; i++ ) {
			List<Integer> dropped = l.drop( i );
			assertThat( dropped.length(), is( size - i ) );
			assertThat( i + "=>" + l + "=" + dropped, dropped.at( 0 ), is( l.at( i ) ) );
			assertThat( dropped.at( size - i - 1 ), is( l.at( size - 1 ) ) );
		}
	}

	private void verifyDropR( List<Integer> l ) {
		final int size = l.length();
		assertThat( List.which.dropsLast( size ).from( l ).length(), is( 0 ) );
		assertThat( List.which.dropsLast( size + 1 ).from( l ).length(), is( 0 ) );
		assertThat( List.which.dropsLast( size - 1 ).from( l ).length(), is( 1 ) );
		for ( int i = 1; i < size; i++ ) {
			List<Integer> dropped = List.which.dropsLast( i ).from( l );
			assertThat( dropped.length(), is( size - i ) );
			assertThat( dropped.at( 0 ), is( l.at( 0 ) ) );
			int lastIndex = size - i - 1;
			assertThat( dropped.at( lastIndex ), is( l.at( lastIndex ) ) );
		}
	}

	private void verifyTakeL( List<Integer> l ) {
		final int size = l.length();
		assertThat( l.take( size ), sameInstance( l ) );
		assertThat( l.take( size + 1 ), sameInstance( l ) );
		assertThat( l.take( size - 1 ), not( sameInstance( l ) ) );
		for ( int i = 1; i < size; i++ ) {
			List<Integer> taken = l.take( i );
			assertThat( taken.length(), is( i ) );
			assertThat( taken.at( 0 ), is( l.at( 0 ) ) );
			int lastIndex = i - 1;
			assertThat( lastIndex + "=>" + l.toString(), taken.at( lastIndex ),
					is( l.at( lastIndex ) ) );
		}
	}

	private void verifyTakeR( List<Integer> l ) {
		final int size = l.length();
		assertThat( List.which.takesLast( size ).from( l ), sameInstance( l ) );
		assertThat( List.which.takesLast( size + 1 ).from( l ), sameInstance( l ) );
		assertThat( List.which.takesLast( size - 1 ).from( l ), not( sameInstance( l ) ) );
		for ( int i = 1; i < size; i++ ) {
			List<Integer> taken = List.which.takesLast( i ).from( l );
			assertThat( taken.length(), is( i ) );
			assertThat( taken.at( 0 ), is( l.at( size - i ) ) );
		}
	}

}
