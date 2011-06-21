package de.jbee.core.list;

public interface EnumLister<E> {

	List<E> stepwiseFromTo( E start, E end, int increment );

}
