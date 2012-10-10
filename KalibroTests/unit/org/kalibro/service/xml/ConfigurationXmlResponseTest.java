package org.kalibro.service.xml;

public class ConfigurationXmlResponseTest extends XmlTest {

	@Override
	public void verifyElements() {
		assertElement("id", Long.class);
		assertElement("name", String.class);
		assertElement("description", String.class);
	}
}