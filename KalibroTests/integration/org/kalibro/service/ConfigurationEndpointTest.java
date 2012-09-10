package org.kalibro.service;

import static org.junit.Assert.assertTrue;
import static org.kalibro.core.model.ConfigurationFixtures.newConfiguration;
import static org.kalibro.core.model.MetricFixtures.analizoMetric;

import java.net.MalformedURLException;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.core.model.Configuration;
import org.kalibro.core.model.MetricConfiguration;
import org.kalibro.core.model.NativeMetric;
import org.kalibro.core.model.enums.Granularity;
import org.kalibro.dao.ConfigurationDaoFake;
import org.kalibro.service.entities.ConfigurationXml;

public class ConfigurationEndpointTest extends KalibroServiceTestCase {

	private Configuration sample;
	private ConfigurationEndpoint port;

	@Before
	public void setUp() throws MalformedURLException {
		sample = newConfiguration();
		ConfigurationDaoFake daoFake = new ConfigurationDaoFake();
		daoFake.save(sample);
		port = publishAndGetPort(new ConfigurationEndpointImpl(daoFake), ConfigurationEndpoint.class);
	}

	@Test(timeout = INTEGRATION_TIMEOUT)
	public void shouldListConfigurationNames() {
		assertDeepList(port.getConfigurationNames(), sample.getName());
	}

	@Test(timeout = INTEGRATION_TIMEOUT)
	public void shouldGetConfigurationByName() {
		assertDeepEquals(sample, port.getConfiguration(sample.getName()).convert());
	}

	@Test(timeout = INTEGRATION_TIMEOUT)
	public void shouldRemoveConfigurationByName() {
		port.removeConfiguration(sample.getName());
		assertTrue(port.getConfigurationNames().isEmpty());
	}

	@Test(timeout = INTEGRATION_TIMEOUT)
	public void shouldSaveConfiguration() {
		testSaveConfiguration(newConfiguration("loc"));
	}

	@Test(timeout = INTEGRATION_TIMEOUT)
	public void shouldSaveEmptyConfiguration() {
		testSaveConfiguration(newConfiguration());
	}

	@Test(timeout = INTEGRATION_TIMEOUT)
	public void shouldSaveConfigurationWithoutRanges() {
		Configuration newConfiguration = newConfiguration();
		newConfiguration.addMetricConfiguration(new MetricConfiguration(analizoMetric("loc")));
		testSaveConfiguration(newConfiguration);
	}

	@Test(timeout = INTEGRATION_TIMEOUT)
	public void shouldSaveMetricWithoutLanguages() {
		NativeMetric nativeMetric = new NativeMetric("name", Granularity.METHOD);
		nativeMetric.setOrigin("origin");
		Configuration newConfiguration = new Configuration();
		newConfiguration.addMetricConfiguration(new MetricConfiguration(nativeMetric));
		testSaveConfiguration(newConfiguration);
	}

	private void testSaveConfiguration(Configuration newConfiguration) {
		newConfiguration.setName("ConfigurationEndpointTest configuration");
		port.saveConfiguration(new ConfigurationXml(newConfiguration));
		assertDeepList(port.getConfigurationNames(), newConfiguration.getName(), sample.getName());
	}
}