package de.jbee.core;

import org.junit.Test;

import de.jbee.core.list.List;
import de.jbee.core.list.RichLister;
import de.jbee.core.type.Enumerate;

public class TestLister {

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

		RichLister<Integer> every3 = Core.lister.listing( Enumerate.stepwise( Enumerate.INTEGERS,
				3, 3 ) );
		System.out.println( every3.fromTo( 3, 12 ) );

		System.out.println( List.numbers.stepwiseFromTo( 3, 12, 4 ) );
		System.out.println( List.numbers.fromThenTo( 3, 5, 12 ) );
		System.out.println( List.numbers.fromThenTo( 12, 9, 3 ) );
		System.out.println( List.numbers.fromThenTo( 11, 9, 2 ) );

		RichLister<Integer> zeroToTen = Core.lister.listing( Enumerate.numbers( 0, 10 ) );
		System.out.println( zeroToTen.from( 3 ) );
		System.out.println( zeroToTen.fromThen( 2, 4 ) );
		System.out.println( zeroToTen.stepwisefrom( 1, 3 ) );

		RichLister<Weekday> weekdaylister = Core.lister.listing( Enumerate.type( Weekday.class ) );
		List<Weekday> l3 = weekdaylister.fromTo( Weekday.Monday, Weekday.Friday );
		System.out.println( l3 );
		List<Weekday> l4 = weekdaylister.fromTo( Weekday.Friday, Weekday.Tuesday );
		System.out.println( l4 );
	}
}
