package org.kalibro.client;

import static org.junit.Assert.*;

import java.util.Random;

import org.junit.Test;
import org.kalibro.MetricConfiguration;
import org.kalibro.service.MetricConfigurationEndpoint;
import org.kalibro.service.xml.MetricConfigurationXml;
import org.powermock.core.classloader.annotations.PrepareOnlyThisForTest;

@PrepareOnlyThisForTest(MetricConfigurationClientDao.class)
public class MetricConfigurationClientDaoTest extends
	ClientTest<MetricConfiguration, MetricConfigurationXml, MetricConfigurationEndpoint, MetricConfigurationClientDao> {

	private static final Long ID = new Random().nextLong();
	private static final Long CONFIGURATION_ID = new Random().nextLong();

	@Override
	protected Class<MetricConfiguration> entityClass() {
		return MetricConfiguration.class;
	}

	@Test
	public void shouldGetById() {
		when(port.getMetricConfiguration(ID)).thenReturn(xml);
		assertSame(entity, client.get(ID));
	}

	@Test
	public void shouldGetMetricConfigurationsOfConfiguration() {
		when(port.metricConfigurationsOf(CONFIGURATION_ID)).thenReturn(list(xml));
		assertDeepEquals(set(entity), client.metricConfigurationsOf(CONFIGURATION_ID));
	}

	@Test
	public void shouldSave() {
		when(port.saveMetricConfiguration(xml, CONFIGURATION_ID)).thenReturn(ID);
		assertEquals(ID, client.save(entity, CONFIGURATION_ID));
	}

	@Test
	public void shouldDelete() {
		client.delete(ID);
		verify(port).deleteMetricConfiguration(ID);
	}
}