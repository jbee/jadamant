package de.jbee.util;

public class StringList
		extends ComparableList<String, StringList> {

	private static final long serialVersionUID = 1L;

	StringList( IList<String> list ) {
		super( list );
	}

	@Override
	protected StringList extend( IList<String> list ) {
		return new StringList( list );
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
