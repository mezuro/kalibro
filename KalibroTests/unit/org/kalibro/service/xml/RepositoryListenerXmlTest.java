package org.kalibro.service.xml;

import org.kalibro.RepositoryListener;


public class RepositoryListenerXmlTest extends XmlTest {

	@Override
	protected Class<?> entityClass() {
		return RepositoryListener.class;
	}
	
	@Override
	protected void verifyElements() {
		assertElement("name", String.class, true);
		assertElement("email", String.class, true);
	}

}
