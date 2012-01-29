package de.jbee.lang.seq;

import static de.jbee.lang.seq.ListMatcher.hasEqualElementsAsIn;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import de.jbee.lang.Bag;
import de.jbee.lang.Order;

public class TestBag {

	@Test
	public void testAdd_OneEntry() {
		Bag<Character> b = emptyCharacterBag();
		b = b.add( 'c' );
		assertThat( b.length(), is( 1 ) );
		assertThat( b, hasEqualElementsAsIn( 'c' ) );
	}

	@Test
	public void testAdd_TwoEntries() {
		Bag<Character> b = emptyCharacterBag();
		b = b.add( 'c' );
		b = b.add( 'a' );
		assertThat( b.length(), is( 2 ) );
		assertThat( b, hasEqualElementsAsIn( 'a', 'c' ) );
	}

	@Test
	public void testAdd_ThreeEntries() {
		Bag<Character> b = emptyCharacterBag();
		b = b.add( 'c' );
		b = b.add( 'a' );
		b = b.add( 'e' );
		assertThat( b.length(), is( 3 ) );
		assertThat( b, hasEqualElementsAsIn( 'a', 'c', 'e' ) );
	}

	@Test
	public void testAdd_OneMultipleEntries() {
		Bag<Character> b = emptyCharacterBag();
		b = b.add( 'c' );
		b = b.add( 'c' );
		assertThat( b.length(), is( 2 ) );
		assertThat( b, hasEqualElementsAsIn( 'c', 'c' ) );
	}

	@Test
	public void testAdd_TwoMultipleEntries() {
		Bag<Character> b = emptyCharacterBag();
		b = b.add( 'c' );
		b = b.add( 'a' );
		b = b.add( 'a' );
		b = b.add( 'c' );
		assertThat( b.length(), is( 4 ) );
		assertThat( b, hasEqualElementsAsIn( 'a', 'a', 'c', 'c' ) );
	}

	@Test
	public void testAdd_ThreeMultipleEntries() {
		Bag<Character> b = emptyCharacterBag();
		b = b.add( 'c' );
		b = b.add( 'a' );
		b = b.add( 'a' );
		b = b.add( 'c' );
		b = b.add( 'b' );
		b = b.add( 'c' );
		assertThat( b.length(), is( 6 ) );
		assertThat( b, hasEqualElementsAsIn( 'a', 'a', 'b', 'c', 'c', 'c' ) );
	}

	@Test
	public void testIndexFor_MultipleEntries() {
		Bag<Character> b = emptyCharacterBag();
		b = b.add( 'c' );
		b = b.add( 'a' );
		b = b.add( 'a' );
		b = b.add( 'c' );
		b = b.add( 'b' );
		b = b.add( 'c' );
		assertThat( b.indexFor( 'a' ), is( 0 ) );
		assertThat( b.indexFor( 'b' ), is( 2 ) );
		assertThat( b.indexFor( 'c' ), is( 3 ) );
	}

	@Test
	public void testEntriesAt_MultipleEntries() {
		Bag<Character> b = emptyCharacterBag();
		b = b.add( 'c' );
		b = b.add( 'a' );
		b = b.add( 'a' );
		b = b.add( 'c' );
		b = b.add( 'b' );
		b = b.add( 'c' );
		assertThat( b.entriesAt( 0 ), hasEqualElementsAsIn( 'a', 'a' ) );
		assertThat( b.entriesAt( 1 ), hasEqualElementsAsIn( 'a', 'a' ) );
		assertThat( b.entriesAt( 2 ), hasEqualElementsAsIn( 'b' ) );
		assertThat( b.entriesAt( 4 ), hasEqualElementsAsIn( 'c', 'c', 'c' ) );
		assertThat( b.entriesAt( 5 ), hasEqualElementsAsIn( 'c', 'c', 'c' ) );
	}

	private Bag<Character> emptyCharacterBag() {
		return Bag.with.noElements( Order.typeaware( Order.abecedarian, Character.class ) );
	}
}
