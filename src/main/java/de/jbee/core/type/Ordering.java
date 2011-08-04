package de.jbee.core.type;

public enum Ordering {

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

	public static Ordering ascOf( Sortable one, Sortable other ) {
		return valueOf( one.ordinal() - other.ordinal() );
	}

	public static Ordering descOf( Sortable one, Sortable other ) {
		return valueOf( other.ordinal() - one.ordinal() );
	}

	public static Ordering valueOf( int value ) {
		return value < 0
			? LT
			: value == 0
				? EQ
				: GT;
	}
}
