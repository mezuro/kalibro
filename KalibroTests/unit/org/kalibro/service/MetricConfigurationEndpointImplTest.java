package org.kalibro.service;

import static org.junit.Assert.*;

import java.util.Random;

import org.junit.Test;
import org.kalibro.MetricConfiguration;
import org.kalibro.dao.MetricConfigurationDao;
import org.kalibro.service.xml.MetricConfigurationXml;
import org.powermock.core.classloader.annotations.PrepareForTest;

@PrepareForTest(MetricConfigurationEndpointImpl.class)
public class MetricConfigurationEndpointImplTest extends EndpointImplementorTest
	<MetricConfiguration, MetricConfigurationXml, MetricConfigurationDao, MetricConfigurationEndpointImpl> {

	private static final Long ID = new Random().nextLong();
	private static final Long CONFIGURATION_ID = new Random().nextLong();

	@Test
	public void shouldGetById() {
		when(dao.get(ID)).thenReturn(entity);
		assertSame(xml, implementor.getMetricConfiguration(ID));
	}

	@Test
	public void shouldGetMetricConfigurationsOfConfiguration() {
		doReturn(sortedSet(entity)).when(dao).metricConfigurationsOf(CONFIGURATION_ID);
		assertDeepEquals(list(xml), implementor.metricConfigurationsOf(CONFIGURATION_ID));
	}

	@Test
	public void shouldSave() {
		when(dao.save(entity, CONFIGURATION_ID)).thenReturn(ID);
		assertEquals(ID, implementor.saveMetricConfiguration(xml, CONFIGURATION_ID));
	}

	@Test
	public void shouldDeleteMetricConfiguration() {
		implementor.deleteMetricConfiguration(ID);
		verify(dao).delete(ID);
	}
}