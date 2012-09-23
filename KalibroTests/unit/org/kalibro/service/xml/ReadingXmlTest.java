package org.kalibro.service.xml;

import org.junit.Test;
import org.kalibro.Reading;

public class ReadingXmlTest extends XmlTest<Reading, ReadingXml> {

	@Override
	protected Reading loadFixture() {
		return loadFixture("excellent", Reading.class);
	}

	@Test
	public void verifyElements() {
		assertElement("id", Long.class, false);
		assertElement("label", String.class, true);
		assertElement("grade", Double.class, true);
		assertElement("color", String.class, true);
	}
}