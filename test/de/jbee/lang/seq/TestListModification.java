package de.jbee.lang.seq;

import static de.jbee.lang.seq.ListMatcher.hasEqualCharactersAsIn;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import de.jbee.lang.List;
import de.jbee.lang.ListIndex;
import de.jbee.lang.ListModification;

public class TestListModification {

	@Test
	public void testInsertList() {
		List<Character> mid = List.with.charactersIn( "mid" );
		List<Character> natt = List.with.charactersIn( "natt" );
		List<Character> midnatt = mid.concat( natt );
		List<Character> sommar = List.with.charactersIn( "sommar" );
		ListIndex atN = List.indexFor.elem( 'n' );
		ListModification<Character> insertSommarAtN = List.modify.insertAt( atN, sommar );
		assertThat( insertSommarAtN.in( midnatt ), hasEqualCharactersAsIn( "midsommarnatt" ) );
		assertThat( insertSommarAtN.in( mid ), hasEqualCharactersAsIn( "mid" ) );
		assertThat( insertSommarAtN.in( natt ), hasEqualCharactersAsIn( "sommarnatt" ) );
	}
}
