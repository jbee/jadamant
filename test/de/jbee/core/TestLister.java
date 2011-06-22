package de.jbee.core;

import static de.jbee.core.type.Enumerate.INTEGERS;
import static de.jbee.core.type.Enumerate.numbers;
import static de.jbee.core.type.Enumerate.stepwise;

import org.junit.Test;

import de.jbee.core.list.List;
import de.jbee.core.list.UtileEnumerator;

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

		UtileEnumerator<Integer> every3 = Core.enumerator.enumerates( stepwise( INTEGERS, 3, 3 ) );
		System.out.println( every3.fromTo( 3, 12 ) );

		System.out.println( List.numbers.stepwiseFromTo( 3, 12, 4 ) );
		System.out.println( List.numbers.fromThenTo( 3, 5, 12 ) );
		System.out.println( List.numbers.fromThenTo( 12, 9, 3 ) );
		System.out.println( List.numbers.fromThenTo( 11, 9, 2 ) );

		UtileEnumerator<Integer> zeroToTen = Core.enumerator.enumerates( numbers( 0, 10 ) );
		System.out.println( zeroToTen.from( 3 ) );
		System.out.println( zeroToTen.fromThen( 2, 4 ) );
		System.out.println( zeroToTen.stepwisefrom( 1, 3 ) );

		UtileEnumerator<Weekday> weekdays = List.that.enumerates( Weekday.class );
		List<Weekday> l3 = weekdays.fromTo( Weekday.Monday, Weekday.Friday );
		System.out.println( l3 );
		List<Weekday> l4 = weekdays.fromTo( Weekday.Friday, Weekday.Tuesday );
		System.out.println( l4 );
	}
}
