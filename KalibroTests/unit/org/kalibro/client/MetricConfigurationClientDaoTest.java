package org.kalibro.client;

import static org.junit.Assert.assertEquals;

import java.util.Random;

import org.junit.Test;
import org.kalibro.MetricConfiguration;
import org.kalibro.service.MetricConfigurationEndpoint;
import org.kalibro.service.xml.MetricConfigurationXmlRequest;
import org.kalibro.service.xml.MetricConfigurationXmlResponse;
import org.powermock.core.classloader.annotations.PrepareOnlyThisForTest;

@PrepareOnlyThisForTest(MetricConfigurationClientDao.class)
public class MetricConfigurationClientDaoTest extends ClientTest<// @formatter:off
	MetricConfiguration, MetricConfigurationXmlRequest, MetricConfigurationXmlResponse,
	MetricConfigurationEndpoint, MetricConfigurationClientDao> { // @formatter:on

	private static final Long ID = new Random().nextLong();
	private static final Long CONFIGURATION_ID = new Random().nextLong();

	@Override
	protected Class<MetricConfiguration> entityClass() {
		return MetricConfiguration.class;
	}

	@Test
	public void shouldGetMetricConfigurationsOfConfiguration() {
		when(port.metricConfigurationsOf(CONFIGURATION_ID)).thenReturn(list(response));
		assertDeepEquals(set(entity), client.metricConfigurationsOf(CONFIGURATION_ID));
	}

	@Test
	public void shouldSave() {
		when(port.saveMetricConfiguration(request, CONFIGURATION_ID)).thenReturn(ID);
		assertEquals(ID, client.save(entity, CONFIGURATION_ID));
	}

	@Test
	public void shouldDelete() {
		client.delete(ID);
		verify(port).deleteMetricConfiguration(ID);
	}
}