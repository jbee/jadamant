package de.jbee.core.list;

public interface Enumerator<E> {

	List<E> stepwiseFromTo( E start, E end, int increment );

}
