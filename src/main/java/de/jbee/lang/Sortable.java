package de.jbee.lang;

/**
 * Can be implemented by enums to easily support {@link Ord} interface by using
 * {@link Order#sortable}.
 * 
 * The {@linkplain Sortable} interface is an alternative to the problematic {@link Comparable}
 * interface which is more common these days.
 * 
 * <h4>What's the problem with {@linkplain Comparable} ?</h4>
 * <p>
 * It has a strong connection to the idea of a natural order. A class is limited to exact one
 * natural order given by the way the compare() method is implemented. This includes the decision
 * over ascending or descending ordering. No matter what your decision will be - someone will need
 * it the other way around.
 * </p>
 * <p>
 * The second and more serious issue with it is the kind of code it enforces. Code that handles
 * comparable values in a generic way often has to refer to a type with a self referring generic
 * like:
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
 * {@link ClassCastException} or not. A similar problem raised by null arguments. Is it valid to
 * call it with null ? Or will that lead to a NPE ? And if you check for null in every of your
 * implementation this is fairly code duplication. Code having the same intention. In both cases you
 * end up with guessing: <i>"I guess it will work with these two... both are Comparable..."</i>
 * </p>
 * 
 * @author Jan Bernitt (jan.bernitt@gmx.de)
 */
//TODO rename this to something else and use Sortable in combination with Sorted
public interface Sortable {

	int ordinal();

}
