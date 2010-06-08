package de.jbee.util;

public final class NumberOperator {

	private NumberOperator() {
		// util
	}

	static final class IntPlus<T extends Number>
			implements IFunc2<Integer, Integer, T> {

		@Override
		public Integer exec( Integer o1, T o2 ) {
			return o1 == null
				? o2 == null
					? 0
					: o2.intValue()
				: o2 == null
					? o1
					: o1.intValue() + o2.intValue();
		}
	}

	private static final IntPlus<Number> INT_PLUS = new IntPlus<Number>();

	@SuppressWarnings ( "unchecked" )
	public static <N extends Number> IFunc2<Integer, Integer, N> plusInt() {
		return (IFunc2<Integer, Integer, N>) INT_PLUS;
	}
}
