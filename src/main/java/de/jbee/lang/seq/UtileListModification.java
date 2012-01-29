package de.jbee.lang.seq;

import de.jbee.lang.List;
import de.jbee.lang.ListIndex;
import de.jbee.lang.ListModification;
import de.jbee.lang.dev.Nonnull;

public class UtileListModification {

	@SuppressWarnings ( "unchecked" )
	private static final ListModification NONE = new NoModification<Object>();

	@SuppressWarnings ( "unchecked" )
	public static <E> ListModification<E> none() {
		return NONE;
	}

	public static <E> ListModification<E> insert( E element, ListIndex index ) {
		Nonnull.element( element );
		return new InsertElementModification<E>( index, element );
	}

	public static <E> ListModification<E> insert( List<E> list, int index ) {
		return insert( list, List.indexFor.elemAt( index ) );
	}

	public static <E> ListModification<E> insert( List<E> list, ListIndex index ) {
		return list.isEmpty()
			? UtileListModification.<E> none()
			: list.length() == 1
				? insert( list.at( 0 ), index )
				: new InsertListModification<E>( index, list );
	}

	private static final class NoModification<E>
			implements ListModification<E> {

		NoModification() {
			// make available
		}

		@Override
		public List<E> from( List<E> list ) {
			return list;
		}

	}

	/**
	 * Inserts a single element into the list at a given {@link ListIndex}.
	 */
	private static final class InsertElementModification<E>
			implements ListModification<E> {

		private final ListIndex insertAt;
		private final E element;

		InsertElementModification( ListIndex insertAt, E element ) {
			super();
			this.insertAt = insertAt;
			this.element = element;
		}

		@Override
		public List<E> from( List<E> list ) {
			int index = insertAt.in( list );
			if ( index < 0 ) {
				return list;
			}
			if ( index == 0 ) {
				return list.prepand( element );
			}
			if ( index >= list.length() ) {
				return list.append( element );
			}
			return list.take( index ).concat( list.drop( index ).prepand( element ) );
		}
	}

	/**
	 * Insert a list into the argument list at a given {@link ListIndex}.
	 */
	private static final class InsertListModification<E>
			implements ListModification<E> {

		private final ListIndex insertAt;
		private final List<E> inserted;

		InsertListModification( ListIndex insertAt, List<E> inserted ) {
			super();
			this.insertAt = insertAt;
			this.inserted = inserted;
		}

		@Override
		public List<E> from( List<E> list ) {
			final int index = insertAt.in( list );
			if ( index < 0 ) { // insertion index not defined -> do not insert
				return list;
			}
			if ( index == 0 ) {
				return inserted.concat( list );
			}
			if ( index >= list.length() ) {
				return list.concat( inserted );
			}
			return list.take( index ).concat( inserted ).concat( list.drop( index ) );
		}

	}
}
