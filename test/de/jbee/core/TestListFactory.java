package de.jbee.core;

import org.junit.Test;

import de.jbee.core.list.List;
import de.jbee.core.type.Enumerate;

public class TestListFactory {

	static enum Weekday {
		Monday,
		Tuesday,
		Wednesday,
		Thursday,
		Friday,
		Saturday,
		Sunday
	}

	@Test
	public void testFromTo() {
		List<Integer> l = List.with.fromTo( 3, 12, Enumerate.INTEGERS );
		System.out.println( l );
		List<Integer> l2 = List.with.fromTo( 12, 3, Enumerate.INTEGERS );
		System.out.println( l2 );
		List<Weekday> l3 = List.with.fromTo( Weekday.Monday, Weekday.Friday,
				Enumerate.type( Weekday.class ) );
		System.out.println( l3 );
		List<Weekday> l4 = List.with.fromTo( Weekday.Friday, Weekday.Tuesday,
				Enumerate.type( Weekday.class ) );
		System.out.println( l4 );

	}
}
