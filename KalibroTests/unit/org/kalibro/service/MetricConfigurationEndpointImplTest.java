package org.kalibro.service;

import static org.junit.Assert.assertEquals;

import java.util.Random;

import org.junit.Test;
import org.kalibro.MetricConfiguration;
import org.kalibro.dao.MetricConfigurationDao;
import org.kalibro.service.xml.MetricConfigurationXmlRequest;
import org.kalibro.service.xml.MetricConfigurationXmlResponse;
import org.powermock.core.classloader.annotations.PrepareForTest;

@PrepareForTest(MetricConfigurationEndpointImpl.class)
public class MetricConfigurationEndpointImplTest extends EndpointImplementorTest<// @formatter:off
	MetricConfiguration, MetricConfigurationXmlRequest, MetricConfigurationXmlResponse,
	MetricConfigurationDao, MetricConfigurationEndpointImpl> {// @formatter:on

	private static final Long ID = Math.abs(new Random().nextLong());

	@Override
	protected Class<MetricConfiguration> entityClass() {
		return MetricConfiguration.class;
	}

	@Test
	public void shouldGetMetricConfigurationsOfConfiguration() {
		when(dao.metricConfigurationsOf(ID)).thenReturn(asSortedSet(entity));
		assertDeepEquals(asList(response), implementor.metricConfigurationsOf(ID));
	}

	@Test
	public void shouldSave() {
		Long configurationId = mock(Long.class);
		when(dao.save(entity, configurationId)).thenReturn(ID);
		assertEquals(ID, implementor.saveMetricConfiguration(request, configurationId));
	}

	@Test
	public void shouldDeleteReading() {
		implementor.deleteMetricConfiguration(ID);
		verify(dao).delete(ID);
	}
}