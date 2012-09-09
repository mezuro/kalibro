package org.kalibro.service.xml;

import org.junit.Test;
import org.kalibro.Reading;

public class ReadingXmlTest extends XmlTest<Reading, ReadingXml> {

	@Override
	protected Reading loadFixture() {
		return loadFixture("/org/kalibro/reading-excellent", Reading.class);
	}

	@Override
	protected Class<ReadingXml> dtoClass() {
		return ReadingXml.class;
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void verifyElements() {
		assertElement("id", Long.class, false);
		assertElement("label", String.class, true);
		assertElement("grade", Double.class, true);
		assertElement("color", String.class, true);
	}
}