package org.kalibro.service.xml;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.kalibro.ReadingGroup;

public class ReadingGroupXmlRequestTest extends XmlTest<ReadingGroup> {

	@Override
	protected ReadingGroup loadFixture() {
		return loadFixture("scholar", ReadingGroup.class);
	}

	@Test
	public void verifyElements() {
		assertElement("id", Long.class, false);
		assertElement("name", String.class, true);
		assertElement("description", String.class, false);
		assertCollection("readings", false, "reading");
	}

	@Test
	public void shouldConvertNullReadingsIntoEmptyList() {
		assertTrue(new ReadingGroupXmlRequest().readings().isEmpty());
	}
}