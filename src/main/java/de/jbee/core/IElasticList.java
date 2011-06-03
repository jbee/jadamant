package de.jbee.core;

import de.jbee.util.ICluster;

public interface IElasticList<E>
		extends ICluster<E>, IIndexAccessible<E>, IPrepandable<E>, IAppendable<E> {

	/*
	 * override inherited methods with extending return types
	 */

	IElasticList<E> prepand( E e );

	IElasticList<E> append( E e );

	/*
	 * additional methods
	 */

	//TODO IElasticList<E> prepand(Iterable<E> elems);

	IElasticList<E> insert( E e, int index );

	IElasticList<E> delete( int index );

	IElasticList<E> tidyUp();

	IElasticList<E> takeL( int beginning );

	IElasticList<E> takeR( int ending );

	//TODO IElasticList<E> dropL(int beginning);

	//TODO IElasticList<E> dropR(int ending);

}
