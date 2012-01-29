package de.jbee.lang.seq;

import static de.jbee.lang.seq.ListMatcher.hasEqualCharactersAsIn;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import de.jbee.lang.List;
import de.jbee.lang.ListModification;

public class TestListModification {

	@Test
	public void testInsertList() {
		List<Character> mitnatt = List.with.charactersIn( "midnatt" );
		List<Character> mat = List.with.charactersIn( "mat" );
		List<Character> natt = List.with.charactersIn( "natt" );
		ListModification<Character> insertSommarAtN = List.a.insertAt(
				List.with.charactersIn( "sommar" ), List.indexFor.elem( 'n' ) );
		assertThat( insertSommarAtN.in( mitnatt ), hasEqualCharactersAsIn( "midsommarnatt" ) );
		assertThat( insertSommarAtN.in( mat ), hasEqualCharactersAsIn( "mat" ) );
		assertThat( insertSommarAtN.in( natt ), hasEqualCharactersAsIn( "sommarnatt" ) );
	}
}
