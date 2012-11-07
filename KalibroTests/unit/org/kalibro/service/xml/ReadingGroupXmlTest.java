package org.kalibro.service.xml;

public class ReadingGroupXmlTest extends XmlTest {

	@Override
	protected void verifyElements() {
		assertElement("id", Long.class);
		assertElement("name", String.class, true);
		assertElement("description", String.class);
	}
}