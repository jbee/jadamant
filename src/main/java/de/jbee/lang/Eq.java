package de.jbee.lang;

/**
 * The {@linkplain Eq}-operation is one special case of the {@link RelationalOp} tests for equality
 * of left and right argument.
 * 
 * To be able to limit arguments to just the equality-operation it extends the more general
 * {@linkplain RelationalOp}.
 * 
 * @author Jan Bernitt (jan.bernitt@gmx.de)
 */
public interface Eq<T>
		extends RelationalOp<T> {

	/**
	 * left and right operator become one and other since it doesn't matter for equality tests.
	 */
	@Override
	boolean holds( T one, T other );
}
