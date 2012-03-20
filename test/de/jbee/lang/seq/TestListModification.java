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
		ListIndex firstN = List.indexFor.elem( 'n' );
		ListModification<Character> insertSommarAtN = List.modifyBy.insertAt( firstN, sommar );
		assertThat( insertSommarAtN.on( midnatt ), hasEqualCharactersAsIn( "midsommarnatt" ) );
		assertThat( insertSommarAtN.on( mid ), hasEqualCharactersAsIn( "mid" ) );
		assertThat( insertSommarAtN.on( natt ), hasEqualCharactersAsIn( "sommarnatt" ) );
	}

	@Test
	public void prepandModificationShouldBeSameAsUsingListPrepand() {
		assertThat( List.modifyBy.prepand( 'O' ).on( List.with.element( 'K' ) ),
				hasEqualCharactersAsIn( "OK" ) );
	}

	@Test
	public void appendModificationShouldBeSameAsUsingListPrepand() {
		assertThat( List.modifyBy.append( 'K' ).on( List.with.element( 'O' ) ),
				hasEqualCharactersAsIn( "OK" ) );
	}

	@Test
	public void concatModificationShouldBeCombinableWithOtherModification() {
		assertThat( List.modifyBy.concat( List.modifyBy.prepand( 'O' ) ).on(
				List.with.element( 'K' ) ), hasEqualCharactersAsIn( "KOK" ) );
	}

	@Test
	public void concatModificationShouldBeSameAsUsingListConcat() {
		assertThat( List.modifyBy.concat( List.with.elements( 'D', 'C' ) ).on(
				List.with.elements( 'A', 'C' ) ), hasEqualCharactersAsIn( "ACDC" ) );
	}

	@Test
	public void embedShouldWorkAsQuoteFunction() {
		assertThat( List.modifyBy.embed( '<', '>' ).on( List.with.charactersIn( "hey!" ) ),
				hasEqualCharactersAsIn( "<hey!>" ) );
	}
}
