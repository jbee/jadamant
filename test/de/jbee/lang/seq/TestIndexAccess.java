package de.jbee.lang.seq;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.Iterator;

import org.junit.Test;

import de.jbee.lang.List;

public class TestIndexAccess {

	@Test
	public void forwardsIteratorShouldStartWithTheFirstElement() {
		Iterator<Integer> forwards = List.iterate.forwards( List.with.elements( 1, 2 ) ).iterator();
		assertThat( forwards.next(), is( 1 ) );
		assertThat( forwards.next(), is( 2 ) );
		assertFalse( forwards.hasNext() );
	}

	@Test
	public void forwardsIteratorShouldWorkWithPartialLists() {
		List<Integer> l = List.with.elements( 1, 2 );
		l = l.concat( List.with.elements( 3, 4 ) );
		Iterator<Integer> forwards = List.iterate.forwards( l ).iterator();
		for ( int i = 0; i < l.length(); i++ ) {
			assertTrue( forwards.hasNext() );
			assertThat( forwards.next(), is( i + 1 ) );
		}
		assertFalse( forwards.hasNext() );
	}

}
