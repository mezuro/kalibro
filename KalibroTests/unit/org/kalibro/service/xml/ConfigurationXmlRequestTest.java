package org.kalibro.service.xml;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.kalibro.Configuration;

public class ConfigurationXmlRequestTest extends XmlTest<Configuration> {

	@Override
	protected Configuration loadFixture() {
		return loadFixture("sc", Configuration.class);
	}

	@Test
	public void verifyElements() {
		assertElement("id", Long.class, false);
		assertElement("name", String.class, true);
		assertElement("description", String.class, false);
		assertCollection("metricConfigurations", false, "metricConfiguration");
	}

	@Test
	public void shouldConvertNullMetricConfigurationsIntoEmptyList() {
		assertTrue(new ConfigurationXmlRequest().metricConfigurations().isEmpty());
	}
}