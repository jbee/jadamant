package de.jbee.core.list;

public interface Enumerator<E> {

	List<E> stepwiseFromTo( E first, E last, int increment );

}
