package org.kalibro.service.xml;

public class ProjectXmlTest extends XmlTest {

	@Override
	public void verifyElements() {
		assertElement("id", Long.class);
		assertElement("name", String.class, true);
		assertElement("description", String.class);
	}
}