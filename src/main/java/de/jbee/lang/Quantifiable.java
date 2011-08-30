package de.jbee.lang;

/**
 * Can be implemented by enums to easily support {@link Ord} interface by using
 * {@link Order#quantifiable}.
 * <p>
 * The {@linkplain Quantifiable} interface is an alternative to the problematic {@link Comparable}
 * interface which is more common these days.
 * </p>
 * 
 * <h4>What's the problem with {@linkplain Comparable} ?</h4>
 * <p>
 * It has a strong connection to the illusion about the existence of a natural order. With natural
 * order a class is limited to exact one order given by the way its <code>compare()</code> -method
 * is implemented (there might be a chance using overrides but than it gets really weird). This
 * includes the decision over ascending or descending ordering. No matter what your decision will be
 * - someone will need it the other way around or both in different runs.
 * </p>
 * <p>
 * The second and more serious issue is the kind of code it enforces. Code that handles comparable
 * values in a generic way often has to refer to a type with a self referring generic like:
 * 
 * <pre>
 * T extends Compareable&lt;T&gt;
 * </pre>
 * 
 * These kind of generic is hard to guarantee, hard to cast to. In fact you cannot tell easily if
 * you actually can call the compare method when holding two {@link Object}s in hands. When found
 * that a value is comparable by checking through <code>instanceof</code> its just clear it is
 * comparable in general. It does not tell you to what kind of value and it might get very dirty to
 * be able to tell if you can pass another value into the compare method without getting a
 * {@link ClassCastException} or not. A similar problem raised by <code>null</code> arguments. Is it
 * valid to call it with null ? Or will that lead to a {@link NullPointerException} ? And if you
 * check for null in every of your implementation this is fairly code duplication. Code having the
 * same intention. In both cases you end up guessing or writing a hole suit of type inferring code.
 * </p>
 * 
 * @author Jan Bernitt (jan.bernitt@gmx.de)
 */
public interface Quantifiable {

	int ordinal();

}
