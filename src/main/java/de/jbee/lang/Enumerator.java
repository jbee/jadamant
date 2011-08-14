package de.jbee.lang;

public interface Enumerator<E> {

	List<E> stepwiseFromTo( E first, E last, int increment );

}
