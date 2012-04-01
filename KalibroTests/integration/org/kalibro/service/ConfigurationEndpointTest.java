package org.kalibro.service;

import static org.analizo.AnalizoStub.*;
import static org.junit.Assert.*;
import static org.kalibro.core.model.ConfigurationFixtures.*;

import java.net.MalformedURLException;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.core.model.Configuration;
import org.kalibro.core.model.MetricConfiguration;
import org.kalibro.core.model.NativeMetric;
import org.kalibro.core.model.enums.Granularity;
import org.kalibro.core.persistence.dao.ConfigurationDaoStub;
import org.kalibro.service.entities.ConfigurationXml;

public class ConfigurationEndpointTest extends KalibroServiceTestCase {

	private Configuration sample;
	private ConfigurationEndpoint port;

	@Before
	public void setUp() throws MalformedURLException {
		sample = simpleConfiguration();
		ConfigurationDaoStub daoStub = new ConfigurationDaoStub();
		daoStub.save(sample);
		port = publishAndGetPort(new ConfigurationEndpointImpl(daoStub), ConfigurationEndpoint.class);
	}

	@Test(timeout = INTEGRATION_TIMEOUT)
	public void shouldListConfigurationNames() {
		assertDeepEquals(port.getConfigurationNames(), sample.getName());
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
		testSaveConfiguration(simpleConfiguration());
	}

	@Test(timeout = INTEGRATION_TIMEOUT)
	public void shouldSaveEmptyConfiguration() {
		testSaveConfiguration(new Configuration());
	}

	@Test(timeout = INTEGRATION_TIMEOUT)
	public void shouldSaveConfigurationWithoutRanges() {
		Configuration newConfiguration = new Configuration();
		newConfiguration.addMetricConfiguration(new MetricConfiguration(nativeMetric("loc")));
		testSaveConfiguration(newConfiguration);
	}

	@Test(timeout = INTEGRATION_TIMEOUT)
	public void shouldSaveMetricWithoutLanguages() {
		NativeMetric nativeMetric = new NativeMetric("name", Granularity.METHOD);
		Configuration newConfiguration = new Configuration();
		newConfiguration.addMetricConfiguration(new MetricConfiguration(nativeMetric));
		testSaveConfiguration(newConfiguration);
	}

	private void testSaveConfiguration(Configuration newConfiguration) {
		newConfiguration.setName("ConfigurationEndpointTest configuration");
		port.saveConfiguration(new ConfigurationXml(newConfiguration));
		assertDeepEquals(port.getConfigurationNames(), newConfiguration.getName(), sample.getName());
	}
}