package de.jbee.core.list;


public interface EnumLister<E> {

	List<E> from( E start );

	List<E> fromTo( E start, E end );
}
