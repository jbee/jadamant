package de.jbee.core;

import org.junit.BeforeClass;

import de.jbee.core.list.CoreList;

public class TestStackLister
		extends TestLister {

	@BeforeClass
	public static void setUp() {
		Core.setUp( CoreList.STACK_LISTER_FACTORY );
	}
}
