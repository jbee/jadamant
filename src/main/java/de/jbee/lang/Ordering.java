package de.jbee.lang;

public enum Ordering
		implements RelationalOp<Object> { // Eq, Ord, Bounded, Enum, Read, Show

	LT,
	EQ,
	GT;

	public boolean isEq() {
		return this == EQ;
	}

	public boolean isLt() {
		return this == LT;
	}

	public boolean isGt() {
		return this == GT;
	}

	public boolean isLe() {
		return this != GT;
	}

	public boolean isGe() {
		return this != LT;
	}

	public int intValue() {
		return this == LT
			? -1
			: this == EQ
				? 0
				: 1;
	}

	public Ordering inverse() {
		return this == LT
			? GT
			: this == GT
				? LT
				: EQ;
	}

	//OPEN transform below methods to functions ?
	//TODO more clear naming

	static Ordering trueFalse( boolean one, boolean other ) {
		return one == other
			? EQ
			: one
				? GT
				: LT;
	}

	static Ordering falseTrue( boolean one, boolean other ) {
		return trueFalse( other, one );
	}

	public static Ordering ascOf( Quantifiable one, Quantifiable other ) {
		return fromComparison( one.ordinal() - other.ordinal() );
	}

	public static Ordering descOf( Quantifiable one, Quantifiable other ) {
		return fromComparison( other.ordinal() - one.ordinal() );
	}

	public static Ordering fromComparison( int comparisonResult ) {
		return comparisonResult < 0
			? LT
			: comparisonResult == 0
				? EQ
				: GT;
	}

	@Override
	public boolean holds( Object left, Object right ) {
		return Order.inherent.ord( left, right ) == this;
	}

}
