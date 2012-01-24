package de.jbee.lang.seq;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.Random;

import org.junit.Test;

import de.jbee.lang.List;

/**
 * Tests validating especially the {@link ElementaryList} implementation.
 * 
 * @author Jan Bernitt (jan.bernitt@gmx.de)
 */
public class TestElementList {

	@Test
	public void testAppend_NoCreationOfLinkedListOnSecondElement() {
		List<Integer> l = ElementaryList.with( 1 );
		assertThat( l.append( 2 ), not( instanceOf( ElementaryList.class ) ) );
	}

	@Test
	public void testAppend_NoCreationOfLinkedList() {
		Random rnd = new Random();
		List<Integer> l = ElementaryList.with( 1 );
		for ( int i = 0; i < 100; i++ ) {
			l = l.append( rnd.nextInt( 100 ) );
		}
		List<Integer> s = l.subsequent();
		int parts = 1;
		while ( !s.isEmpty() ) {
			s = s.subsequent();
			parts++;
		}
		assertTrue( parts <= 51 ); //TODO reduce this number!
	}

	@Test
	public void testPrepand_NoCreationOfLinkedListOnSecondElement() {
		List<Integer> l = ElementaryList.with( 1 );
		assertThat( l.prepand( 2 ), not( instanceOf( ElementaryList.class ) ) );
	}

	@Test
	public void testPrepand_NoCreationOfLinkedList() {
		Random rnd = new Random();
		List<Integer> l = ElementaryList.with( 1 );
		for ( int i = 0; i < 100; i++ ) {
			l = l.prepand( rnd.nextInt( 100 ) );
		}
		List<Integer> s = l.subsequent();
		int parts = 1;
		while ( !s.isEmpty() ) {
			s = s.subsequent();
			parts++;
		}
		assertTrue( parts <= 7 );
	}
}
