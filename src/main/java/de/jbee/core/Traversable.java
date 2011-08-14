package de.jbee.core;

public interface Traversable<E> {

	void traverse( int start, Traversal<? super E> traversal );
}
