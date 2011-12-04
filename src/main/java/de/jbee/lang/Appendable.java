package de.jbee.lang;

/**
 * A sequence allows to {@link #append(Object)} further elements to it.
 * 
 * Make use of the covariant return type override in more specific sequences when possible!
 * 
 * @author Jan Bernitt (jan.bernitt@gmx.de)
 */
public interface Appendable<E> {

	/**
	 * @return A sequence contains <code>e</code> as last element. This sequence kept unchanged.
	 */
	Appendable<E> append( E e );
}
