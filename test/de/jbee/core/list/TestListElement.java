package de.jbee.core.list;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import de.jbee.core.type.Equal;

public class TestListElement {

	@Test
	public void testAtListElement() {
		List<Integer> l = List.with.elements( 5, 6, 7 );
		assertThat( List.element.at( 2 ).in( l ), is( 2 ) );
		assertThat( List.element.at( 0 ).in( l ), is( 0 ) );
		assertThat( List.element.at( -1 ).in( l ), is( -1 ) );
		assertThat( List.element.at( -5 ).in( l ), is( -1 ) );
		assertThat( List.element.at( 3 ).in( l ), is( -1 ) );
	}

	@Test
	public void testNthListElement() {
		List<Integer> l = List.with.elements( 3, 4, 3, 4, 3 );
		assertThat( List.element.nthEq( 1, 3, Equal.equality ).in( l ), is( 0 ) );
		assertThat( List.element.nthEq( 2, 3, Equal.equality ).in( l ), is( 2 ) );
		assertThat( List.element.nthEq( 3, 3, Equal.equality ).in( l ), is( 4 ) );
	}

	@Test
	public void testHeadListElement() {
		assertThat( List.head.in( List.with.elements( 42, 3, 5 ) ), is( 0 ) );
		assertThat( List.head.in( List.with.elements( 42, 3 ) ), is( 0 ) );
		assertThat( List.head.in( List.with.elements( 42 ) ), is( 0 ) );
		assertThat( List.head.in( List.with.noElements() ), is( -1 ) );
	}
}
