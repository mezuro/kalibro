package org.kalibro.service.xml;

public class ThrowableXmlTest extends XmlTest {

	@Override
	public void verifyElements() {
		assertElement("throwableClass", String.class);
		assertElement("detailMessage", String.class);
		assertElement("cause", ThrowableXml.class);
		assertCollection("stackTrace");
	}
}