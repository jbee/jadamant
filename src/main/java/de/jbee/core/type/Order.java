package de.jbee.core.type;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;

import de.jbee.core.dev.Null;
import de.jbee.core.dev.Nullproof;
import de.jbee.core.dev.Nullsave;
import de.jbee.util.Fulfills;
import de.jbee.util.ICondition;

/**
 * All types of orders are ascending by default. You will not find a descending version. Therefore
 * you can invert every order by applying the {@link InverseOrder} proxy.
 * 
 * @author Jan Bernitt (jan.bernitt@gmx.de)
 * 
 */
public final class Order {

	/**
	 * <p>
	 * This order might result in what you expect under no special circumstances. In fact there is
	 * no 'natural' order in general. Its just the most common understanding of a 'correct' ordering
	 * in the most common cases where this leads to the most expected result. So in some cases this
	 * order might deliver quite unexpected results.
	 * </p>
	 * <p>
	 * Please do not misunderstand the existence of this order. It acts as an default order where no
	 * order should or could be passed or be used. <b>If you know better - don't use this!</b>
	 * </p>
	 */
	public static final Ord<Object> inherent = nullsave( new InherentOrder() );

	public static final Ord<Sortable> sortable = new SortableOrder();
	public static final Ord<Number> numerical = new NumericalOrder();
	public static final Ord<Character> abecedarian = new AbecedarianOrder();
	public static final Ord<CharSequence> alphabetical = new AlphabeticalOrder();
	public static final Ord<Object> identity = new IdentityOrder();
	public static final Ord<java.lang.Enum<?>> enumerative = new EnumerativeOrder();
	public static final Ord<Date> chronological = new ChronologicalOrder();
	public static final Ord<Calendar> calendrical = new CalendricalOrder();
	public static final Ord<Object> byHashCode = new HashCodeOrder();

	public static <T> Ord<T> asc( Ord<T> ord ) {
		if ( ord instanceof InverseOrder<?> ) {
			return asc( ( (InverseOrder<T>) ord ).ord );
		}
		return ord;
	}

	public static <T extends Comparable<T>> Ord<T> byCompare() {
		return new CompareableOrder<T>();
	}

