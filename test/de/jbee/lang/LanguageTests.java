package de.jbee.lang;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import de.jbee.lang.seq.TestList;
import de.jbee.lang.seq.TestLister;
import de.jbee.lang.seq.TestSet;

@RunWith ( Suite.class )
@SuiteClasses ( { TestList.class, TestSet.class, TestLister.class } )
public class LanguageTests {
	// all language level tests
}
