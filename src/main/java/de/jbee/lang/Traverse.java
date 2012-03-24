package de.jbee.lang;

import de.jbee.lang.dev.Null;
import de.jbee.lang.dev.Nullproof;
import de.jbee.lang.dev.Nullsave;

public final class Traverse {

	public static final Traverse instance = new Traverse();

	private Traverse() {
		// singleton
	}

	static final class Res<T> {

		public static <T> Res<T> of( T init ) {
			return new Res<T>( init );
		}

		public static <T> Res<T> newInstance() {
			return new Res<T>( null );
		}

		public T value;

		private Res( T init ) {
			this.value = init;
		}
	}

	public <E> E[] toArray( List<E> t, Class<E> elementType ) {
		E[] res = Array.newInstance( elementType, t.length() );
		t.traverse( 0, new ToArrayTraversal<E>( res, 0 ) );
		return res;
	}

	//TODO find a way to model fold by a ListTransformation<E,E> 
	public static <E> ListTransformation<E, E> fold( Op<E> op, E init ) {
		Res<E> res = Res.of( init );
		//l.traverse( 0, new FoldByOperatorTraversal<E>( op, res ) );
		return null;
	}

	private static class FoldL<E>
			implements ListTransformation<E, E> {

		@Override
		public E on( List<E> list ) {
			// TODO Auto-generated method stub
			return null;
		}

	}

	static interface Accumulator<E> {

		void accumulate( E element );
	}

	//OPEN how can a traverse build up a list and return it ? maybe traverse needs a second method and generic ? -> or some kind of ListBuilder passed into and feeded by the Traversal

	//OPEN verify a design by the wc example - count characters/words/lines in a single traverse

	static final class AnyTraversal<E>
			implements Traversal<E> {

		private final Predicate<E> predicate;

		AnyTraversal( Predicate<E> predicate ) {
			super();
			this.predicate = predicate;
		}

		@Override
		public int incrementOn( E e ) {
			//TODO
			return 0;
		}

	}

	static abstract class IndexDependendTraversal<E>
			implements Traversal<E> {

		private int index = 0;

		@Override
		public int incrementOn( E e ) {
			int res = incrementOn( index, e );
			index += res;
			return res;
		}

		abstract int incrementOn( int index, E e );

	}

	static final class EveryNthTraversal<E>
			implements Traversal<E>, Nullproof {

		private final int multiplier;
		private final Traversal<E> successional;

		EveryNthTraversal( int multiplier, Traversal<E> successional ) {
			super();
			this.multiplier = multiplier;
			this.successional = successional;
		}

		@Override
		public int incrementOn( E e ) {
			return multiplier * successional.incrementOn( e );
		}

		@Override
		public boolean isNullsave() {
			return Null.isSave( successional );
		}

	}

	/**
	 * Combines two traversal into one. Can be used to build an endless chain of traversals whereby
	 * any number of traversals can be executed in a single traverse.
	 */
	static final class DualTraversal<E>
			implements Traversal<E>, Nullproof {

		private final Traversal<E> fst;
		private final Traversal<E> snd;

		private int incFst = 0;
		private int incSnd = 0;

		DualTraversal( Traversal<E> fst, Traversal<E> snd ) {
			super();
			this.fst = fst;
			this.snd = snd;
		}

		@Override
		public int incrementOn( E e ) {
			if ( incFst == incSnd ) {
				incFst = fst.incrementOn( e );
				incSnd = snd.incrementOn( e );
				return incFst < 0 && incSnd < 0
					? STOP_TRAVERSAL
					: Math.min( incFst, incSnd );
			}
			if ( incFst < incSnd ) {
				incSnd -= incFst;
				incFst = fst.incrementOn( e );
			} else {
				incFst -= incSnd;
				incSnd = snd.incrementOn( e );
			}
			return Math.min( incFst, incSnd );
		}

		@Override
		public boolean isNullsave() {
			return Null.isSave( fst ) && Null.isSave( snd );
		}

	}

	/**
	 * Passes each element n-times to the inner traversal.
	 */
	static class ElementLoopingTraversal<E>
			implements Traversal<E>, Nullproof {

		private final int n;
		private final Traversal<E> oneTime;

		ElementLoopingTraversal( int n, Traversal<E> oneTime ) {
			super();
			this.n = n;
			this.oneTime = oneTime;
		}

		@Override
		public int incrementOn( E e ) {
			int res = 1;
			for ( int i = 0; i < n; i++ ) {
				res = oneTime.incrementOn( e );
				if ( res < 0 ) {
					return res;
				}
			}
			return res;
		}

		@Override
		public boolean isNullsave() {
			return Null.isSave( oneTime );
		}

	}

	static class ToArrayTraversal<E>
			implements Traversal<E>, Nullsave {

		private final Object[] dest;
		private final int lastDestIndex;

		private int index;

		ToArrayTraversal( Object[] dest, int startIndex ) {
			super();
			this.dest = dest;
			this.index = startIndex;
			this.lastDestIndex = dest.length - 1;
		}

		@Override
		public int incrementOn( E e ) {
			if ( index < lastDestIndex ) {
				dest[index++] = e;
				return 1;
			}
			if ( index == lastDestIndex ) {
				dest[index++] = e;
			}
			return STOP_TRAVERSAL;
		}

	}

	/**
	 * Folds by applying a binary operator to all elements.
	 */
	static class FoldByOperatorTraversal<E>
			implements Traversal<E>, Nullproof {

		private final Op<E> op;
		private final Res<E> res;

		FoldByOperatorTraversal( Op<E> op, Res<E> init ) {
			super();
			this.op = op;
			this.res = init;
		}

		@Override
		public int incrementOn( E e ) {
			res.value = op.operate( res.value, e );
			return 1;
		}

		@Override
		public boolean isNullsave() {
			return Null.isSave( op );
		}

	}

	static class OnceThisThenThatTraversal<E>
			implements Traversal<E> {

		private final Traversal<E> b;
		private final Traversal<E> a;
		private Traversal<E> current;

		OnceThisThenThatTraversal( Traversal<E> thiz, Traversal<E> that ) {
			super();
			this.a = thiz;
			this.b = that;
			this.current = thiz;
		}

		@Override
		public int incrementOn( E e ) {
			int res = current.incrementOn( e );
			if ( current != b ) {
				current = b;
			}
			return res;
		}

		@Override
		public String toString() {
			return "1#" + a + ">1#" + b;
		}
	}

}
