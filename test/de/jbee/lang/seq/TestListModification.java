package de.jbee.lang.seq;

import static de.jbee.lang.seq.ListMatcher.hasEqualCharactersAsIn;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import de.jbee.lang.List;
import de.jbee.lang.ListModification;

public class TestListModification {

	@Test
	public void testInsertList() {
		List<Character> mid = List.with.charactersIn( "mid" );
		List<Character> natt = List.with.charactersIn( "natt" );
		List<Character> midnatt = mid.concat( natt );
		ListModification<Character> insertSommarAtN = List.a.insertAt(
				List.with.charactersIn( "sommar" ), List.indexFor.elem( 'n' ) );
		assertThat( insertSommarAtN.in( midnatt ), hasEqualCharactersAsIn( "midsommarnatt" ) );
		assertThat( insertSommarAtN.in( mid ), hasEqualCharactersAsIn( "mid" ) );
		assertThat( insertSommarAtN.in( natt ), hasEqualCharactersAsIn( "sommarnatt" ) );
	}
}
