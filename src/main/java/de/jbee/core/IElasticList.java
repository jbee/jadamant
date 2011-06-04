package de.jbee.core;

import de.jbee.util.ICluster;

public interface IElasticList<E>
		extends ICluster<E>, IIndexAccessible<E>, IPrepandable<E>, IAppendable<E> {

	/*
	 * override inherited methods with extending return types
	 */

	/**
	 * Expect this to be the fastest way to add elements. Almost O(1) in every case.
	 */
	IElasticList<E> prepand( E e );

	/**
	 * Expect this to be much slower than {@link #prepand(Object)}. Only use this when
	 * {@link ElasticList#reverse(IElasticList)} a list so a <code>append</code> is in fact a
	 * <code> prepand</code>.
	 */
	IElasticList<E> append( E e );

	/*
	 * additional methods
	 */

	//TODO IElasticList<E> prepand(ICluster<E> elems);

	//TODO IElasticList<E> append(ICluster<E> elems);

	IElasticList<E> concat( IElasticList<E> other );

	IElasticList<E> insert( E e, int index );

	IElasticList<E> delete( int index );

	IElasticList<E> tidyUp();

	IElasticList<E> takeL( int beginning );

	IElasticList<E> takeR( int ending );

	IElasticList<E> dropL( int beginning );

	IElasticList<E> dropR( int ending );

}
