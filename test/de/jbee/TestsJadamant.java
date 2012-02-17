package de.jbee;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import de.jbee.data.TestData;
import de.jbee.lang.TestArray;
import de.jbee.lang.TestCalculate;
import de.jbee.lang.TestOrder;
import de.jbee.lang.seq.TestsSequences;

@RunWith ( Suite.class )
@SuiteClasses ( { TestsSequences.class, TestData.class, TestOrder.class, TestCalculate.class,
		TestArray.class } )
public class TestsJadamant {
	// all tests for the the project
}
