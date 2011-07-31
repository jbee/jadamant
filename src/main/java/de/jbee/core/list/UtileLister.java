package de.jbee.core.list;

import de.jbee.core.Array;
import de.jbee.util.ICluster;

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

	@Override
	public <E> List<E> elements( E... elems ) {
		return utilised.elements( elems );
	}

	@Override
	public <E> List<E> elements( ICluster<E> elems ) {
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
		Object[] stack = new Object[StackList.nextHighestPowerOf2( n )];
		Array.fill( stack, e, stack.length - n, n );
		return StackList.tidy( n, stack, utilised.<E> noElements() );
	}
}
