package de.jbee.core.list;

import static de.jbee.core.type.Enumerate.INTEGERS;
import static de.jbee.core.type.Enumerate.numbers;
import static de.jbee.core.type.Enumerate.stepwise;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

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
	public void testCharactersIn() {
		List<Character> l = List.with.charactersIn( "Hello" );
		assertThat( 5, is( l.size() ) );
		assertThat( 'H', is( l.at( 0 ) ) );
		assertThat( 'e', is( l.at( 1 ) ) );
		assertThat( 'l', is( l.at( 2 ) ) );
		assertThat( 'l', is( l.at( 3 ) ) );
		assertThat( 'o', is( l.at( 4 ) ) );
	}

	@Test
	public void testFromTo() {
		List<Integer> l = List.numbers.fromTo( 3, 12 );
		System.out.println( l );
		List<Integer> l2 = List.numbers.fromTo( 12, 3 );
		System.out.println( l2 );

		UtileEnumerator<Integer> every3 = List.does.enumerates( stepwise( INTEGERS, 3, 3 ) );
		System.out.println( every3.fromTo( 3, 12 ) );

		System.out.println( List.numbers.stepwiseFromTo( 3, 12, 4 ) );
		System.out.println( List.numbers.fromThenTo( 3, 5, 12 ) );
		System.out.println( List.numbers.fromThenTo( 12, 9, 3 ) );
		System.out.println( List.numbers.fromThenTo( 11, 9, 2 ) );

		UtileEnumerator<Integer> zeroToTen = List.does.enumerates( numbers( 0, 10 ) );
		System.out.println( zeroToTen.from( 3 ) );
		System.out.println( zeroToTen.fromThen( 2, 4 ) );
		System.out.println( zeroToTen.stepwisefrom( 1, 3 ) );
		System.out.println( zeroToTen.stepwiseFromTo( 1, 6, 3 ) );

		System.out.println( List.letters.from( 'B' ) );
		System.out.println( List.letters.fromTo( 'B', 'F' ) );
		System.out.println( List.letters.stepwisefrom( 'A', 3 ) );

		System.out.println( List.characters.fromTo( 'C', '0' ) );

		UtileEnumerator<Weekday> weekdays = List.does.enumerates( Weekday.class );
		List<Weekday> l3 = weekdays.fromTo( Weekday.Monday, Weekday.Friday );
		System.out.println( l3 );
		List<Weekday> l4 = weekdays.fromTo( Weekday.Friday, Weekday.Tuesday );
		System.out.println( l4 );
	}

	@Test
	public void testNextHighestPowerOf2() {
		assertThat( StackList.nextHighestPowerOf2( 1 ), is( 1 ) );
		assertThat( StackList.nextHighestPowerOf2( 2 ), is( 2 ) );
		assertThat( StackList.nextHighestPowerOf2( 3 ), is( 4 ) );
		assertThat( StackList.nextHighestPowerOf2( 4 ), is( 4 ) );
		assertThat( StackList.nextHighestPowerOf2( 5 ), is( 8 ) );
		assertThat( StackList.nextHighestPowerOf2( 6 ), is( 8 ) );
		assertThat( StackList.nextHighestPowerOf2( 7 ), is( 8 ) );
		assertThat( StackList.nextHighestPowerOf2( 8 ), is( 8 ) );
		assertThat( StackList.nextHighestPowerOf2( 9 ), is( 16 ) );
	}

	@Test
	public void testElementsCluster() {
		List<Integer> l = List.with.elements( de.jbee.util.List.readonly1( 1, 2, 3 ) );
		assertThat( l.size(), is( 3 ) );
		assertThat( l.at( 0 ), is( 1 ) );
		assertThat( l.at( 1 ), is( 2 ) );
		assertThat( l.at( 2 ), is( 3 ) );
	}
}
