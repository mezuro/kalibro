package org.kalibro.service.xml;

public class ReadingXmlTest extends XmlTest {

	@Override
	public void verifyElements() {
		assertElement("id", Long.class);
		assertElement("label", String.class, true);
		assertElement("grade", Double.class, true);
		assertElement("color", String.class, true);
	}
}