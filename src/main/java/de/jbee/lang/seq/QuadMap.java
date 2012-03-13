package de.jbee.lang.seq;

/**
 * The basic idea:
 * 
 * Bits from highest (most significant - left) to lowest (least significant right)
 * 
 * <pre>
 * ???
 * </pre>
 * 
 * Use the most right 2 bits as first order value index. in case of a collision on that index we use
 * additional 3 bit as second order index to a collision Object array we put as value. (use special
 * class that knows and checks for collisions). Thereby we get 4x8 = 32 values per node max.
 * 
 * All other bits are interpreted as indexes to the deeper levels in groups of 2 bits (4 children).
 * 
 * Children and Values kept short (length 4) because those are the arrays that has to be copied on
 * the changed path (1 leaf node values, all layers above the children).
 * 
 * @author Jan Bernitt (jan.bernitt@gmx.de)
 * 
 */
public class QuadMap {

	static interface Node {

	}

	static class LeafNode
			implements Node {

		final Object[] eightValues;

		LeafNode( Object[] eightValues ) {
			super();
			this.eightValues = eightValues;
		}

	}

	static class FullNode
			implements Node {

		final Object[] eightValues;
		final Node[] fourChildren;

		FullNode( Object[] eightValues, Node[] fourChildren ) {
			super();
			this.eightValues = eightValues;
			this.fourChildren = fourChildren;
		}

	}
}
