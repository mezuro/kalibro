package org.kalibro.service.xml;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ReadingGroupXmlRequestTest extends XmlTest {

	@Override
	protected void verifyElements() {
		assertElement("id", Long.class);
		assertElement("name", String.class, true);
		assertElement("description", String.class);
		assertCollection("reading");
	}

	@Test
	public void shouldConvertNullReadingsIntoEmptyList() {
		assertTrue(new ReadingGroupXmlRequest().readings().isEmpty());
	}
}