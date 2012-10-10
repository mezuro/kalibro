package org.kalibro.service.xml;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ProjectXmlRequestTest extends XmlTest {

	@Override
	public void verifyElements() {
		assertElement("id", Long.class);
		assertElement("name", String.class, true);
		assertElement("description", String.class);
		assertCollection("repository");
	}

	@Test
	public void shouldConvertNullRepositoriesIntoEmptyCollection() {
		assertTrue(new ProjectXmlRequest().repositories().isEmpty());
	}
}