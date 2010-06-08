package de.jbee.util;

import java.util.Comparator;
import java.util.Iterator;

public abstract class AbstractBag<T, C extends IBag<T, C>, M extends Iterable<T>>
		extends AbstractCollection<T>
		implements IBag<T, C> {

	/**
	 * Adds the element e to the mutable collection.
	 */
	protected abstract void add( M mutableCollection, T e );

	/**
	 * Adds all elements of src to dest.
	 */
	protected abstract void addAll( M dest, M src );

	/**
	 * Creates a new mutable collection contains all elements of this collection.
	 * 
	 * @param expectedAdditionalSize
	 *            The maximum expected amount of element will be added to the returned collection
	 *            (in addition to the one in this collection) until the collection is wrapped into a
	 *            immutable one.
	 */
	protected abstract M clone( int expectedAdditionalSize );

	/**
	 * Returns true if the collection contains the element e.
	 */
	protected abstract boolean contains( M mutableCollection, T e );

	/**
	 * A empty immutable collection.
	 */
	protected abstract C empty();

	/**
	 * Creates a new empty mutable collection.
	 * 
	 * @param expectedSize
	 *            This is a hint tells how large the collection will grow before it wrapped into a
	 *            immutable one. This can be used to optimize the internal data structure of the
	 *            create mutable collection.
	 */
	protected abstract M empty( int expectedSize );

	/**
	 * Creates a read-only instance of the mutable collection instance.
	 */
	protected abstract C readonly( M bag );

	/**
	 * This immutable collection.
	 */
	protected abstract C self();

	@Override
	public int count( T e ) {
		return countBy( Equal.<T> equals(), e );
	}

	@Override
	public int countBy( IEquality<? super T> equality, T e ) {
		int c = 0;
		for ( final T e2 : this ) {
			if ( equality.is( e, e2 ) ) {
				c++;
			}
		}
		return c;
	}

	@Override
	public C delete( T e ) {
		return deleteBy( equalsEquality(), e );
	}

	@Override
	public final C difference( ICluster<T> other ) {
		return differenceBy( equalsEquality(), other );
	}

	@Override
	public C differenceBy( IEquality<? super T> equality, ICluster<T> other ) {
		if ( other.isEmpty() ) {
			return self();
		}
		final M bag = empty( size() + other.size() );
		for ( final T e : other ) {
			if ( !any( e ) ) {
				add( bag, e );
			}
		}
		for ( final T e : this ) {
			if ( !contains( bag, e ) ) {
				add( bag, e );
			}
		}
		return readonly( bag );
	}

	@Override
	public C filter( ICondition<? super T> filterFunction ) {
		final M bag = empty( size() );
		for ( final T e : this ) {
			if ( filterFunction.fulfilledBy( e ) ) {
				add( bag, e );
			}
		}
		return readonly( bag );
	}

	@Override
	public final C intersect( ICluster<T> other ) {
		return intersectBy( equalsEquality(), other );
	}

	@Override
	public C intersectBy( IEquality<? super T> equality, ICluster<T> other ) {
		if ( other.isEmpty() ) {
			return empty();
		}
		final M bag = empty( Math.min( size(), other.size() ) );
		for ( final T e : other ) {
			if ( anyBy( equality, e ) ) {
				add( bag, e );
			}
		}
		return readonly( bag );
	}

	@Override
	public boolean isEqual( C other ) {
		return isEqualBy( equalsEquality(), other );
	}

	@Override
	public boolean isEqualBy( IEquality<? super T> equality, C other ) {
		if ( this == other ) {
			return true;
		}
		if ( size() != other.size() ) {
			return false;
		}
		final Iterator<T> i = iterator();
		final Iterator<T> j = other.iterator();
		while ( i.hasNext() && j.hasNext() ) {
			if ( !equality.is( i.next(), j.next() ) ) {
				return false;
			}
		}
		return true;
	}

	@Override
	public final C nub() {
		return nubBy( equalsEquality() );
	}

	@Override
	public C nubBy( IEquality<? super T> equality ) {
		final M bag = empty( size() );
		for ( final T e : this ) {
			final Iterator<T> i = bag.iterator();
			boolean contained = false;
			while ( i.hasNext() && !contained ) {
				contained = equality.is( e, i.next() );
			}
			if ( !contained ) {
				add( bag, e );
			}
		}
		return null;
	}

	@Override
	public Conditional<C> split( T e, Comparator<? super T> c ) {
		final int size = size();
		final M positives = empty( size );
		final M negatives = empty( size );
		for ( final T e2 : this ) {
			if ( c.compare( e, e2 ) >= 0 ) {
				add( positives, e2 );
			} else {
				add( negatives, e2 );
			}
		}
		return Conditional.<C> of( readonly( positives ), readonly( negatives ) );
	}

	@Override
	public final C union( ICluster<T> other ) {
		return unionBy( equalsEquality(), other );
	}

	@Override
	public C unionBy( IEquality<? super T> equality, ICluster<T> other ) {
		if ( other.isEmpty() ) {
			return self();
		}
		final M bag = clone( other.size() );
		for ( final T e : other ) {
			final Iterator<T> i = bag.iterator();
			boolean contained = false;
			while ( i.hasNext() && !contained ) {
				contained = equality.is( e, i.next() );
			}
			if ( !contained ) {
				add( bag, e );
			}
		}
		return readonly( bag );
	}

	@Override
	public C deleteBy( IEquality<? super T> equality, T e ) {
		final M set = empty( size() );
		for ( final T e2 : this ) {
			if ( !equality.is( e, e2 ) ) {
				add( set, e2 );
			}
		}
		return readonly( set );
	}

	@Override
	public Conditional<C> partition( ICondition<? super T> condition ) {
		final int size = size();
		final M positives = empty( size );
		final M negatives = empty( size );
		for ( final T e : this ) {
			if ( condition.fulfilledBy( e ) ) {
				add( positives, e );
			} else {
				add( negatives, e );
			}
		}
		return Conditional.<C> of( readonly( positives ), readonly( negatives ) );
	}
}
