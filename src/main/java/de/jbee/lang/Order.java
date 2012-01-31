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

	/**
	 * A {@link Ord} that keeps the order as it is. Useable in some special cases as kind of a
	 * NULL-object.
	 */
	public static final Ord<Object> keep = new StaticOrder( Ordering.LT );

	/**
	 * A {@link Ord} that will reverse the order each time it is applied.
	 */
	public static final Ord<Object> reverse = new StaticOrder( Ordering.GT );

	public static final Ord<Quantifiable> quantifiable = new QuantifiableOrder();
	public static final Ord<Number> numerical = new NumericalOrder();
	public static final Ord<Character> abecedarian = new AbecedarianOrder();
	public static final Ord<CharSequence> alphabetical = new AlphabeticalOrder();
	public static final Ord<Object> identity = new IdentityOrder();
	public static final Ord<java.lang.Enum<?>> enumerative = new EnumerativeOrder();
	public static final Ord<Date> chronological = new ChronologicalOrder();
	public static final Ord<Calendar> calendrical = new CalendricalOrder();
	public static final Ord<Object> hashCode = new HashCodeOrder();

	@SuppressWarnings ( "unchecked" )
	public static final Ord<Map.Entry> entriesBy( Ord<CharSequence> keyOrder ) {
		return new EntryOrder( keyOrder );
	}

	public static <V> Ord<Element> elementsBy( Ord<? super V> valueOrder ) {
		return new ElementOrder( valueOrder );
	}

	public static boolean keepable( Ordered sorted, Ord<?> required ) {
		if ( required == inherent ) {
			return true;
		}
		return sorted.order() == required; //TODO improve
	}

	public static Ord<Object> inherent( Class<?> type ) {
		//TODO analyze type and return result directly so resolution is done once.
		return inherent;
	}

	public static <T> Ord<T> asc( Ord<T> order ) {
		if ( order instanceof InverseOrder<?> ) {
			return asc( ( (InverseOrder<T>) order ).order );
		}
		return order;
	}

	public static <T> Ord<T> by( Eq<T> equality ) {
		return new EqualityOrder<T>( equality );
	}

	public static <T extends Comparable<T>> Ord<T> byCompare() {
		return new CompareableOrder<T>();
	}

	public static <T> Ord<T> desc( Ord<T> order ) {
		if ( order instanceof InverseOrder<?> ) {
			return desc( ( (InverseOrder<T>) order ).order );
		}
		return new InverseOrder<T>( order );
	}

	public static <T> Ord<T> inverse( Ord<T> order ) {
		if ( order instanceof InverseOrder<?> ) {
			return ( (InverseOrder<T>) order ).order;
		}
		return new InverseOrder<T>( order );
	}

	public static <T> Ord<T> nullsave( Ord<T> order ) {
		return Null.isSave( order )
			? order
			: new NullsaveOrder<T>( order );
	}

	public static <T> Ord<T> nullsFirst( Ord<T> order ) {
		return nullsave( order );
	}

	public static <T> Ord<T> nullsLast( Ord<T> order ) {
		return inverse( nullsave( inverse( order ) ) );
	}

	public static <S, T extends S> void sort( T[] array, Ord<S> order ) {
		Arrays.sort( array, new OrderAdapterComparator<S>( order ) );
	}

	public static <T> Ord<T> sub( Ord<? super T> primary, Ord<? super T> secondary ) {
		return new SubOrder<T>( primary, secondary );
	}

	public static Ord<Object> sub2( Ord<Object> primary, Ord<Object> secondary ) {
		return new SubOrder<Object>( primary, secondary );
	}

	public static <T> Ord<Object> typeaware( Ord<? super T> order, Class<T> type ) {
		return new TypeawareOrder<T>( type, order );
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

	public static <T> Ord<T> by( Comparator<T> comparator ) {
		return new ComparatorOrder<T>( comparator );
	}

	private Order() {
		throw new UnsupportedOperationException( "util" );
	}

	//TODO find a place for such utils like binarySearch and sort
	public static <E> int binarySearch( Sequence<E> list, int startInclusive, int endExcluisve,
			Object key, Ord<Object> order ) {
		int index = normalBinarySearch( list, startInclusive, endExcluisve, key, order );
		while ( index > 0 && order.ord( list.at( index ), list.at( index - 1 ) ).isEq() ) {
			index--;
		}
		return index;
	}

	/**
	 * Doesn't consider possibility of duplicates (in a block) for the searched key and therefore
	 * the index of one of them might be the result but we cannot tell which one it will be.
	 */
	private static <E> int normalBinarySearch( Sequence<E> list, int startInclusive,
			int endExcluisve, Object key, Ord<Object> order ) {
		int low = startInclusive;
		int high = endExcluisve - 1;
		while ( low <= high ) {
			int mid = ( low + high ) >>> 1;
			E midVal = list.at( mid );
			Ordering cmp = order.ord( midVal, key );
			if ( cmp.isLt() ) {
				low = mid + 1;
			} else if ( cmp.isGt() ) {
				high = mid - 1;
			} else {
				return mid; // key found
			}
		}
		return - ( low + 1 ); // key not found.
	}

	private static final class ElementOrder<V>
			implements Ord<Element<V>> {

		private final Ord<? super V> valueOrder;

		ElementOrder( Ord<? super V> valueOrder ) {
			super();
			this.valueOrder = valueOrder;
		}

		@Override
		public Ordering ord( Element<V> left, Element<V> right ) {
			return valueOrder.ord( left.value(), right.value() );
		}

		@Override
		public String toString() {
			return "^" + valueOrder;
		}
	}

	@SuppressWarnings ( "unchecked" )
	private static final class EntryOrder
			implements Ord<Map.Entry> {

		private final Ord<CharSequence> keyOrder;

		EntryOrder( Ord<CharSequence> keyOrder ) {
			super();
			this.keyOrder = keyOrder;
		}

		@Override
		public Ordering ord( Map.Entry left, Map.Entry right ) {
			return keyOrder.ord( left.key().pattern(), right.key().pattern() );
		}

		@Override
		public String toString() {
			return "§" + keyOrder.toString();
		}
	}

	private static final class StaticOrder
			implements Ord<Object> {

		private final Ordering ordering;

		StaticOrder( Ordering ordering ) {
			super();
			this.ordering = ordering;
		}

		@Override
		public Ordering ord( Object left, Object right ) {
			return ordering;
		}

		@Override
		public String toString() {
			return ordering.name();
		}
	}

	/**
	 * A kind of adapter from {@link Eq} to {@link Ord}.
	 * 
	 * Elements that are equal by {@linkplain Eq} will result in {@link Ordering#EQ}.
	 * 
	 * @author Jan Bernitt (jan.bernitt@gmx.de)
	 */
	private static final class EqualityOrder<T>
			implements Ord<T> {

		private final Eq<T> equality;

		EqualityOrder( Eq<T> equality ) {
			super();
			this.equality = equality;
		}

		@Override
		public Ordering ord( T left, T right ) {
			if ( equality.holds( left, right ) ) {
				return Ordering.EQ;
			}
			return keep.ord( left, right );
		}

	}

	private static final class FulfillingUnfulfillingOrder<T>
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

	private static final class EqualUnequalOrder<T>
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

	private static final class AbecedarianOrder
			implements Ord<Character> {

		AbecedarianOrder() {
			// make visible
		}

		@Override
		public Ordering ord( Character left, Character right ) {
			return fromComparison( left.compareTo( right ) );
		}

		@Override
		public String toString() {
			return "'a'..'z'";
		}
	}

	private static final class AlphabeticalOrder
			implements Ord<CharSequence> {

		AlphabeticalOrder() {
			// make visible
		}

		@Override
		public Ordering ord( CharSequence left, CharSequence right ) {
			final int length = Math.min( left.length(), right.length() );
			for ( int i = 0; i < length; i++ ) {
				Ordering order = abecedarian.ord( left.charAt( i ), right.charAt( i ) );
				if ( !order.isEq() ) {
					return order;
				}
			}
			return numerical.ord( left.length(), right.length() );
		}

		@Override
		public String toString() {
			return "\"a\"..\"z\"";
		}
	}

	private static final class CalendricalOrder
			implements Ord<Calendar> {

		CalendricalOrder() {
			// make visible
		}

		@Override
		public Ordering ord( Calendar left, Calendar right ) {
			return fromComparison( left.compareTo( right ) );
		}

		@Override
		public String toString() {
			return "(ddmmyyyy)";
		}

	}

	private static final class ChronologicalOrder
			implements Ord<Date> {

		ChronologicalOrder() {
			// make visible
		}

		@Override
		public Ordering ord( Date left, Date right ) {
			return fromComparison( left.compareTo( right ) );
		}

		@Override
		public String toString() {
			return "(ddmmyyyy)";
		}
	}

	private static final class ComparatorOrder<T>
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

		@Override
		public String toString() {
			return "compare()";
		}

	}

	private static final class CompareableOrder<T extends Comparable<T>>
			implements Ord<T> {

		CompareableOrder() {
			// make visible
		}

		@Override
		public Ordering ord( T left, T right ) {
			return fromComparison( left.compareTo( right ) );
		}

		@Override
		public String toString() {
			return "compareTo()";
		}
	}

	private static final class EnumerativeOrder
			implements Ord<java.lang.Enum<?>> {

		EnumerativeOrder() {
			// make visible
		}

		@Override
		public Ordering ord( java.lang.Enum<?> left, java.lang.Enum<?> right ) {
			if ( left.getDeclaringClass() == right.getDeclaringClass() ) {
				return fromComparison( left.ordinal() - right.ordinal() );
			}
			return alphabetical.ord( left.getClass().getCanonicalName(),
					right.getClass().getCanonicalName() );
		}

		@Override
		public String toString() {
			return "(ordinal 0..9)";
		}
	}

	private static final class HashCodeOrder
			implements Ord<Object> {

		HashCodeOrder() {
			// make visible
		}

		@Override
		public Ordering ord( Object left, Object right ) {
			return fromComparison( left.hashCode() - right.hashCode() );
		}

		@Override
		public String toString() {
			return "hashCode()";
		}
	}

	private static final class IdentityOrder
			implements Ord<Object> {

		IdentityOrder() {
			// make visible
		}

		@Override
		public Ordering ord( Object left, Object right ) {
			return fromComparison( System.identityHashCode( left )
					- System.identityHashCode( right ) );
		}

		@Override
		public String toString() {
			return "(identity)";
		}
	}

	private static final class InherentOrder
			implements Ord<Object> {

		InherentOrder() {
			// make visible
		}

		@Override
		public Ordering ord( Object left, Object right ) {
			if ( left.getClass() == right.getClass() ) {
				return ordIdenticalType( left, right );
			}
			return identity.ord( left, right );
		}

		private Ordering ordIdenticalType( Object left, Object right ) {
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

		@Override
		public String toString() {
			return "(inherent)";
		}
	}

	private static final class InverseOrder<T>
			implements Ord<T>, Nullproof {

		final Ord<T> order;

		InverseOrder( Ord<T> order ) {
			super();
			this.order = order;
		}

		@Override
		public boolean isNullsave() {
			return Null.isSave( order );
		}

		@Override
		public Ordering ord( T left, T right ) {
			return ord( right, left );
		}

		@Override
		public String toString() {
			return "-" + order;
		}
	}

	private static final class NullsaveOrder<T>
			implements Ord<T>, Nullsave {

		final Ord<T> order;

		NullsaveOrder( Ord<T> order ) {
			super();
			this.order = order;
		}

		@Override
		public final Ordering ord( T left, T right ) {
			if ( left == null ) {
				return Ordering.LT;
			}
			if ( right == null ) {
				return Ordering.GT;
			}
			return order.ord( left, right );
		}

		@Override
		public String toString() {
			return "]" + order + "[";
		}
	}

	private static final class NumericalOrder
			implements Ord<Number> {

		NumericalOrder() {
			//make visible
		}

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

		@Override
		public String toString() {
			return "1..9";
		}
	}

	private static final class OrderAdapterComparator<T>
			implements Comparator<T> {

		private final Ord<T> order;

		OrderAdapterComparator( Ord<T> order ) {
			super();
			this.order = order;
		}

		@Override
		public int compare( T one, T other ) {
			return order.ord( one, other ).intValue();
		}

		@Override
		public String toString() {
			return order.toString();
		}
	}

	private static final class QuantifiableOrder
			implements Ord<Quantifiable> {

		QuantifiableOrder() {
			// make visible
		}

		@Override
		public Ordering ord( Quantifiable left, Quantifiable right ) {
			return fromComparison( left.ordinal() - right.ordinal() );
		}

		@Override
		public String toString() {
			return "ord(1..9)";
		}
	}

	private static final class SubOrder<T>
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

		@Override
		public String toString() {
			return primary + " => " + secondary;
		}
	}

	/**
	 * Order is given by a sequence. All elements not contained in the sequence are considered to be
	 * less than any element contained.
	 * 
	 * @author Jan Bernitt (jan.bernitt@gmx.de)
	 */
	private static final class SequenceOrder<T>
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

		@Override
		public String toString() {
			return seq.toString();
		}
	}

	private static final class TypeawareOrder<T>
			implements Ord<Object>, Nullsave {

		final Class<T> type;
		final Ord<? super T> order;

		TypeawareOrder( Class<T> type, Ord<? super T> order ) {
			super();
			this.type = type;
			this.order = order;
		}

		@SuppressWarnings ( "unchecked" )
		@Override
		public Ordering ord( Object left, Object right ) {
			final boolean instLeft = type.isInstance( left );
			final boolean instRight = type.isInstance( right );
			if ( instLeft && instRight ) {
				return order.ord( (T) left, (T) right );
			}
			if ( instLeft ) {
				return Ordering.GT;
			}
			if ( instRight ) {
				return Ordering.LT;
			}
			return Ordering.EQ;
		}

		@Override
		public String toString() {
			return "[" + type.getSimpleName() + ":" + order + "]";
		}
	}
}
