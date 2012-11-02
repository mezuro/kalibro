package org.kalibro.service.xml;

public class ThrowableXmlTest extends XmlTest {

	@Override
	protected void verifyElements() {
		assertElement("targetString", String.class, true);
		assertElement("message", String.class);
		assertElement("cause", ThrowableXml.class);
		assertCollection("stackTrace", "stackTraceElement");
	}
}