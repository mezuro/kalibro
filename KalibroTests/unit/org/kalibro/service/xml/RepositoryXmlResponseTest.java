package org.kalibro.service.xml;

import org.kalibro.RepositoryType;

public class RepositoryXmlResponseTest extends XmlTest {

	@Override
	protected void verifyElements() {
		assertElement("id", Long.class);
		assertElement("name", String.class);
		assertElement("description", String.class);
		assertElement("license", String.class);
		assertElement("processPeriod", Integer.class);
		assertElement("type", RepositoryType.class);
		assertElement("address", String.class);
	}
}