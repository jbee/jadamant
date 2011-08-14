package de.jbee.lang.seq;

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
			if ( expected == null && actual.size() > 0 ) {
				return false;
			}
			if ( actual.size() < expected.length ) {
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
			description.appendText( "Equal elements as in " + expected );
		}

	}
}
