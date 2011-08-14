package de.jbee.util;

import java.util.Comparator;

import de.jbee.lang.Fulfills;
import de.jbee.lang.Predicate;

public abstract class AbstractCollection<T>
		implements ICollection<T> {

	protected IEquality<T> equalsEquality() {
		return Equal.equals();
	}

	@Override
	public boolean any( Predicate<? super T> condition ) {
		for ( final T e : this ) {
			if ( condition.fulfilledBy( e ) ) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean anyBy( IEquality<? super T> equality, T e ) {
		for ( final T o : this ) {
			if ( equality.is( o, e ) ) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean all( Predicate<? super T> condition ) {
		return !any( Fulfills.not( condition ) );
	}

	@Override
	public boolean all( T e ) {
		return all( Fulfills.equality( equalsEquality(), e ) );
	}

	@Override
	public boolean allOf( ICluster<T> other ) {
		for ( final T e : other ) {
			if ( !any( e ) ) {
				return false;
			}
		}
		return true;
	}

	@Override
	public T find( Predicate<? super T> condition ) {
		return find( condition, null );
	}

	@Override
	public T find( Predicate<? super T> condition, T noMatchValue ) {
		for ( final T e : this ) {
			if ( condition.fulfilledBy( e ) ) {
				return e;
			}
		}
		return noMatchValue;
	}

	@Override
	public T maximumBy( Comparator<? super T> c ) {
		T max = null;
		for ( final T e : this ) {
			if ( c.compare( max, e ) > 1 ) {
				max = e;
			}
		}
		return max;
	}

	@Override
	public T minimumBy( Comparator<? super T> c ) {
		T min = null;
		for ( final T e : this ) {
			if ( c.compare( min, e ) < 1 ) {
				min = e;
			}
		}
		return min;
	}

}
