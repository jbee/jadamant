package de.jbee.lang.seq;

import de.jbee.lang.List;
import de.jbee.lang.Lister;
import de.jbee.lang.Ord;
import de.jbee.lang.Order;
import de.jbee.lang.Sequence;
import de.jbee.lang.Set;

public class UtileSetLister
		implements Lister.SetLister {

	public <E> Set<E> elements( List<E> elems ) {
		return elements( Order.inherent, elems );
	}

	@Override
	public <E> Set<E> elements( Ord<Object> order, List<E> elems ) {

		return null;
	}

	@Override
	public <E> Set<E> noElements() {
		return elements( List.with.<E> noElements() );
	}

	@Override
	public <E> Set<E> element( E e ) {
		return SortedList.setOf( List.with.element( e ), Order.inherent );
	}

	@Override
	public <E> Set<E> elements( E... elems ) {
		return null;
	}

	@Override
	public <E> Set<E> elements( Sequence<E> elems ) {
		throw new UnsupportedOperationException( "missing impl." );
	}

	@Override
	public <E> Set<E> noElements( Ord<Object> order ) {
		// TODO Auto-generated method stub
		return null;
	}

}
