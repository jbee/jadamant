package de.jbee.lang.seq;

import de.jbee.lang.List;
import de.jbee.lang.ListIndex;
import de.jbee.lang.ListModification;
import de.jbee.lang.dev.Nonnull;

public class ModifyBy {

	@SuppressWarnings ( "unchecked" )
	private static final ListModification NONE = new NoModification<Object>();

	@SuppressWarnings ( "unchecked" )
	public <E> ListModification<E> none() {
		return NONE;
	}

	public <E> ListModification<E> insertAt( ListIndex index, E element ) {
		Nonnull.element( element );
		return new InsertElementModification<E>( index, element );
	}

	public <E> ListModification<E> insertAt( int index, List<E> list ) {
		return insertAt( List.indexFor.elemAt( index ), list );
	}

	public <E> ListModification<E> insertAt( ListIndex index, List<E> list ) {
		return list.isEmpty()
			? this.<E> none()
			: list.length() == 1
				? insertAt( index, list.at( 0 ) )
				: new InsertListModification<E>( index, list );
	}

	private static final class NoModification<E>
			implements ListModification<E> {

		NoModification() {
			// make available
		}

		@Override
		public List<E> in( List<E> list ) {
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
		public List<E> in( List<E> list ) {
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
		public List<E> in( List<E> list ) {
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
