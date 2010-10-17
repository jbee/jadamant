package de.jbee.util;

import java.util.Comparator;

public class StringList
		extends ComparatorList<String, StringList> {

	private static final long serialVersionUID = 1L;

	StringList( IList<String> list ) {
		this( list, Collection.<String> comparator() );
	}

	private StringList( IList<String> list, Comparator<String> comparator ) {
		super( list, comparator );
	}

	@Override
	protected StringList extend( IList<String> list ) {
		return new StringList( list );
	}

	@Override
	public StringList comparedBy( Comparator<String> comparator ) {
		return new StringList( getCollection(), comparator );
	}

	/**
	 * creates a string from an array of strings, it inserts space characters between original
	 * strings
	 * 
	 * <pre>
	 * Input: unwords ["aa","bb","cc","dd","ee"]
	 * Output: "aa bb cc dd ee"
	 * </pre>
	 */
	public String unwords() {
		return foldL1( new IFunc2<String, String, String>() {

			@Override
			public String exec( String o1, String o2 ) {
				return o1 + " " + o2;
			}
		}, "" );
	}

}
