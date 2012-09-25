package org.kalibro.service;

import static org.kalibro.ConfigurationFixtures.newConfiguration;
import static org.kalibro.MetricFixtures.analizoMetric;

import java.util.Arrays;

import org.junit.Test;
import org.kalibro.Configuration;
import org.kalibro.Granularity;
import org.kalibro.MetricConfiguration;
import org.kalibro.NativeMetric;
import org.kalibro.client.EndpointTest;
import org.kalibro.dao.ConfigurationDao;
import org.kalibro.service.xml.ConfigurationXml;

public class ConfigurationEndpointTest extends EndpointTest<Configuration, ConfigurationDao, ConfigurationEndpoint> {

	@Override
	protected Configuration loadFixture() {
		return newConfiguration();
	}

	@Test
	public void shouldListConfigurationNames() {
		when(dao.getConfigurationNames()).thenReturn(Arrays.asList("42"));
		assertDeepList(port.getConfigurationNames(), "42");
	}

	@Test
	public void shouldGetConfigurationByName() {
		when(dao.getConfiguration("42")).thenReturn(entity);
		assertDeepDtoEquals(entity, port.getConfiguration("42"));
	}

	@Test
	public void shouldDeleteConfigurationById() {
		port.deleteConfiguration(42L);
		verify(dao).delete(42L);
	}

	@Test
	public void shouldSaveNormalConfiguration() {
		entity = newConfiguration("loc");
		shouldSaveConfiguration();
	}

	@Test
	public void shouldSaveEmptyConfiguration() {
		entity = newConfiguration();
		shouldSaveConfiguration();
	}

	@Test
	public void shouldSaveConfigurationWithoutRanges() {
		entity = newConfiguration();
		entity.addMetricConfiguration(new MetricConfiguration(analizoMetric("loc")));
		shouldSaveConfiguration();
	}

	@Test
	public void shouldSaveMetricWithoutLanguages() {
		NativeMetric nativeMetric = new NativeMetric("name", Granularity.METHOD);
		nativeMetric.setOrigin("origin");
		entity = new Configuration();
		entity.addMetricConfiguration(new MetricConfiguration(nativeMetric));
		shouldSaveConfiguration();
	}

	private void shouldSaveConfiguration() {
		port.saveConfiguration(new ConfigurationXml(entity));
		verify(dao).save(entity);
	}
}