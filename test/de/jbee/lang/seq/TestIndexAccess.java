package de.jbee.lang.seq;

import static junit.framework.Assert.assertFalse;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

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
}
