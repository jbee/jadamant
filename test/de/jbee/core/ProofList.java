package de.jbee.core;

import static de.jbee.core.Core.$;
import static de.jbee.core.Core.I;
import static de.jbee.core.Core._;
import static de.jbee.core.list.ListTransition.reverse;
import static de.jbee.core.list.ListTransition.tail;

import org.junit.Test;

import de.jbee.core.list.List;
import de.jbee.core.list.ListTransition;

public class ProofList {

	public void proofStaticListSyntax() {
		List<Integer> empty = _();
		List<Integer> one = _( 1 );
		List<Integer> many = _( 1, 2, 4 );
		List<List<Integer>> multi = _( _( 1, 4 ), _( 2, 3 ) );
	}

	public void proofDeleteOverride() {
		List<Integer> l = I().deleteAt( 0 );
	}

	public void proofDropLOverride() {
		List<Integer> l = I().dropL( 2 );
	}

	public void proofDropROverride() {
		List<Integer> l = I().dropR( 2 );
	}

	public void proofInsertOverride() {
		List<Integer> l = I().insertAt( 1, 2 );
	}

	public void proofPrepandOverride() {
		List<Integer> l = I().prepand( 1 );
	}

	public void proofTakeLOverride() {
		List<Integer> l = I().takeL( 2 );
	}

	public void proofTakeROverride() {
		List<Integer> l = I().takeR( 2 );
	}

	public void proofFactoryAccess() {
		List<Integer> l1 = List.with.element( 1 );
		List<Integer> l2 = List.with.elements( 1, 2, 3, 4 );
		List<Integer> l4 = List.with.noElements();
	}

	@Test
	public void proofListTransitionChaining() {
		ListTransition dropLR1 = $( tail, $( reverse, tail ) );
		System.out.println( dropLR1.from( List.numbers.fromTo( 1, 10 ) ) );
	}

}
