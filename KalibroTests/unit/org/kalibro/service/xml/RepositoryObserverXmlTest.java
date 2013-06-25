package org.kalibro.service.xml;

import org.kalibro.RepositoryObserver;


public class RepositoryObserverXmlTest extends XmlTest {

	@Override
	protected Class<?> entityClass() {
		return RepositoryObserver.class;
	}
	
	@Override
	protected void verifyElements() {
		assertElement("name", String.class, true);
		assertElement("email", String.class, true);
	}

}
