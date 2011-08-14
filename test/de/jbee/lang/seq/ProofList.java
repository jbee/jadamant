package de.jbee.lang.seq;

import static de.jbee.lang.Lang.list;
import static de.jbee.lang.Lang.noInts;

import org.junit.Test;

import de.jbee.lang.List;

public class ProofList {

	@Test
	public void proofStaticListSyntax() {
		List<Integer> empty = list();
		List<Integer> one = list( 1 );
		List<Integer> many = list( 1, 2, 4 );
		List<List<Integer>> multi = list( list( 1, 4 ), list( 2, 3 ) );
		System.out.println( multi );
	}

	public void proofDeleteOverride() {
		List<Integer> l = noInts().deleteAt( 0 );
	}

	public void proofDropLOverride() {
		List<Integer> l = noInts().drop( 2 );
	}

	public void proofInsertOverride() {
		List<Integer> l = noInts().insertAt( 1, 2 );
	}

	public void proofPrepandOverride() {
		List<Integer> l = noInts().prepand( 1 );
	}

	public void proofTakeLOverride() {
		List<Integer> l = noInts().take( 2 );
	}

	public void proofFactoryAccess() {
		List<Integer> l1 = List.with.element( 1 );
		List<Integer> l2 = List.with.elements( 1, 2, 3, 4 );
		List<Integer> l4 = List.with.noElements();
		List<Character> lc = List.with.charactersIn( "Hello" );
	}

}
