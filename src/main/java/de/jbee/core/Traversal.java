package de.jbee.core;

public interface Traversal<E> {

	int STOP_TRAVERSAL = -1;

	int incrementOn( E e );
}
