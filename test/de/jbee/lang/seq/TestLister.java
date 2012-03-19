package de.jbee.lang.seq;

import static de.jbee.lang.Enumerate.INTEGERS;
import static de.jbee.lang.Enumerate.numbers;
import static de.jbee.lang.Enumerate.stepwise;
import static de.jbee.lang.seq.ListMatcher.hasEqualElementsAsIn;
import static de.jbee.lang.seq.Sequences.list;
import static de.jbee.lang.seq.TestLister.Weekday.Friday;
import static de.jbee.lang.seq.TestLister.Weekday.Monday;
import static de.jbee.lang.seq.TestLister.Weekday.Saturday;
import static de.jbee.lang.seq.TestLister.Weekday.Sunday;
import static de.jbee.lang.seq.TestLister.Weekday.Thursday;
import static de.jbee.lang.seq.TestLister.Weekday.Tuesday;
import static de.jbee.lang.seq.TestLister.Weekday.Wednesday;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import de.jbee.lang.List;

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
		assertThat( List.numbers.fromTo( 3, 12 ), hasEqualElementsAsIn( 3, 4, 5, 6, 7, 8, 9, 10,
				11, 12 ) );
		assertThat( List.numbers.fromTo( 12, 3 ), hasEqualElementsAsIn( 12, 11, 10, 9, 8, 7, 6, 5,
				4, 3 ) );

		Range<Integer> every3 = List.rangeTo.enumerate( stepwise( INTEGERS, 3, 3 ) );
		assertThat( every3.fromTo( 3, 12 ), hasEqualElementsAsIn( 3, 6, 9, 12 ) );

		assertThat( List.numbers.stepwiseFromTo( 3, 12, 4 ), hasEqualElementsAsIn( 3, 7, 11 ) );
		assertThat( List.numbers.fromThenTo( 3, 5, 12 ), hasEqualElementsAsIn( 3, 5, 7, 9, 11 ) );
		assertThat( List.numbers.fromThenTo( 12, 9, 3 ), hasEqualElementsAsIn( 12, 9, 6, 3 ) );
		assertThat( List.numbers.fromThenTo( 11, 9, 2 ), hasEqualElementsAsIn( 11, 9, 7, 5, 3 ) );

		Range<Integer> zeroToTen = List.rangeTo.enumerate( numbers( 0, 10 ) );
		assertThat( zeroToTen.from( 3 ), hasEqualElementsAsIn( 3, 4, 5, 6, 7, 8, 9, 10 ) );
		assertThat( zeroToTen.fromThen( 2, 4 ), hasEqualElementsAsIn( 2, 4, 6, 8, 10 ) );
		assertThat( zeroToTen.stepwisefrom( 1, 3 ), hasEqualElementsAsIn( 1, 4, 7, 10 ) );
		assertThat( zeroToTen.stepwiseFromTo( 1, 7, 3 ), hasEqualElementsAsIn( 1, 4, 7 ) );

		assertThat( List.letters.from( 'T' ), hasEqualElementsAsIn( 'T', 'U', 'V', 'W', 'X', 'Y',
				'Z' ) );
		assertThat( List.letters.fromTo( 'B', 'F' ), hasEqualElementsAsIn( 'B', 'C', 'D', 'E', 'F' ) );
		assertThat( List.letters.stepwisefrom( 'S', 3 ), hasEqualElementsAsIn( 'S', 'V', 'Y' ) );

		assertThat( List.characters.fromTo( 'C', 'A' ), hasEqualElementsAsIn( 'C', 'B', 'A' ) );
	}

	@Test
	public void fromToShouldWorkWithAllEnums() {
		Range<Weekday> weekdays = List.rangeTo.enumerate( Weekday.class );

		assertThat( weekdays.fromTo( Monday, Friday ), //
				hasEqualElementsAsIn( Monday, Tuesday, Wednesday, Thursday, Friday ) );
		assertThat( weekdays.fromTo( Friday, Tuesday ), // 
				hasEqualElementsAsIn( Friday, Thursday, Wednesday, Tuesday ) );

		assertThat( weekdays.fromToCircular( Friday, Tuesday ), //
				hasEqualElementsAsIn( Friday, Saturday, Sunday, Monday, Tuesday ) );
		assertThat( weekdays.fromToCircular( Friday, Friday ), //
				hasEqualElementsAsIn( Friday, Saturday, Sunday, Monday, Tuesday, Wednesday,
						Thursday, Friday ) );
		assertThat( weekdays.fromToCircular( Tuesday, Friday ), //
				hasEqualElementsAsIn( Tuesday, Monday, Sunday, Saturday, Friday ) );
	}

}
