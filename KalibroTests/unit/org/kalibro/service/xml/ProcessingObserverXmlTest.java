package org.kalibro.service.xml;

import org.kalibro.ProcessingObserver;


public class ProcessingObserverXmlTest extends XmlTest {

	@Override
	protected Class<?> entityClass() {
		return ProcessingObserver.class;
	}
	
	@Override
	protected void verifyElements() {
		assertElement("name", String.class, true);
		assertElement("email", String.class, true);
	}

}
