package de.jbee.lang.seq;

import de.jbee.lang.Bag;
import de.jbee.lang.List;
import de.jbee.lang.Lister;
import de.jbee.lang.Ord;
import de.jbee.lang.Order;
import de.jbee.lang.Ordered;
import de.jbee.lang.Sequence;
import de.jbee.lang.Set;

public class UtileSetLister
		implements Lister.SetLister {

	public <E> Set<E> elements( List<E> elems ) {
		return elements( Order.inherent, elems );
	}

	@Override
	public <E> Set<E> elements( Ord<Object> order, List<E> elems ) {
		return OrderedList.setOf( refinedToSetConstraints( elems, order ), order );
	}

	@Override
	public <E> Set<E> noElements() {
		return elements( List.with.<E> noElements() );
	}

	@Override
	public <E> Set<E> element( E e ) {
		return OrderedList.setOf( List.with.element( e ), Order.inherent );
	}

	@Override
	public <E> Set<E> elements( E... elems ) {
		return elements( List.with.elements( elems ) );
	}

	@Override
	public <E> Set<E> elements( Sequence<E> elems ) {
		return elements( Order.inheritFrom( elems ), List.with.elements( elems ) );
	}

	@Override
	public <E> Set<E> noElements( Ord<Object> order ) {
		return elements( order, List.with.<E> noElements() );
	}

	private <E> List<E> refinedToSetConstraints( List<E> elems, Ord<Object> order ) {
		if ( elems.length() <= 1 ) {
			return elems;
		}
		if ( elems instanceof Ordered ) {
			final boolean inheritOrder = order == Order.inherent;
			final Ord<Object> seqOrder = ( (Ordered) elems ).order();
			if ( elems instanceof Set<?> ) {
				if ( inheritOrder || order == seqOrder ) {
					return elems;
				}
			}
			if ( elems instanceof Bag<?> ) {
				if ( inheritOrder || order == seqOrder ) {
					return List.alterBy.nubBy( order ).in( elems );
				}
			}
			if ( inheritOrder ) {
				order = seqOrder;
			}
		}
		return List.alterBy.nubBy( order ).in( List.alterBy.sortBy( order ).in( elems ) );
	}

}
