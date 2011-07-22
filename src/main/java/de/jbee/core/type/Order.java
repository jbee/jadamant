package de.jbee.core.type;

import java.util.Comparator;

import de.jbee.core.dev.Null;
import de.jbee.core.dev.Nullproof;
import de.jbee.core.dev.Nullsave;

/**
 * All types of orders are ascending by default. You will not find a descending version. Therefore
 * you can invert every order by applying the {@link InverseOrder} wrapper.
 * 
 * @author Jan Bernitt (jan.bernitt@gmx.de)
 * 
 */
public final class Order {

	private Order() {
		throw new UnsupportedOperationException( "util" );
	}

	public static final Ord<Sortable> sortable = new SortableOrder();
	public static final Ord<Number> numerical = new NumericalOrder();
	public static final Ord<Character> abecedarian = new AbecedarianOrder();
	public static final Ord<CharSequence> alphabetical = new AlphabeticalOrder();
	public static final Ord<Object> identity = new IdentityOrder();

	public static <T extends Comparable<T>> Ord<T> byCompare() {
		return new CompareableOrder<T>();
	}

	public static <T> Ord<T> desc( Ord<T> ord ) {
		if ( ord instanceof InverseOrder<?> ) {
			return desc( ( (InverseOrder<T>) ord ).ord );
		}
		return new InverseOrder<T>( ord );
	}

	public static <T> Ord<T> asc( Ord<T> ord ) {
		if ( ord instanceof InverseOrder<?> ) {
			return asc( ( (InverseOrder<T>) ord ).ord );
		}
		return ord;
	}

	public static <T> Ord<T> inverse( Ord<T> ord ) {
		if ( ord instanceof InverseOrder<?> ) {
			return ( (InverseOrder<T>) ord ).ord;
		}
		return new InverseOrder<T>( ord );
	}

	public static <T> Ord<T> nullsave( Ord<T> ord ) {
		return Null.isSave( ord )
			? ord
			: new NullsaveOrder<T>( ord );
	}

	public static <T> Ord<T> nullsFirst( Ord<T> ord ) {
		return nullsave( ord );
	}

	public static <T> Ord<T> nullsLast( Ord<T> ord ) {
		return inverse( nullsave( inverse( ord ) ) );
	}

	static final class IdentityOrder
			implements Ord<Object> {

		@Override
		public Ordering ord( Object one, Object other ) {
			return Ordering.valueOf( System.identityHashCode( one )
					- System.identityHashCode( other ) );
		}

	}

	static final class NullsaveOrder<T>
			implements Ord<T>, Nullsave {

		final Ord<T> ord;

		NullsaveOrder( Ord<T> ord ) {
			super();
			this.ord = ord;
		}

		@Override
		public final Ordering ord( T one, T other ) {
			if ( one == null ) {
				return Ordering.LT;
			}
			if ( other == null ) {
				return Ordering.GT;
			}
			return ord( one, other );
		}

	}

	static final class AbecedarianOrder
			implements Ord<Character> {

		@Override
		public Ordering ord( Character one, Character other ) {
			return Ordering.valueOf( one.compareTo( other ) );
		}

	}

	static final class AlphabeticalOrder
			implements Ord<CharSequence> {

		@Override
		public Ordering ord( CharSequence one, CharSequence other ) {
			final int length = Math.min( one.length(), other.length() );
			for ( int i = 0; i < length; i++ ) {
				Ordering ord = abecedarian.ord( one.charAt( i ), other.charAt( i ) );
				if ( !ord.isEq() ) {
					return ord;
				}
			}
			return Ordering.EQ;
		}

	}

	static final class ComparatorOrder<T>
			implements Ord<T> {

		final Comparator<T> comparator;

		ComparatorOrder( Comparator<T> comparator ) {
			super();
			this.comparator = comparator;
		}

		@Override
		public Ordering ord( T one, T other ) {
			return Ordering.valueOf( comparator.compare( one, other ) );
		}

	}

	static final class CompareableOrder<T extends Comparable<T>>
			implements Ord<T> {

		@Override
		public Ordering ord( T one, T other ) {
			return Ordering.valueOf( one.compareTo( other ) );
		}
	}

	static final class NumericalOrder
			implements Ord<Number> {

		@Override
		public Ordering ord( Number one, Number other ) {
			return Ordering.valueOf( Double.compare( one.doubleValue(), other.doubleValue() ) );
		}

	}

	static final class SortableOrder
			implements Ord<Sortable> {

		@Override
		public Ordering ord( Sortable one, Sortable other ) {
			return Ordering.valueOf( one.ordinal() - other.ordinal() );
		}

	}

	static final class InverseOrder<T>
			implements Ord<T>, Nullproof {

		final Ord<T> ord;

		InverseOrder( Ord<T> ord ) {
			super();
			this.ord = ord;
		}

		@Override
		public Ordering ord( T one, T other ) {
			return ord( other, one );
		}

		@Override
		public boolean isNullsave() {
			return Null.isSave( ord );
		}
	}
}
