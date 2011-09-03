package de.jbee.lang.seq;

import de.jbee.lang.Bagger;
import de.jbee.lang.List;
import de.jbee.lang.Ord;
import de.jbee.lang.Order;
import de.jbee.lang.Set;

public class UtileBagger
		implements Bagger {

	public <E> Set<E> elements( List<E> elems ) {
		return elements( Order.inherent, elems );
	}

	@Override
	public <E> Set<E> elements( Ord<Object> order, List<E> elems ) {
		return SortedList.asSet( order, elems );
	}

	@Override
	public <E> Set<E> noElements() {
		// TODO Auto-generated method stub
		return null;
	}

}
