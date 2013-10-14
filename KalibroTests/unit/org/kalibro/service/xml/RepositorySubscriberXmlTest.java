package org.kalibro.service.xml;

import org.kalibro.RepositorySubscriber;


public class RepositorySubscriberXmlTest extends XmlTest {

	@Override
	protected Class<?> entityClass() {
		return RepositorySubscriber.class;
	}
	
	@Override
	protected void verifyElements() {
		assertElement("name", String.class, true);
		assertElement("email", String.class, true);
	}

}
