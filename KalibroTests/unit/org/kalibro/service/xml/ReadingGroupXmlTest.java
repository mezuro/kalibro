package org.kalibro.service.xml;

import org.junit.Test;
import org.kalibro.ReadingGroup;

public class ReadingGroupXmlTest extends XmlTest<ReadingGroup, ReadingGroupXml> {

	@Override
	protected ReadingGroup loadFixture() {
		return loadFixture("/org/kalibro/readingGroup-scholar", ReadingGroup.class);
	}

	@Override
	protected Class<ReadingGroupXml> dtoClass() {
		return ReadingGroupXml.class;
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void verifyElements() {
		assertElement("id", Long.class, false);
		assertElement("name", String.class, true);
		assertElement("description", String.class, false);
		assertCollection("readings", false, "reading");
	}
}