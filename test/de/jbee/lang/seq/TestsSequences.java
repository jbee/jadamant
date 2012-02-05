package de.jbee.lang.seq;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith ( Suite.class )
@SuiteClasses ( { TestList.class, TestSet.class, TestBag.class, TestMap.class, TestMultimap.class,
		TestElementList.class, TestEnumLister.class, TestEvolutionLister.class,
		TestListIndex.class, TestListAlteration.class, TestListModification.class,
		TestReverseList.class } )
public class TestsSequences {
	// all tests for the sequences package
}
