package org.kalibro.service.xml;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.kalibro.Granularity;

public class MetricXmlResponseTest extends XmlTest {

	@Override
	public void verifyElements() {
		assertElement("compound", boolean.class);
		assertElement("name", String.class);
		assertElement("scope", Granularity.class);
		assertElement("description", String.class);
		assertElement("script", String.class);
		assertCollection("language");
	}

	@Test
	public void shouldConvertNullLanguagesIntoEmptyCollection() {
		assertTrue(new MetricXmlRequest().languages().isEmpty());
	}
}