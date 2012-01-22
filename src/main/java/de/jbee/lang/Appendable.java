package de.jbee.lang;

/**
 * The ability to {@link #append(Object)} further elements to this object's internal elements and
 * return a new object having the elements of this object and the appended element as last element.
 * 
 * Make use of the covariant return type override in more specific sequences when possible!
 * 
 * @author Jan Bernitt (jan.bernitt@gmx.de)
 */
public interface Appendable<E> {

	/**
	 * @return A sequence contains <code>e</code> as last element. This object kept unchanged.
	 */
	Appendable<E> append( E e );
}
