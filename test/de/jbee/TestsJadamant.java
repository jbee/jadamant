package de.jbee;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import de.jbee.data.TestDataset;
import de.jbee.lang.TestCalculate;
import de.jbee.lang.TestOrder;
import de.jbee.lang.seq.TestsSequences;

@RunWith ( Suite.class )
@SuiteClasses ( { TestsSequences.class, TestDataset.class, TestOrder.class, TestCalculate.class } )
public class TestsJadamant {
	// all tests for the the project
}
