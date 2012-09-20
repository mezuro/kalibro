package org.kalibro.service;

import static org.junit.Assert.*;
import static org.kalibro.core.model.ConfigurationFixtures.newConfiguration;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.Configuration;
import org.kalibro.TestCase;
import org.kalibro.dao.ConfigurationDao;
import org.kalibro.dao.DaoFactory;
import org.kalibro.service.entities.ConfigurationXml;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(DaoFactory.class)
public class ConfigurationEndpointImplTest extends TestCase {

	private ConfigurationDao dao;
	private Configuration configuration;
	private ConfigurationEndpointImpl endpoint;

	@Before
	public void setUp() {
		mockDao();
		configuration = newConfiguration();
		endpoint = new ConfigurationEndpointImpl();
	}

	private void mockDao() {
		dao = mock(ConfigurationDao.class);
		mockStatic(DaoFactory.class);
		when(DaoFactory.getConfigurationDao()).thenReturn(dao);
	}

	@Test
	public void testSaveConfiguration() {
		endpoint.saveConfiguration(new ConfigurationXml(configuration));
		Mockito.verify(dao).save(configuration);
	}

	@Test
	public void testGetConfigurationNames() {
		List<String> names = new ArrayList<String>();
		when(dao.getConfigurationNames()).thenReturn(names);
		assertSame(names, endpoint.getConfigurationNames());
	}

	@Test
	public void testConfirmConfiguration() {
		when(dao.hasConfiguration("42")).thenReturn(true);
		assertTrue(endpoint.hasConfiguration("42"));

		when(dao.hasConfiguration("42")).thenReturn(false);
		assertFalse(endpoint.hasConfiguration("42"));
	}

	@Test
	public void testGetConfiguration() {
		when(dao.getConfiguration("42")).thenReturn(configuration);
		assertDeepEquals(configuration, endpoint.getConfiguration("42").convert());
	}

	@Test
	public void testRemoveConfiguration() {
		endpoint.deleteConfiguration(42L);
		Mockito.verify(dao).delete(42L);
	}
}