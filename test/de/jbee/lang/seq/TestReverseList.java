package de.jbee.lang.seq;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import de.jbee.lang.List;
import de.jbee.lang.ListAlteration;

public class TestReverseList {

	@Test
	public void testAppend() {
		List<Integer> l = List.alterBy.reverse.in( List.with.<Integer> noElements() );
		for ( int i = 1; i < 100; i++ ) {
			l = l.append( i );
			assertThat( i, is( l.length() ) );
			for ( int j = 0; j < i; j++ ) {
				assertThat( j + 1, is( l.at( j ) ) );
			}
		}
	}

	@Test
	public void testReverseTransition() {
		List<Integer> expected = List.with.elements( 4, 3, 2, 1 );
		List<Integer> actual = List.alterBy.reverse.in( List.with.elements( 1, 2, 3, 4 ) );
		for ( int i = 0; i < expected.length(); i++ ) {
			assertThat( actual.at( i ), is( expected.at( i ) ) );
		}
	}

	@Test
	public void testConcatTransition() {
		List<Integer> expected = List.with.elements( 3, 2, 1 );
		ListAlteration reverseTail = List.alterBy.chain( List.alterBy.reverse, List.alterBy.tail );
		List<Integer> actual = reverseTail.in( List.with.elements( 1, 2, 3, 4 ) );
		for ( int i = 0; i < expected.length(); i++ ) {
			assertThat( actual.at( i ), is( expected.at( i ) ) );
		}
	}
}
