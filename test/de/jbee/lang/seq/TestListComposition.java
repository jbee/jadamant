package de.jbee.lang.seq;

import static de.jbee.lang.seq.Sequences.depth;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.Random;

import org.junit.Test;

import de.jbee.lang.List;

/**
 * Tests validating the correct use of the different {@link List} implementations.
 * 
 * @author Jan Bernitt (jan.bernitt@gmx.de)
 */
public class TestListComposition {

	@Test
	public void appendingToASingleElementListShouldNotChainTwoSingleElementLists() {
		List<Integer> l = ElementaryList.element( 1 );
		assertThat( l.append( 2 ), not( instanceOf( ElementaryList.class ) ) );
	}

	@Test
	public void appendShouldUseSubsequencesGrowingWithThePowerOf2() {
		Random rnd = new Random();
		List<Integer> l = ElementaryList.element( 1 );
		for ( int i = 0; i < 100; i++ ) {
			l = l.append( rnd.nextInt( 100 ) );
		}
		int depth = depth( l );
		assertTrue( depth <= 7 ); // because 2^7 -> 128 and we use a reversed prepanded list
	}

	@Test
	public void prepandingToASingleElementListShouldNotChainTwoSingleElementLists() {
		List<Integer> l = ElementaryList.element( 1 );
		assertThat( l.prepand( 2 ), not( instanceOf( ElementaryList.class ) ) );
	}

	@Test
	public void prepandShouldUseSubsequencesGrowingWithThePowerOf2() {
		Random rnd = new Random();
		List<Integer> l = ElementaryList.element( 1 );
		for ( int i = 0; i < 100; i++ ) {
			l = l.prepand( rnd.nextInt( 100 ) );
		}
		assertTrue( depth( l ) <= 7 ); // because 2^7 -> 128
	}
}
