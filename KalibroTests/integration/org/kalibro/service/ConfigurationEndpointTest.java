package org.kalibro.service;

import static org.junit.Assert.*;

import java.net.MalformedURLException;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.core.model.Configuration;
import org.kalibro.core.model.ConfigurationFixtures;
import org.kalibro.core.persistence.dao.ConfigurationDaoStub;
import org.kalibro.service.entities.ConfigurationXml;

public class ConfigurationEndpointTest extends KalibroServiceTestCase {

	private Configuration sample;
	private ConfigurationEndpoint port;

	@Before
	public void setUp() throws MalformedURLException {
		sample = ConfigurationFixtures.simpleConfiguration();
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
		Configuration newConfiguration = ConfigurationFixtures.simpleConfiguration();
		newConfiguration.setName("ConfigurationEndpointTest configuration");
		port.saveConfiguration(new ConfigurationXml(newConfiguration));
		assertDeepEquals(port.getConfigurationNames(), sample.getName(), newConfiguration.getName());
	}
}