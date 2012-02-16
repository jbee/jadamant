package de.jbee.lang.seq;

import static de.jbee.lang.Lang.noInts;
import static de.jbee.lang.seq.ListMatcher.hasEqualElementsAsIn;
import static de.jbee.lang.seq.ListMatcher.hasNoElements;
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
	public void testAt_OnEmptyListOutOfBoundsException() {
		List<Integer> l = List.with.noElements();
		l.at( 0 );
	}

	@Test ( expected = IndexOutOfBoundsException.class )
	public void testAt_OnOneElementListOutOfBoundsException() {
		List<Integer> l = List.with.noElements();
		l = l.prepand( 1 );
		l.at( 1 );
	}

	@Test ( expected = IndexOutOfBoundsException.class )
	public void testAt_OnTwoElementListOutOfBoundsException() {
		List<Integer> l = List.with.noElements();
		l = l.prepand( 1 ).prepand( 2 );
		l.at( 2 );
	}

	@Test
	public void testDeleteAt_Loop() {
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

	@Test ( expected = IndexOutOfBoundsException.class )
	public void testDeteleAt_OnEmptyList() {
		List.with.noElements().deleteAt( 0 );
	}

	@Test
	public void testDeleteAt_OnlyElement() {
		assertThat( List.with.element( 'a' ).deleteAt( 0 ), hasNoElements( Character.class ) );
	}

	@Test
	public void testDeleteAt_FirstOfTwo() {
		assertThat( List.with.elements( 'a', 'g' ).deleteAt( 0 ), hasEqualElementsAsIn( 'g' ) );
	}

	@Test
	public void testDeleteAt_LastOfTwo() {
		assertThat( List.with.elements( 'a', 'g' ).deleteAt( 1 ), hasEqualElementsAsIn( 'a' ) );
	}

	@Test
	public void testDeleteAt_FirstOfThree() {
		assertThat( List.with.elements( 'a', 'g', 'k' ).deleteAt( 0 ), hasEqualElementsAsIn( 'g',
				'k' ) );
	}

	@Test
	public void testDeleteAt_SecondOfThree() {
		assertThat( List.with.elements( 'a', 'g', 'k' ).deleteAt( 1 ), hasEqualElementsAsIn( 'a',
				'k' ) );
	}

	@Test
	public void testDeleteAt_ThirdOfThree() {
		assertThat( List.with.elements( 'a', 'g', 'k' ).deleteAt( 2 ), hasEqualElementsAsIn( 'a',
				'g' ) );
	}

	@Test
	public void testDropL_DescendingList() {
		verifyDropL( descendingTo1From( LIST_SIZE ) );
	}

	@Test
	public void testDropL_RandomList() {
		verifyDropL( randomized( LIST_SIZE ) );
	}

	@Test
	public void testDropR_DescendingList() {
		verifyDropR( descendingTo1From( LIST_SIZE ) );
	}

	@Test
	public void testDropR_RandomList() {
		verifyDropR( randomized( LIST_SIZE ) );
	}

	@Test
	public void testFill() {
		verifyFill_All( List.with.elements( 1, 2, 3, 4, 5 ) );
		verifyFill_All( List.with.elements( 9, 7, 6, 4, 3, 1 ) );
	}

	@Test
	public void testFill_EnumStack() {
		verifyFill_All( List.numbers.fromTo( 1, 4 ).concat( List.with.elements( 6, 9, 2 ) ) );
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
	public void testTakeL_DescendingList() {
		verifyTakeL( descendingTo1From( LIST_SIZE ) );
	}

	@Test
	public void testTakeL_RandomList() {
		verifyTakeL( randomized( LIST_SIZE ) );
	}

	@Test
	public void testTakeR_DescendingFollowdByMixedList() {
		verifyTakeR( List.with.elements( 7, 1, 2, 11, 19, 11, 22, 11, 12 ) );
		verifyTakeR( List.with.elements( 7, 1, 2, 11, 19, 11, 22, 10, 11, 12 ) );
	}

	@Test
	public void testTakeR_DescendingList() {
		verifyTakeR( descendingTo1From( LIST_SIZE ) );
	}

	@Test
	public void testTakeR_RandomList() {
		verifyTakeR( randomized( LIST_SIZE ) );
	}

	@Test
	public void testTraverse() {
		verifyTraverseIterative( List.with.elements( 1, 4, 7, 2, 9 ) );
		verifyTraverseIterative( List.with.elements( 1, 2, 3, 4, 5 ) );
		verifyTraverseIterative( List.with.elements( 42, 20 ) );
		verifyTraverseIterative( List.with.elements( 42 ) );
		verifyTraverseIterative( List.with.<Integer> noElements() );
	}

	@Test ( expected = IndexOutOfBoundsException.class )
	public void testInsertAt_NegativeIndex() {
		List.with.noElements().insertAt( -1, 'a' );
	}

	@Test ( expected = IndexOutOfBoundsException.class )
	public void testInsertAt_ToHighIndex() {
		List.with.elements( 'a', 'b' ).insertAt( 4, 'c' );
	}

	@Test
	public void testInsertAt_FirstElementIntoAnEmptyList() {
		assertThat( noInts().insertAt( 0, 1 ), hasEqualElementsAsIn( 1 ) );
	}

	@Test
	public void testInsertAt_StartOfSingleElementList() {
		assertThat( List.with.element( 1 ).insertAt( 0, 2 ), hasEqualElementsAsIn( 2, 1 ) );
	}

	@Test
	public void testInsertAt_EndOfSingleElementList() {
		assertThat( List.with.element( 1 ).insertAt( 1, 2 ), hasEqualElementsAsIn( 1, 2 ) );
	}

	@Test
	public void testInsertAt_EndOfTwoElementsList() {
		assertThat( List.with.elements( 1, 2 ).insertAt( 2, 3 ), hasEqualElementsAsIn( 1, 2, 3 ) );
	}

	@Test
	public void testInsertAt_StartOfTwoElementsList() {
		assertThat( List.with.elements( 1, 2 ).insertAt( 0, 3 ), hasEqualElementsAsIn( 3, 1, 2 ) );
	}

	@Test
	public void testInsertAt_MiddleOfTwoElementsList() {
		assertThat( List.with.elements( 1, 2 ).insertAt( 1, 3 ), hasEqualElementsAsIn( 1, 3, 2 ) );
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
		assertThat( List.alterBy.dropRight( size ).from( l ).length(), is( 0 ) );
		assertThat( List.alterBy.dropRight( size + 1 ).from( l ).length(), is( 0 ) );
		assertThat( List.alterBy.dropRight( size - 1 ).from( l ).length(), is( 1 ) );
		for ( int i = 1; i < size; i++ ) {
			List<Integer> dropped = List.alterBy.dropRight( i ).from( l );
			assertThat( dropped.length(), is( size - i ) );
			assertThat( dropped.at( 0 ), is( l.at( 0 ) ) );
			int lastIndex = size - i - 1;
			assertThat( dropped.at( lastIndex ), is( l.at( lastIndex ) ) );
		}
	}

	private void verifyFill_All( List<Integer> l ) {
		Integer[] a = new Integer[l.length()];
		l.fill( 0, a, 0, l.length() );
		for ( int i = 0; i < a.length; i++ ) {
			assertThat( a[i], is( l.at( i ) ) );
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
		assertThat( List.alterBy.takeRight( size ).from( l ), sameInstance( l ) );
		assertThat( List.alterBy.takeRight( size + 1 ).from( l ), sameInstance( l ) );
		assertThat( List.alterBy.takeRight( size - 1 ).from( l ), not( sameInstance( l ) ) );
		for ( int i = 1; i < size; i++ ) {
			List<Integer> taken = List.alterBy.takeRight( i ).from( l );
			assertThat( taken.length(), is( i ) );
			assertThat( taken.at( 0 ), is( l.at( size - i ) ) );
		}
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

}
