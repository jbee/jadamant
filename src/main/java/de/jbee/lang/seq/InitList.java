package de.jbee.lang.seq;

import de.jbee.lang.EnumeratorFactory;
import de.jbee.lang.Lister;


public interface InitList {

	Lister LISTER = StackList.LISTER;
	EnumeratorFactory ENUMERATOR_FACTORY = EnumList.ENUMERATOR_FACTORY;

}