package org.kalibro.client;

import static org.junit.Assert.assertSame;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.MetricConfiguration;
import org.kalibro.service.MetricConfigurationEndpoint;
import org.kalibro.service.xml.MetricConfigurationXml;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest({MetricConfigurationClientDao.class, EndpointClient.class})
public class MetricConfigurationClientDaoTest extends ClientTest<// @formatter:off
	MetricConfiguration, MetricConfigurationXml, MetricConfigurationXml,
	MetricConfigurationEndpoint, MetricConfigurationClientDao> { // @formatter:on

	private static final String CONFIGURATION_NAME = "MetricConfigurationClientDaoTest configuration name";
	private static final String METRIC_NAME = "MetricConfigurationClientDaoTest metric name";

	@Override
	protected Class<MetricConfiguration> entityClass() {
		return MetricConfiguration.class;
	}

	@Test
	public void testSave() {
		client.save(entity, CONFIGURATION_NAME);
		verify(port).saveMetricConfiguration(request, CONFIGURATION_NAME);
	}

	@Test
	public void testGetMetricConfiguration() {
		when(port.getMetricConfiguration(METRIC_NAME, CONFIGURATION_NAME)).thenReturn(response);
		assertSame(entity, client.getMetricConfiguration(METRIC_NAME, CONFIGURATION_NAME));
	}

	@Test
	public void testRemoveMetricConfiguration() {
		client.removeMetricConfiguration(METRIC_NAME, CONFIGURATION_NAME);
		verify(port).removeMetricConfiguration(METRIC_NAME, CONFIGURATION_NAME);
	}
}