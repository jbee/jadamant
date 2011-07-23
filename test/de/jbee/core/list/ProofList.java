package de.jbee.core.list;

import static de.jbee.core.Core.I;
import static de.jbee.core.Core._;
import static de.jbee.core.Core.ſ;
import static de.jbee.core.list.ListTransition.reverse;
import static de.jbee.core.list.ListTransition.tail;

import org.junit.Test;

public class ProofList {

	@Test
	public void proofStaticListSyntax() {
		List<Integer> empty = _();
		List<Integer> one = _( 1 );
		List<Integer> many = _( 1, 2, 4 );
		List<List<Integer>> multi = _( _( 1, 4 ), _( 2, 3 ) );
		System.out.println( multi );
	}

	public void proofDeleteOverride() {
		List<Integer> l = I().deleteAt( 0 );
	}

	public void proofDropLOverride() {
		List<Integer> l = I().drop( 2 );
	}

	public void proofInsertOverride() {
		List<Integer> l = I().insertAt( 1, 2 );
	}

	public void proofPrepandOverride() {
		List<Integer> l = I().prepand( 1 );
	}

	public void proofTakeLOverride() {
		List<Integer> l = I().take( 2 );
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