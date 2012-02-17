package de.jbee.lang.seq;

import static de.jbee.lang.Enumerate.INTEGERS;
import static de.jbee.lang.Enumerate.numbers;
import static de.jbee.lang.Enumerate.stepwise;
import static de.jbee.lang.seq.ListMatcher.hasEqualElementsAsIn;
import static de.jbee.lang.seq.Sequences.list;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import de.jbee.lang.List;
import de.jbee.lang.Sequence;

public class TestLister {

	@Test
	public void testStaticListUpTo6Elements() {
		assertThat( list( 12 ), hasEqualElementsAsIn( 12 ) );
		assertThat( list( 11, 12 ), hasEqualElementsAsIn( 11, 12 ) );
		assertThat( list( 22, 11, 12 ), hasEqualElementsAsIn( 22, 11, 12 ) );
		assertThat( list( 11, 22, 11, 12 ), hasEqualElementsAsIn( 11, 22, 11, 12 ) );
		assertThat( list( 19, 11, 22, 11, 12 ), hasEqualElementsAsIn( 19, 11, 22, 11, 12 ) );
		assertThat( list( 11, 19, 11, 22, 11, 12 ), hasEqualElementsAsIn( 11, 19, 11, 22, 11, 12 ) );
	}

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
		assertThat( l.length(), is( 5 ) );
		assertThat( l.at( 0 ), is( 'H' ) );
		assertThat( l.at( 1 ), is( 'e' ) );
		assertThat( l.at( 2 ), is( 'l' ) );
		assertThat( l.at( 3 ), is( 'l' ) );
		assertThat( l.at( 4 ), is( 'o' ) );
	}

	@Test
	public void testLinesIn() {
		List<String> l = List.with.linesIn( "Hello\nMr. Fancy Pancy!\nClown" );
		assertThat( l.length(), is( 3 ) );
		assertThat( l.at( 0 ), is( "Hello" ) );
		assertThat( l.at( 1 ), is( "Mr. Fancy Pancy!" ) );
		assertThat( l.at( 2 ), is( "Clown" ) );
	}

	@Test
	public void testWordsIn() {
		List<String> l = List.with.wordsIn( "This is the end!\nMy only friend - the end" );
		assertThat( l.length(), is( 10 ) );
		assertThat( l.at( 0 ), is( "This" ) );
		assertThat( l.at( 1 ), is( "is" ) );
		assertThat( l.at( 2 ), is( "the" ) );
		assertThat( l.at( 3 ), is( "end!" ) );
		assertThat( l.at( 4 ), is( "My" ) );
		assertThat( l.at( 5 ), is( "only" ) );
		assertThat( l.at( 6 ), is( "friend" ) );
		assertThat( l.at( 7 ), is( "-" ) );
		assertThat( l.at( 8 ), is( "the" ) );
		assertThat( l.at( 9 ), is( "end" ) );
	}

	@Test
	public void testEnumListIsUsedForIntegers() {
		assertThat( List.numbers.fromThenTo( 1, 2, 4 ), instanceOf( EnumList.class ) );
	}

	@Test
	public void testFromTo() {
		List<Integer> l = List.numbers.fromTo( 3, 12 );
		System.out.println( l );
		List<Integer> l2 = List.numbers.fromTo( 12, 3 );
		System.out.println( l2 );

		Range<Integer> every3 = List.rangeTo.enumerate( stepwise( INTEGERS, 3, 3 ) );
		System.out.println( every3.fromTo( 3, 12 ) );

		System.out.println( List.numbers.stepwiseFromTo( 3, 12, 4 ) );
		System.out.println( List.numbers.fromThenTo( 3, 5, 12 ) );
		System.out.println( List.numbers.fromThenTo( 12, 9, 3 ) );
		System.out.println( List.numbers.fromThenTo( 11, 9, 2 ) );

		Range<Integer> zeroToTen = List.rangeTo.enumerate( numbers( 0, 10 ) );
		System.out.println( zeroToTen.from( 3 ) );
		System.out.println( zeroToTen.fromThen( 2, 4 ) );
		System.out.println( zeroToTen.stepwisefrom( 1, 3 ) );
		System.out.println( zeroToTen.stepwiseFromTo( 1, 6, 3 ) );

		System.out.println( List.letters.from( 'B' ) );
		System.out.println( List.letters.fromTo( 'B', 'F' ) );
		System.out.println( List.letters.stepwisefrom( 'A', 3 ) );

		System.out.println( List.characters.fromTo( 'C', '0' ) );

		Range<Weekday> weekdays = List.rangeTo.enumerate( Weekday.class );
		List<Weekday> l3 = weekdays.fromTo( Weekday.Monday, Weekday.Friday );
		System.out.println( l3 );
		List<Weekday> l4 = weekdays.fromTo( Weekday.Friday, Weekday.Tuesday );
		System.out.println( l4 );
		System.out.println( weekdays.fromToCircular( Weekday.Friday, Weekday.Tuesday ) );
		System.out.println( weekdays.fromToCircular( Weekday.Friday, Weekday.Friday ) );
		System.out.println( weekdays.fromToCircular( Weekday.Tuesday, Weekday.Friday ) );
	}

	@Test
	public void testElementsCluster() {
		List<Integer> l = List.with.elements( new Sequence<Integer>() {

			@Override
			public boolean isEmpty() {
				return false;
			}

			@Override
			public int length() {
				return 3;
			}

			@Override
			public Integer at( int index ) {
				return new int[] { 1, 2, 3 }[index];
			}

		} );
		assertThat( l.length(), is( 3 ) );
		assertThat( l.at( 0 ), is( 1 ) );
		assertThat( l.at( 1 ), is( 2 ) );
		assertThat( l.at( 2 ), is( 3 ) );
	}
}
