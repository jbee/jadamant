package de.jbee.core;

import org.junit.Test;

import de.jbee.core.list.List;

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
		List<Integer> l = List.numbers.fromTo( 3, 12 );
		System.out.println( l );
		List<Integer> l2 = List.numbers.fromTo( 12, 3 );
		System.out.println( l2 );
		//		List<Weekday> l3 = List.with.fromTo( Weekday.Monday, Weekday.Friday,
		//				Enumerate.type( Weekday.class ) );
		//		System.out.println( l3 );
		//		List<Weekday> l4 = List.with.fromTo( Weekday.Friday, Weekday.Tuesday,
		//				Enumerate.type( Weekday.class ) );
		//		System.out.println( l4 );
	}
}
