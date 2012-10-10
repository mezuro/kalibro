package org.kalibro.service.xml;

import static org.junit.Assert.*;

import org.junit.Test;
import org.kalibro.Granularity;

public class MetricXmlRequestTest extends XmlTest {

	@Override
	public void verifyElements() {
		assertElement("compound", boolean.class, true);
		assertElement("name", String.class, true);
		assertElement("scope", Granularity.class, true);
		assertElement("description", String.class);
		assertElement("script", String.class);
		assertCollection("language");
	}

	@Test
	public void shouldConvertNullScriptIntoDefault() {
		assertEquals("return 1;", new MetricXmlRequest().script());
	}

	@Test
	public void shouldConvertNullLanguagesIntoEmptyCollection() {
		assertTrue(new MetricXmlRequest().languages().isEmpty());
	}
}