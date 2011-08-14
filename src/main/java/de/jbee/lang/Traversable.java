package de.jbee.lang;

public interface Traversable<E> {

	void traverse( int start, Traversal<? super E> traversal );
}
