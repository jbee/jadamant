package de.jbee.lang.seq;

import de.jbee.lang.Array;
import de.jbee.lang.Lang;
import de.jbee.lang.List;
import de.jbee.lang.Lister;
import de.jbee.lang.Sequence;

public class UtileLister
		implements Lister {

	private final Lister utilised;

	public UtileLister( Lister utilised ) {
		super();
		this.utilised = utilised;
	}

	@Override
	public <E> List<E> element( E e ) {
		return utilised.element( e );
	}

	//TODO utility methods for primitive array types ? -> List<Integer> elements(int...)

	@Override
	public <E> List<E> elements( E... elems ) {
		return utilised.elements( elems );
	}

	@Override
	public <E> List<E> elements( Sequence<E> elems ) {
		return utilised.elements( elems );
	}

	@Override
	public <E> List<E> noElements() {
		return utilised.noElements();
	}

	public List<Character> charactersIn( CharSequence seq ) {
		//TODO a StringList impl. that just wraps strings as a list
		Character[] elems = new Character[seq.length()];
		for ( int i = 0; i < elems.length; i++ ) {
			elems[i] = seq.charAt( i );
		}
		return elements( elems );
	}

	public <E> List<E> readonly( java.util.Collection<E> c ) {
		return HarpList.tidy( c.size(), c.toArray() );
	}

	public <E> List<E> readonly( java.util.Iterator<E> c ) {
		List<E> res = noElements();
		while ( c.hasNext() ) {
			res = res.append( c.next() );
		}
		return res;
	}

	/**
	 * lines breaks a string up into a list of strings at newline characters. The resulting strings
	 * do not contain newlines.
	 */
	public List<String> linesIn( String text ) {
		if ( text == null || text.isEmpty() ) {
			return noElements();
		}
		return elements( text.split( "\n+" ) );
	}

	/**
	 * words breaks a string up into a list of words, which were delimited by white space.
	 */
	public List<String> wordsIn( String text ) {
		if ( text == null || text.isEmpty() ) {
			return noElements();
		}
		return elements( text.split( "\\s+" ) );
	}

	public <E> List<E> replicate( int n, E e ) {
		Object[] stack = new Object[Lang.nextHighestPowerOf2( n )];
		Array.fill( stack, e, stack.length - n, n );
		return HarpList.tidy( n, stack );
	}
}
