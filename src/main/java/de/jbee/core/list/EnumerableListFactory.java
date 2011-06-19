package de.jbee.core.list;


public interface EnumerableListFactory<E> {

	List<E> from( E start );

	List<E> fromTo( E start, E end );
}
