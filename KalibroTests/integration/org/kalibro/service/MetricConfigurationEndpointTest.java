package org.kalibro.service;

import static org.junit.Assert.assertEquals;
import static org.kalibro.MetricConfigurationFixtures.metricConfiguration;

import java.util.Random;

import org.junit.Test;
import org.kalibro.MetricConfiguration;
import org.kalibro.client.EndpointTest;
import org.kalibro.dao.MetricConfigurationDao;
import org.kalibro.service.xml.MetricConfigurationXmlRequest;

public class MetricConfigurationEndpointTest extends
	EndpointTest<MetricConfiguration, MetricConfigurationDao, MetricConfigurationEndpoint> {

	private static final Long ID = new Random().nextLong();

	@Override
	protected MetricConfiguration loadFixture() {
		return metricConfiguration("cbo");
	}

	@Test
	public void shouldGetMetricConfigurationsOfConfiguration() {
		when(dao.metricConfigurationsOf(ID)).thenReturn(asSortedSet(entity));
		assertDeepDtoList(port.metricConfigurationsOf(ID), entity);
	}

	@Test
	public void shouldSave() {
		when(dao.save(entity)).thenReturn(ID);
		assertEquals(ID, port.saveMetricConfiguration(new MetricConfigurationXmlRequest(entity)));
	}

	@Test
	public void shouldDelete() {
		port.deleteMetricConfiguration(ID);
		verify(dao).delete(ID);
	}
}