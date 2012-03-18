package de.jbee.draft;

/**
 * The basic idea:
 * 
 * Bits from highest (most significant - left) to lowest (least significant right)
 * 
 * <pre>
 * ???
 * </pre>
 * 
 * @author Jan Bernitt (jan.bernitt@gmx.de)
 * 
 */
public class OctetMap {

	/**
	 * We clone this when creating new node arrays so that all cells contain the empty node
	 * constant.
	 */
	private static Node[] EIGHT_EMPTY_NODES = new Node[8]; //TODO fill with 8 empty nodes

	private static Node[] FOUR_EMPTY_NODES = new Node[4]; //TODO fill with 8 empty nodes

	static interface NodeEdit {

		Node newChildFrom( Node leaf );
	}

	static interface Node {

		Node edit( NodeEdit edit, int hash );
	}

	/**
	 * Has 4 groups with 1-8 members each. Separated to reduce copied memory.
	 * 
	 * All members are at the exact index given by the 5 level bits. This is just divided into 2
	 * indices. The most right 2 bits are the group bits. The following 3 bits are the index in the
	 * group.
	 * 
	 * <pre>
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * int group = hash &amp; GROUP_MASK;
	 * int memberIndex = ( hash &gt;&gt; 2 ) &amp; INDEX_MASK;
	 * </pre>
	 * 
	 * @author Jan Bernitt (jan.bernitt@gmx.de)
	 * 
	 */
	static class Node32 {

	}

	/**
	 * Has 3 groups with 1-8 members each.
	 * 
	 * As son as the left our group gets a member it will turn into a {@link Node32}.
	 * 
	 * It works the same way as a {@link Node32} but just needs to check if a insert affects group
	 * not covered yet. In that case it has to turn into a {@link Node32} otherwise it stays a
	 * {@link Node16}.
	 * 
	 * To do this check we maybe prepare a mask having 8 1-bits in a row. This is shifted 0-3 times
	 * depending on the index of the insert and than AND combined with the node#s mask. A result of
	 * zero indicates that the uncovered 8 bits are affected.
	 * 
	 * @author Jan Bernitt (jan.bernitt@gmx.de)
	 * 
	 */
	static class Node24 {

	}

	/**
	 * Has 2 groups with 1-8 members ?
	 * 
	 * As son as one of the 2 left out groups gets a member it will turn into a {@link Node24} .
	 * 
	 * @author Jan Bernitt (jan.bernitt@gmx.de)
	 * 
	 */
	static class Node16 {

	}

	/**
	 * Has 2 groups with 1-4 members each.
	 * 
	 * As son as another group is required it turns into a {@link Node16}.
	 * 
	 * @author Jan Bernitt (jan.bernitt@gmx.de)
	 */
	static class Node8 {

	}

	/**
	 * Has 1 group of 1-4 elements.
	 * 
	 * As son as a 5th members is inserted it will turn into a {@link Node8}.
	 * 
	 * As always the bitmask has a 1-bit at the position of each of the members.
	 * 
	 * @author Jan Bernitt (jan.bernitt@gmx.de)
	 */
	static class Node4 {

	}

	/**
	 * Carries one key/value pair or whatever.
	 * 
	 * For a List it could just have the value as long as the highest 2 bits are zero because those
	 * are not covered by the path. The index is implicit know than.
	 * 
	 * @author Jan Bernitt (jan.bernitt@gmx.de)
	 * 
	 */
	static class LeafNode {

	}

}
