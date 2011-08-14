package de.jbee.core.list;

import static de.jbee.core.Core.noInt;
import static de.jbee.core.Core.list;
import static de.jbee.core.Core.list;
import static de.jbee.core.Core.ſ;
import static de.jbee.core.list.ListTransition.reverse;
import static de.jbee.core.list.ListTransition.tail;

import org.junit.Test;

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
		List<Integer> l = noInt().deleteAt( 0 );
	}

	public void proofDropLOverride() {
		List<Integer> l = noInt().drop( 2 );
	}

	public void proofInsertOverride() {
		List<Integer> l = noInt().insertAt( 1, 2 );
	}

	public void proofPrepandOverride() {
		List<Integer> l = noInt().prepand( 1 );
	}

	public void proofTakeLOverride() {
		List<Integer> l = noInt().take( 2 );
	}

	public void proofFactoryAccess() {
		List<Integer> l1 = List.with.element( 1 );
		List<Integer> l2 = List.with.elements( 1, 2, 3, 4 );
		List<Integer> l4 = List.with.noElements();
		List<Character> lc = List.with.charactersIn( "Hello" );
	}

	@Test
	public void proofListTransitionChaining() {
		ListTransition dropLR1 = ſ( tail, ſ( reverse, tail ) );
		System.out.println( dropLR1.from( List.numbers.fromTo( 1, 10 ) ) );
	}

}
