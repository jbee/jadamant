package de.jbee.lang;

import static de.jbee.lang.ListIndex.NOT_CONTAINED;
import static de.jbee.lang.Ordering.fromComparison;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;

import de.jbee.lang.dev.Null;
import de.jbee.lang.dev.Nullproof;
import de.jbee.lang.dev.Nullsave;

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

	public static final Ord<Object> keep = new KeepOrder();
	public static final Ord<Quantifiable> quantifiable = new QuantifiableOrder();
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

	public static <T> Ord<Object> typeaware( Ord<? super T> ord, Class<T> type ) {
		return new TypeawareOrder<T>( type, ord );
	}

	public static <T> Ord<T> unequalEqualTo( T value, Eq<T> eq ) {
		return equalUnequalTo( value, Equal.not( eq ) );
	}

	public static <T> Ord<T> equalUnequalTo( T value, Eq<T> eq ) {
		return new EqualUnequalOrder<T>( eq, value );
	}

	public static <T> Ord<T> unfulfillingFulfilling( Predicate<T> predicate ) {
		return fulfillingUnfulfilling( Is.not( predicate ) );
	}

	public static <T> Ord<T> fulfillingUnfulfilling( Predicate<T> predicate ) {
		return new FulfillingUnfulfillingOrder<T>( predicate );
	}

	@SuppressWarnings ( "unchecked" )
	public static <T> Ord<T> by( Sequence<T> seq, Eq<Object> eq ) {
		return seq.isEmpty()
			? (Ord<T>) keep
			: new SequenceOrder<T>( seq, eq );
	}

	private Order() {
		throw new UnsupportedOperationException( "util" );
	}

	static final class KeepOrder
			implements Ord<Object> {

		@Override
		public Ordering ord( Object left, Object right ) {
			return Ordering.LT;
		}

	}

	static final class FulfillingUnfulfillingOrder<T>
			implements Ord<T> {

		private final Predicate<T> condition;

		FulfillingUnfulfillingOrder( Predicate<T> condition ) {
			super();
			this.condition = condition;
		}

		@Override
		public Ordering ord( T left, T right ) {
			return Ordering.trueFalse( condition.is( left ), condition.is( right ) );
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
		public Ordering ord( T left, T right ) {
			return Ordering.trueFalse( eq.holds( left, value ), eq.holds( right, value ) );
		}

	}

	static final class AbecedarianOrder
			implements Ord<Character> {

		@Override
		public Ordering ord( Character left, Character right ) {
			return fromComparison( left.compareTo( right ) );
		}

	}

	static final class AlphabeticalOrder
			implements Ord<CharSequence> {

		@Override
		public Ordering ord( CharSequence left, CharSequence right ) {
			final int length = Math.min( left.length(), right.length() );
			for ( int i = 0; i < length; i++ ) {
				Ordering ord = abecedarian.ord( left.charAt( i ), right.charAt( i ) );
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
		public Ordering ord( Calendar left, Calendar right ) {
			return fromComparison( left.compareTo( right ) );
		}

	}

	static final class ChronologicalOrder
			implements Ord<Date> {

		@Override
		public Ordering ord( Date left, Date right ) {
			return fromComparison( left.compareTo( right ) );
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
		public Ordering ord( T left, T right ) {
			return fromComparison( comparator.compare( left, right ) );
		}

	}

	static final class CompareableOrder<T extends Comparable<T>>
			implements Ord<T> {

		@Override
		public Ordering ord( T left, T right ) {
			return fromComparison( left.compareTo( right ) );
		}
	}

	static final class EnumerativeOrder
			implements Ord<java.lang.Enum<?>> {

		@Override
		public Ordering ord( java.lang.Enum<?> left, java.lang.Enum<?> right ) {
			if ( left.getDeclaringClass() == right.getDeclaringClass() ) {
				return fromComparison( left.ordinal() - right.ordinal() );
			}
			return alphabetical.ord( left.getClass().getCanonicalName(),
					right.getClass().getCanonicalName() );
		}

	}

	static final class HashCodeOrder
			implements Ord<Object> {

		@Override
		public Ordering ord( Object left, Object right ) {
			return fromComparison( left.hashCode() - right.hashCode() );
		}

	}

	static final class IdentityOrder
			implements Ord<Object> {

		@Override
		public Ordering ord( Object left, Object right ) {
			return fromComparison( System.identityHashCode( left )
					- System.identityHashCode( right ) );
		}

	}

	static final class InherentOrder
			implements Ord<Object> {

		@Override
		public Ordering ord( Object left, Object right ) {
			if ( left.getClass() == right.getClass() ) {
				return identicalTypeOrd( left, right );
			}
			return identity.ord( left, right );
		}

		private Ordering identicalTypeOrd( Object left, Object right ) {
			if ( left instanceof Quantifiable ) {
				return quantifiable.ord( (Quantifiable) left, (Quantifiable) right );
			}
			if ( left.getClass().isEnum() ) {
				return enumerative.ord( (java.lang.Enum<?>) left, (java.lang.Enum<?>) right );
			}
			if ( left instanceof Number ) {
				return numerical.ord( (Number) left, (Number) right );
			}
			if ( left instanceof CharSequence ) {
				return alphabetical.ord( (CharSequence) left, (CharSequence) right );
			}
			if ( left instanceof Character ) {
				return abecedarian.ord( (Character) left, (Character) right );
			}
			if ( left instanceof Date ) {
				return chronological.ord( (Date) left, (Date) right );
			}
			return identity.ord( left, right );
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
		public Ordering ord( T left, T right ) {
			return ord( right, left );
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
		public final Ordering ord( T left, T right ) {
			if ( left == null ) {
				return Ordering.LT;
			}
			if ( right == null ) {
				return Ordering.GT;
			}
			return ord.ord( left, right );
		}

	}

	static final class NumericalOrder
			implements Ord<Number> {

		@Override
		public Ordering ord( Number left, Number right ) {
			if ( left.getClass() == right.getClass() ) {
				return identicalTypeOrd( left, right );
			}
			return fromComparison( Double.compare( left.doubleValue(), right.doubleValue() ) );
		}

		private Ordering identicalTypeOrd( Number left, Number right ) {
			if ( left instanceof Float ) {
				return fromComparison( Float.compare( left.floatValue(), right.floatValue() ) );
			}
			if ( left instanceof Long ) {
				return fromComparison( Long.signum( left.longValue() - right.longValue() ) );
			}
			if ( left instanceof Integer ) {
				return fromComparison( left.intValue() - right.intValue() );
			}
			if ( left instanceof BigDecimal ) {
				return fromComparison( ( (BigDecimal) left ).compareTo( (BigDecimal) right ) );
			}
			if ( left instanceof BigInteger ) {
				return fromComparison( ( (BigInteger) left ).compareTo( (BigInteger) right ) );
			}
			return fromComparison( Double.compare( left.doubleValue(), right.doubleValue() ) );
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

	static final class QuantifiableOrder
			implements Ord<Quantifiable> {

		@Override
		public Ordering ord( Quantifiable left, Quantifiable right ) {
			return fromComparison( left.ordinal() - right.ordinal() );
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
		public Ordering ord( T left, T right ) {
			final Ordering res = primary.ord( left, right );
			return res.isEq()
				? secondary.ord( left, right )
				: res;
		}

	}

	/**
	 * Order is given by a sequence. All elements not contained in the sequence are considered to be
	 * less than any element contained.
	 * 
	 * @author Jan Bernitt (jan.bernitt@gmx.de)
	 */
	static final class SequenceOrder<T>
			implements Ord<T> {

		private final Sequence<T> seq;
		private final Eq<Object> eq;

		SequenceOrder( Sequence<T> seq, Eq<Object> eq ) {
			super();
			this.seq = seq;
			this.eq = eq;
		}

		@Override
		public Ordering ord( T left, T right ) {
			final int leftIndex = List.indexFor.elemBy( left, eq ).in( seq );
			final int rightIndex = List.indexFor.elemBy( right, eq ).in( seq );
			if ( leftIndex == NOT_CONTAINED ) {
				return rightIndex == NOT_CONTAINED
					? Ordering.EQ
					: Ordering.LT;
			}
			if ( rightIndex == NOT_CONTAINED ) {
				return Ordering.GT;
			}
			return Ordering.fromComparison( leftIndex - rightIndex );
		}

	}

	static final class TypeawareOrder<T>
			implements Ord<Object>, Nullsave {

		final Class<T> type;
		final Ord<? super T> ord;

		TypeawareOrder( Class<T> type, Ord<? super T> ord ) {
			super();
			this.type = type;
			this.ord = ord;
		}

		@SuppressWarnings ( "unchecked" )
		@Override
		public Ordering ord( Object left, Object right ) {
			final boolean instLeft = type.isInstance( left );
			final boolean instRight = type.isInstance( right );
			if ( instLeft && instRight ) {
				return ord.ord( (T) left, (T) right );
			}
			if ( instLeft ) {
				return Ordering.GT;
			}
			if ( instRight ) {
				return Ordering.LT;
			}
			return Ordering.EQ;
		}

	}
}
