package de.jbee.lang.seq;

import java.util.Arrays;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;

import de.jbee.lang.Array;
import de.jbee.lang.List;

public class ListMatcher {

	public static Matcher<List<Character>> hasEqualCharactersAsIn( CharSequence expected ) {
		Character[] characters = new Character[expected.length()];
		for ( int i = 0; i < characters.length; i++ ) {
			characters[i] = expected.charAt( i );
		}
		return new EqualElementsMatcher<Character>( characters );
	}

	public static <E> Matcher<List<E>> hasEqualElementsAsIn( E... expected ) {
		return new EqualElementsMatcher<E>( expected );
	}

	public static <E> Matcher<List<E>> hasNoElements( Class<E> type ) {
		return hasEqualElementsAsIn( Array.newInstance( type, 0 ) );
	}

	static final class EqualElementsMatcher<E>
			extends BaseMatcher<List<E>> {

		private final E[] expected;

		EqualElementsMatcher( E... expected ) {
			super();
			this.expected = expected;
		}

		@Override
		public boolean matches( Object item ) {
			if ( ! ( item instanceof List<?> ) ) {
				return false;
			}
			List<?> actual = ( (List<?>) item );
			int length = actual.length();
			if ( expected == null || expected.length == 0 ) {
				return length == 0;
			}
			if ( length != expected.length ) {
				return false;
			}
			for ( int i = 0; i < expected.length; i++ ) {
				if ( !expected[i].equals( actual.at( i ) ) ) {
					return false;
				}
			}
			return true;
		}

		@Override
		public void describeTo( Description description ) {
			description.appendText( "Equal elements as in " + Arrays.toString( expected ) );
		}

	}
}
