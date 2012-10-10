package org.kalibro.service.xml;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ConfigurationXmlRequestTest extends XmlTest {

	@Override
	public void verifyElements() {
		assertElement("id", Long.class);
		assertElement("name", String.class, true);
		assertElement("description", String.class);
		assertCollection("metricConfiguration");
	}

	@Test
	public void shouldConvertNullMetricConfigurationsIntoEmptyCollection() {
		assertTrue(new ConfigurationXmlRequest().metricConfigurations().isEmpty());
	}
}