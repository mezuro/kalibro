package org.kalibro.client;

import static org.junit.Assert.assertEquals;

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

	private static final Long ID = mock(Long.class);

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
		Long configurationId = mock(Long.class);
		when(port.saveMetricConfiguration(request, configurationId)).thenReturn(ID);
		assertEquals(ID, client.save(entity, configurationId));
	}

	@Test
	public void shouldDelete() {
		client.delete(ID);
		verify(port).deleteMetricConfiguration(ID);
	}
}