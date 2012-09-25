package org.kalibro.service;

import static org.kalibro.core.model.MetricConfigurationFixtures.metricConfiguration;

import org.junit.Test;
import org.kalibro.MetricConfiguration;
import org.kalibro.dao.MetricConfigurationDao;
import org.kalibro.service.xml.MetricConfigurationXml;

public class MetricConfigurationEndpointTest extends
	EndpointTest<MetricConfiguration, MetricConfigurationDao, MetricConfigurationEndpoint> {

	private static final String CONFIGURATION_NAME = "MetricConfigurationEndpointTest configuration name";

	@Override
	protected MetricConfiguration loadFixture() {
		return metricConfiguration("cbo");
	}

	@Test
	public void shouldGetMetricConfigurationByName() {
		when(dao.getMetricConfiguration(CONFIGURATION_NAME, "42")).thenReturn(entity);
		assertDeepDtoEquals(entity, port.getMetricConfiguration(CONFIGURATION_NAME, "42"));
	}

	@Test
	public void shouldRemoveConfigurationByName() {
		port.removeMetricConfiguration(CONFIGURATION_NAME, "42");
		verify(dao).removeMetricConfiguration(CONFIGURATION_NAME, "42");
	}

	@Test
	public void shouldSaveConfiguration() {
		port.saveMetricConfiguration(new MetricConfigurationXml(entity), CONFIGURATION_NAME);
		verify(dao).save(entity, CONFIGURATION_NAME);
	}
}