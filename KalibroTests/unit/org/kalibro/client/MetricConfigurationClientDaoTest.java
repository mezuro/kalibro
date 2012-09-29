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

	private static final Long ID = Math.abs(new Random().nextLong());

	@Override
	protected Class<MetricConfiguration> entityClass() {
		return MetricConfiguration.class;
	}

	@Test
	public void shouldGetMetricConfigurationsOfConfiguration() {
		when(port.metricConfigurationsOf(ID)).thenReturn(asList(response));
		assertDeepEquals(asSet(entity), client.metricConfigurationsOf(ID));
	}

	@Test
	public void shouldSave() {
		when(port.saveMetricConfiguration(request)).thenReturn(ID);
		assertEquals(ID, client.save(entity));
	}

	@Test
	public void shouldDelete() {
		client.delete(ID);
		verify(port).deleteMetricConfiguration(ID);
	}
}