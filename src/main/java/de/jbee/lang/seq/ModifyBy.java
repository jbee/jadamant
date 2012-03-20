package de.jbee.lang.seq;

import static de.jbee.lang.seq.IndexFor.exists;
import de.jbee.lang.List;
import de.jbee.lang.ListAlteration;
import de.jbee.lang.ListIndex;
import de.jbee.lang.ListModification;
import de.jbee.lang.dev.Nonnull;

/**
 * This is a utility to create and combine {@link ListModification}s.
 * 
 * A {@linkplain ListModification} is a function that modifies a {@link List}. In contrast to a
 * {@link ListAlteration} this modification depends on the element type of the list so that it can
 * just be applied to list of a special element type. A {@link ListAlteration} however can be
 * applied to all list.
 * 
 * @author Jan Bernitt (jan.bernitt@gmx.de)
 */
public class ModifyBy {

	@SuppressWarnings ( "unchecked" )
	private static final ListModification NONE = new NoModification<Object>();

	public final static ModifyBy modifyBy = new ModifyBy();

	private ModifyBy() {
		// hide
	}

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
				: insertAt( index, constant( list ) );
	}

	public <E> ListModification<E> insertAt( ListIndex index, ListModification<E> inserted ) {
		return new InsertListModification<E>( index, inserted );
	}

	public <E> ListModification<E> replaceAt( int index, E element ) {
		return replaceAt( List.indexFor.elemAt( index ), element );
	}

	public <E> ListModification<E> replaceAt( ListIndex index, E element ) {
		Nonnull.element( element );
		return new ReplaceElementModification<E>( index, element );
	}

	public <E> ListModification<E> prepand( E element ) {
		return new PrepandModification<E>( element );
	}

	public <E> ListModification<E> append( E element ) {
		return new AppendModification<E>( element );
	}

	public <E> ListModification<E> prepand( List<E> init ) {
		return prepand( constant( init ) );
	}

	public <E> ListModification<E> prepand( ListModification<E> init ) {
		return new PrepandListModification<E>( init );
	}

	public <E> ListModification<E> append( List<E> tail ) {
		return concat( tail );
	}

	public <E> ListModification<E> embed( E before, E after ) {
		return chain( prepand( before ), append( after ) );
	}

	public <E> ListModification<E> embed( List<E> before, List<E> after ) {
		return embed( constant( before ), constant( after ) );
	}

	public <E> ListModification<E> embed( ListModification<E> init, ListModification<E> tail ) {
		return chain( prepand( init ), concat( tail ) );
	}

	public <E> ListModification<E> concat( List<E> tail ) {
		return concat( constant( tail ) );
	}

	public <E> ListModification<E> concat( ListModification<E> tail ) {
		return new ConcatModification<E>( tail );
	}

	public <E> ListModification<E> constant( List<E> constant ) {
		return new ConstantListModification<E>( constant );
	}

	public <E> ListModification<E> alter( ListAlteration alteration ) {
		return new AlterationModification<E>( alteration );
	}

	public <E> ListModification<E> chain( ListModification<E> fst, ListModification<E> snd ) {
		if ( fst == NONE ) {
			return snd;
		}
		if ( snd == NONE ) {
			return fst;
		}
		return new ConsecutiveListModification<E>( fst, snd );
	}

	/**
	 * The NO-OP {@link ListModification}.
	 */
	private static final class NoModification<E>
			implements ListModification<E> {

		NoModification() {
			// make available
		}

		@Override
		public List<E> on( List<E> list ) {
			return list;
		}

	}

	private static final class ConsecutiveListModification<E>
			implements ListModification<E> {

		private final ListModification<E> fst;
		private final ListModification<E> snd;

		ConsecutiveListModification( ListModification<E> fst, ListModification<E> snd ) {
			super();
			this.fst = fst;
			this.snd = snd;
		}

		@Override
		public List<E> on( List<E> list ) {
			return snd.on( fst.on( list ) );
		}

	}

	/**
	 * Returns the given constant list no matter what list is passed as argument.
	 * 
	 * This is useful since it allows to pass a fix list to a {@link ListModification} that has
	 * another modification as constructor argument.
	 */
	private static final class ConstantListModification<E>
			implements ListModification<E> {

		private final List<E> constant;

		ConstantListModification( List<E> constant ) {
			super();
			this.constant = constant;
		}

		@Override
		public List<E> on( List<E> list ) {
			return constant;
		}
	}

	/**
	 * Applies a {@link ListAlteration} to the argument list.
	 * 
	 * This is useful since it allows to pass a {@linkplain ListAlteration} as modification to
	 * another modification that expects one as constructor argument.
	 */
	private static final class AlterationModification<E>
			implements ListModification<E> {

		private final ListAlteration alteration;

		AlterationModification( ListAlteration alteration ) {
			super();
			this.alteration = alteration;
		}

		@Override
		public List<E> on( List<E> list ) {
			return alteration.from( list );
		}

	}

	/**
	 * Prepands the init-list to the argument list.
	 */
	private static class PrepandListModification<E>
			implements ListModification<E> {

		private final ListModification<E> init;

		PrepandListModification( ListModification<E> init ) {
			super();
			this.init = init;
		}

		@Override
		public List<E> on( List<E> list ) {
			return init.on( list ).concat( list );
		}
	}

	/**
	 * Appends the tail to the argument list.
	 */
	private static final class ConcatModification<E>
			implements ListModification<E> {

		private final ListModification<E> tail;

		ConcatModification( ListModification<E> tail ) {
			super();
			this.tail = tail;
		}

		@Override
		public List<E> on( List<E> list ) {
			return list.concat( tail.on( list ) );
		}

	}

	/**
	 * Appends the element to the argument list.
	 */
	private static final class AppendModification<E>
			implements ListModification<E> {

		private final E element;

		AppendModification( E element ) {
			super();
			this.element = element;
		}

		@Override
		public List<E> on( List<E> list ) {
			return list.append( element );
		}

	}

	/**
	 * Prepands the element to the argument list.
	 */
	private static final class PrepandModification<E>
			implements ListModification<E> {

		private final E element;

		PrepandModification( E element ) {
			super();
			this.element = element;
		}

		@Override
		public List<E> on( List<E> list ) {
			return list.prepand( element );
		}

	}

	private static final class ReplaceElementModification<E>
			implements ListModification<E> {

		private final ListIndex replaceAt;
		private final E element;

		ReplaceElementModification( ListIndex replaceAt, E element ) {
			super();
			this.replaceAt = replaceAt;
			this.element = element;
		}

		@Override
		public List<E> on( List<E> list ) {
			int index = replaceAt.in( list );
			return !exists( index )
				? list
				: list.replaceAt( index, element );
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
		public List<E> on( List<E> list ) {
			int index = insertAt.in( list );
			return !exists( index )
				? list
				: list.insertAt( index, element );
		}
	}

	/**
	 * Insert a list into the argument list at a given {@link ListIndex}.
	 */
	private static final class InsertListModification<E>
			implements ListModification<E> {

		private final ListIndex insertAt;
		private final ListModification<E> inserted;

		InsertListModification( ListIndex insertAt, ListModification<E> inserted ) {
			super();
			this.insertAt = insertAt;
			this.inserted = inserted;
		}

		@Override
		public List<E> on( List<E> list ) {
			final int index = insertAt.in( list );
			if ( !exists( index ) ) { // insertion index not defined -> do not insert
				return list;
			}
			List<E> insertedList = inserted.on( list );
			if ( index == 0 ) {
				return insertedList.concat( list );
			}
			if ( index >= list.length() ) {
				return list.concat( insertedList );
			}
			return list.take( index ).concat( insertedList ).concat( list.drop( index ) );
		}

	}
}