	public static <T> Ord<T> desc( Ord<T> ord ) {
		if ( ord instanceof InverseOrder<?> ) {
			return desc( ( (InverseOrder<T>) ord ).ord );
		}
		return new InverseOrder<T>( ord );
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

	public static <T> void sort( T[] array, Ord<T> ord ) {
		Arrays.sort( array, new OrderAdapterComparator<T>( ord ) );
	}

	public static <T> Ord<T> sub( Ord<? super T> primary, Ord<? super T> secondary ) {
		return new SubOrder<T>( primary, secondary );
	}

	public static <T> Ord<Object> typesave( Ord<? super T> ord, Class<T> type ) {
		return new TypesaveOrder<T>( type, ord );
	}

	public static <T> Ord<T> unequalEqualTo( T value, Eq<T> eq ) {
		return equalUnequalTo( value, Equal.not( eq ) );
	}

	public static <T> Ord<T> equalUnequalTo( T value, Eq<T> eq ) {
		return new EqualUnequalOrder<T>( eq, value );
	}

	public static <T> Ord<T> unfulfillingFulfilling( ICondition<T> condition ) {
		return fulfillingUnfulfilling( Fulfills.not( condition ) );
	}

	public static <T> Ord<T> fulfillingUnfulfilling( ICondition<T> condition ) {
		return new FulfillingUnfulfillingOrder<T>( condition );
	}

	private Order() {
		throw new UnsupportedOperationException( "util" );
	}

	static final class FulfillingUnfulfillingOrder<T>
			implements Ord<T> {

		private final ICondition<T> condition;

		FulfillingUnfulfillingOrder( ICondition<T> condition ) {
			super();
			this.condition = condition;
		}

		@Override
		public Ordering ord( T one, T other ) {
			return Ordering.trueFalse( condition.fulfilledBy( one ), condition.fulfilledBy( other ) );
		}
	}

	static final class EqualUnequalOrder<T>
			implements Ord<T> {

		private final Eq<T> eq;
		private final T value;

		EqualUnequalOrder( Eq<T> eq, T value ) {
			super();
			this.eq = eq;
			this.value = value;
		}

		@Override
		public Ordering ord( T one, T other ) {
			return Ordering.trueFalse( eq.eq( one, value ), eq.eq( other, value ) );
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

	static final class CalendricalOrder
			implements Ord<Calendar> {

		@Override
		public Ordering ord( Calendar one, Calendar other ) {
			return Ordering.valueOf( one.compareTo( other ) );
		}

	}

	static final class ChronologicalOrder
			implements Ord<Date> {

		@Override
		public Ordering ord( Date one, Date other ) {
			return Ordering.valueOf( one.compareTo( other ) );
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

	static final class EnumerativeOrder
			implements Ord<java.lang.Enum<?>> {

		@Override
		public Ordering ord( java.lang.Enum<?> one, java.lang.Enum<?> other ) {
			if ( one.getDeclaringClass() == other.getDeclaringClass() ) {
				return Ordering.valueOf( one.ordinal() - other.ordinal() );
			}
			return alphabetical.ord( one.getClass().getCanonicalName(),
					other.getClass().getCanonicalName() );
		}

	}

	static final class HashCodeOrder
			implements Ord<Object> {

		@Override
		public Ordering ord( Object one, Object other ) {
			return Ordering.valueOf( one.hashCode() - other.hashCode() );
		}

	}

	static final class IdentityOrder
			implements Ord<Object> {

		@Override
		public Ordering ord( Object one, Object other ) {
			return Ordering.valueOf( System.identityHashCode( one )
					- System.identityHashCode( other ) );
		}

	}

	static final class InherentOrder
			implements Ord<Object> {

		@SuppressWarnings ( "unchecked" )
		@Override
		public Ordering ord( Object one, Object other ) {
			if ( one instanceof Sortable && other instanceof Sortable ) {
				return sortable.ord( (Sortable) one, (Sortable) other );
			}
			if ( one.getClass().isEnum() && other.getClass().isEnum() ) {
				return enumerative.ord( (java.lang.Enum<?>) one, (java.lang.Enum<?>) other );
			}
			if ( one instanceof Number && other instanceof Number ) {
				return numerical.ord( (Number) one, (Number) other );
			}
			if ( one instanceof CharSequence && other instanceof CharSequence ) {
				return alphabetical.ord( (CharSequence) one, (CharSequence) other );
			}
			if ( one instanceof Character && other instanceof Character ) {
				return abecedarian.ord( (Character) one, (Character) other );
			}
			if ( one instanceof Date && other instanceof Date ) {
				return chronological.ord( (Date) one, (Date) other );
			}
			if ( one instanceof Comparable<?> && other instanceof Comparable<?>
					&& one.getClass() == other.getClass() ) {
				return compare( (Comparable) one, (Comparable) other );
			}
			return identity.ord( one, other );
		}

		private <T extends Comparable<T>> Ordering compare( T one, T other ) {
			return Order.<T> byCompare().ord( one, other );
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
		public boolean isNullsave() {
			return Null.isSave( ord );
		}

		@Override
		public Ordering ord( T one, T other ) {
			return ord( other, one );
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
			return ord.ord( one, other );
		}

	}

	static final class NumericalOrder
			implements Ord<Number> {

		@Override
		public Ordering ord( Number one, Number other ) {
			if ( one instanceof Float && other instanceof Float ) {
				return Ordering.valueOf( Float.compare( one.floatValue(), other.floatValue() ) );
			}
			if ( one instanceof Long && other instanceof Long ) {
				return Ordering.valueOf( Long.signum( one.longValue() - other.longValue() ) );
			}
			if ( one instanceof Integer && other instanceof Integer ) {
				return Ordering.valueOf( one.intValue() - other.intValue() );
			}
			if ( one instanceof BigDecimal && other instanceof BigDecimal ) {
				return Ordering.valueOf( ( (BigDecimal) one ).compareTo( (BigDecimal) other ) );
			}
			if ( one instanceof BigInteger && other instanceof BigInteger ) {
				return Ordering.valueOf( ( (BigInteger) one ).compareTo( (BigInteger) other ) );
			}
			return Ordering.valueOf( Double.compare( one.doubleValue(), other.doubleValue() ) );
		}

	}

	static final class OrderAdapterComparator<T>
			implements Comparator<T> {

		private final Ord<T> ord;

		OrderAdapterComparator( Ord<T> ord ) {
			super();
			this.ord = ord;
		}

		@Override
		public int compare( T one, T other ) {
			return ord.ord( one, other ).intValue();
		}

	}

	static final class SortableOrder
			implements Ord<Sortable> {

		@Override
		public Ordering ord( Sortable one, Sortable other ) {
			return Ordering.valueOf( one.ordinal() - other.ordinal() );
		}

	}

	static final class SubOrder<T>
			implements Ord<T> {

		private final Ord<? super T> primary;
		private final Ord<? super T> secondary;

		SubOrder( Ord<? super T> primary, Ord<? super T> secondary ) {
			super();
			this.primary = primary;
			this.secondary = secondary;
		}

		@Override
		public Ordering ord( T one, T other ) {
			final Ordering res = primary.ord( one, other );
			return res.isEq()
				? secondary.ord( one, other )
				: res;
		}

	}

	static final class TypesaveOrder<T>
			implements Ord<Object>, Nullsave {

		final Class<T> type;
		final Ord<? super T> ord;

		TypesaveOrder( Class<T> type, Ord<? super T> ord ) {
			super();
			this.type = type;
			this.ord = ord;
		}

		@SuppressWarnings ( "unchecked" )
		@Override
		public Ordering ord( Object one, Object other ) {
			final boolean instOne = type.isInstance( one );
			final boolean instOther = type.isInstance( other );
			if ( instOne && instOther ) {
				return ord.ord( (T) one, (T) other );
			}
			if ( instOne ) {
				return Ordering.GT;
			}
			if ( instOther ) {
				return Ordering.LT;
			}
			return Ordering.EQ;
		}

	}
}
