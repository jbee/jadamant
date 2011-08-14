package de.jbee.lang.seq;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;

import de.jbee.lang.List;

public class ListMatcher {

	public static <E> Matcher<List<E>> hasEqualElementsAsIn( List<E> expected ) {
		return new EqualElementsMatcher<E>( expected );
	}

	static final class EqualElementsMatcher<E>
			extends BaseMatcher<List<E>> {

		private final List<E> expected;

		EqualElementsMatcher( List<E> expected ) {
			super();
			this.expected = expected;
		}

		@Override
		public boolean matches( Object item ) {
			if ( ! ( item instanceof List<?> ) ) {
				return false;
			}
			List<?> actual = ( (List<?>) item );
			if ( actual.size() < expected.size() ) {
				return false;
			}
			for ( int i = 0; i < expected.size(); i++ ) {
				if ( !expected.at( i ).equals( actual.at( i ) ) ) {
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
