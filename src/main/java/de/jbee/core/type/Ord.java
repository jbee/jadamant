package de.jbee.core.type;

/**
 * <p>
 * {@link Ord} is not a property of the value instances being ordered itself since there is no
 * natural order. This is a common misunderstanding. {@link Order} does depend on the current role
 * of the sorted object. Therefore a order is something external that is applied to value objects.
 * It results in a {@link Ordering}.
 * </p>
 * <p>
 * If that seams to force you to violate encapsulation principle simply put the concerned
 * {@linkplain Ord} implementation as a static inner class of your value object. That helps to hide
 * its data and still allows to apply an specific order based on your entity's hidden field(s).
 * </p>
 * <p>
 * There are a bunch of frequently used orderings available in the {@link Order} utility class.
 * </p>
 * 
 * @author Jan Bernitt (jan.bernitt@gmx.de)
 */
public interface Ord<T> {

	Ordering ord( T one, T other );
}
