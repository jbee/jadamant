package de.jbee.lang.seq;

import java.util.Arrays;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;

import de.jbee.lang.List;

public class ListMatcher {

	public static <E> Matcher<List<E>> hasEqualElementsAsIn( E... expected ) {
		return new EqualElementsMatcher<E>( expected );
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
			if ( expected == null && length > 0 ) {
				return false;
			}
			if ( length < expected.length ) {
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
