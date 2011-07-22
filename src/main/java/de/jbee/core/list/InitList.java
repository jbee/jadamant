package de.jbee.core.list;

import de.jbee.core.list.StackList.StackEnumListerFactory;
import de.jbee.core.list.StackList.StackLister;

public interface InitList {

	Lister LISTER = new StackLister();
	EnumeratorFactory LISTER_FACTORY = new StackEnumListerFactory();

}
