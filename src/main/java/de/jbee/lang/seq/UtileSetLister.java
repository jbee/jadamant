package de.jbee.lang.seq;

import de.jbee.lang.Array;
import de.jbee.lang.Bag;
import de.jbee.lang.Lang;
import de.jbee.lang.List;
import de.jbee.lang.Lister;
import de.jbee.lang.Ord;
import de.jbee.lang.Order;
import de.jbee.lang.Sequence;
import de.jbee.lang.Set;
import de.jbee.lang.Sorted;

public class UtileSetLister
		implements Lister.SetLister {

	public <E> Set<E> elements( List<E> elems ) {
		return elements( Order.inherent, elems );
	}

	@Override
	public <E> Set<E> elements( Ord<Object> order, List<E> elems ) {
		return SortedList.setOf( sort( elems, order ), order );
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
		return elements( List.with.elements( elems ) );
	}

	@Override
	public <E> Set<E> elements( Sequence<E> elems ) {
		Ord<Object> order = elems instanceof Sorted
			? ( (Sorted) elems ).order()
			: Order.inherent;
		return elements( order, List.with.elements( elems ) );
	}

	@Override
	public <E> Set<E> noElements( Ord<Object> order ) {
		return elements( order, List.with.<E> noElements() );
	}

	private <E> List<E> sort( List<E> elems, Ord<Object> order ) {
		if ( elems.length() <= 1 ) {
			return elems;
		}
		if ( elems instanceof Sorted ) {
			final boolean inheritOrder = order == Order.inherent;
			final Ord<Object> seqOrder = ( (Sorted) elems ).order();
			if ( elems instanceof Set<?> ) {
				if ( inheritOrder || order == seqOrder ) {
					return elems;
				}
			}
			if ( elems instanceof Bag<?> ) {
				if ( inheritOrder || order == seqOrder ) {
					return withoutDuplicates( elems, order );
				}
			}
			if ( inheritOrder ) {
				order = seqOrder;
			}
		}
		Object[] elements = new Object[elems.length()];
		elems.fill( 0, elements, 0, elems.length() );
		Order.sort( elements, order );
		return withoutDuplicates( (Sequence<E>) Array.sequence( elements ), order );
	}

	private static <E> List<E> withoutDuplicates( Sequence<E> elems, Ord<Object> order ) {
		Object[] elements = new Object[Lang.nextHighestPowerOf2( elems.length() )];
		int j = elements.length - 1;
		E previous = elems.at( List.indexFor.elemOn( -1 ).in( elems ) );
		elements[j--] = previous;
		for ( int i = elems.length() - 2; i >= 0; i-- ) {
			E e = elems.at( i );
			if ( order.ord( e, previous ).isLt() ) {
				previous = e;
				elements[j--] = e;
			}
		}
		return EvolutionList.dominant( elements.length - j - 1, elements );
	}

}
