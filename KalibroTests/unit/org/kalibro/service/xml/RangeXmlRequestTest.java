package org.kalibro.service.xml;

public class RangeXmlRequestTest extends XmlTest {

	@Override
	public void verifyElements() {
		assertElement("id", Long.class);
		assertElement("beginning", Double.class, true);
		assertElement("end", Double.class, true);
		assertElement("comments", String.class);
		assertElement("readingId", Long.class);
	}
}