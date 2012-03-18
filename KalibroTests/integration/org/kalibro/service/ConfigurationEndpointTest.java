package org.kalibro.service;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.client.dao.PortDaoFactory;
import org.kalibro.core.concurrent.Task;
import org.kalibro.core.model.Configuration;
import org.kalibro.core.model.ConfigurationFixtures;
import org.kalibro.core.persistence.dao.ConfigurationDao;

public class ConfigurationEndpointTest extends KalibroServiceTestCase {

	private ConfigurationDao dao;
	private Configuration configuration;

	@Before
	public void setUp() {
		dao = new PortDaoFactory().getConfigurationDao();
	}

	@Test(timeout = ACCEPTANCE_TIMEOUT)
	public void shouldHaveDefaultConfiguration() {
		configuration = ConfigurationFixtures.kalibroConfiguration();
		assertTrue(dao.getConfigurationNames().contains(configuration.getName()));

		Configuration retrieved = dao.getConfiguration(configuration.getName());
		assertDeepEquals(configuration, retrieved);
	}

	@Test(timeout = ACCEPTANCE_TIMEOUT)
	public void shouldSaveAndRemoveConfiguration() {
		String configurationName = "ConfigurationEndpointTest configuration";
		configuration = ConfigurationFixtures.simpleConfiguration();
		configuration.setName(configurationName);

		dao.save(configuration);
		assertTrue(dao.getConfigurationNames().contains(configurationName));
		assertDeepEquals(configuration, dao.getConfiguration(configurationName));

		dao.removeConfiguration(configurationName);
		assertFalse(dao.getConfigurationNames().contains(configuration));
	}

	@Test(timeout = ACCEPTANCE_TIMEOUT)
	public void shouldNotGetConfigurationForProjectRemotely() {
		checkException(new Task() {

			@Override
			public void perform() throws Exception {
				dao.getConfigurationFor("");
			}
		}, UnsupportedOperationException.class, "Not available remotely");
	}
}