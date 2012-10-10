package org.kalibro.service.xml;

import org.kalibro.RepositoryType;

public class RepositoryXmlRequestTest extends XmlTest {

	@Override
	public void verifyElements() {
		assertElement("id", Long.class);
		assertElement("name", String.class, true);
		assertElement("description", String.class);
		assertElement("license", String.class);
		assertElement("processPeriod", Integer.class);
		assertElement("type", RepositoryType.class, true);
		assertElement("address", String.class, true);
		assertElement("configurationId", Long.class, true);
	}
}