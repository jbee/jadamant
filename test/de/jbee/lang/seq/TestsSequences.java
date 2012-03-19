package de.jbee.lang.seq;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith ( Suite.class )
@SuiteClasses ( { TestList.class, TestSet.class, TestBag.class, TestMap.class, TestMultimap.class,
		TestListComposition.class, TestEnumLister.class, TestEvolutionLister.class,
		TestListIndex.class, TestListAlteration.class, TestListModification.class,
		TestReverseList.class } )
public class TestsSequences {
	// suite for seq package
}
