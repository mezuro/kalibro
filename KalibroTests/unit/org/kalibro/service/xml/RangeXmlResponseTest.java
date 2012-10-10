package org.kalibro.service.xml;

public class RangeXmlResponseTest extends XmlTest {

	@Override
	public void verifyElements() {
		assertElement("id", Long.class);
		assertElement("beginning", Double.class);
		assertElement("end", Double.class);
		assertElement("comments", String.class);
	}
}