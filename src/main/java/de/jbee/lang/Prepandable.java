package de.jbee.lang;

/**
 * A sequence allows to {@link #prepand(Object)} further elements in front of it.
 * 
 * Make use of the covariant return type override in more specific sequences when possible!
 * 
 * @author Jan Bernitt (jan.bernitt@gmx.de)
 */
public interface Prepandable<E> {

	/**
	 * @return A sequence containing <code>e</code> as its first element. This sequence kept
	 *         unchanged.
	 */
	Prepandable<E> prepand( E e );
}
