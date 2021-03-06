package de.jbee.lang.seq;

import de.jbee.lang.Array;
import de.jbee.lang.Bag;
import de.jbee.lang.List;
import de.jbee.lang.Ord;
import de.jbee.lang.Order;
import de.jbee.lang.Ordered;
import de.jbee.lang.Sequence;

class UtileBagLister
		implements Bag.BagLister {

	@Override
	public <E> Bag<E> element( E e ) {
		return OrderedList.bagOf( List.with.element( e ), Order.inherent );
	}

	@Override
	public <E> Bag<E> elements( E... elems ) {
		return elements( Array.sequence( elems ) );
	}

	@Override
	public <E> Bag<E> elements( Sequence<E> elems ) {
		return elements( Order.inheritFrom( elems ), List.with.elements( elems ) );
	}

	@Override
	public <E> Bag<E> noElements() {
		return noElements( Order.inherent );
	}

	@Override
	public <E> Bag<E> noElements( Ord<Object> order ) {
		return OrderedList.bagOf( List.with.<E> noElements(), order );
	}

	@Override
	public <E> Bag<E> elements( Ord<Object> order, List<E> elems ) {
		return OrderedList.bagOf( refinedToBagConstraints( elems, order ), order );
	}

	private <E> List<E> refinedToBagConstraints( List<E> elems, Ord<Object> order ) {
		if ( elems.length() <= 1 ) {
			return elems;
		}
		if ( elems instanceof Ordered ) {
			final boolean inheritOrder = order == Order.inherent;
			final Ord<Object> seqOrder = ( (Ordered) elems ).order();
			if ( inheritOrder || order == seqOrder ) {
				return elems;
			}
		}
		return List.alterBy.sortBy( order ).from( elems );
	}
}
