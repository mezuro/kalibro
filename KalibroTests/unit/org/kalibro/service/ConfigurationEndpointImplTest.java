package org.kalibro.service;

import static org.junit.Assert.*;
import static org.kalibro.core.model.ConfigurationFixtures.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.Kalibro;
import org.kalibro.KalibroTestCase;
import org.kalibro.core.model.Configuration;
import org.kalibro.core.persistence.dao.ConfigurationDao;
import org.kalibro.service.entities.ConfigurationXml;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(Kalibro.class)
public class ConfigurationEndpointImplTest extends KalibroTestCase {

	private ConfigurationDao dao;
	private Configuration configuration;
	private ConfigurationEndpointImpl endpoint;

	@Before
	public void setUp() {
		mockDao();
		configuration = simpleConfiguration();
		endpoint = new ConfigurationEndpointImpl();
	}

	private void mockDao() {
		dao = PowerMockito.mock(ConfigurationDao.class);
		PowerMockito.mockStatic(Kalibro.class);
		PowerMockito.when(Kalibro.getConfigurationDao()).thenReturn(dao);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void testSaveConfiguration() {
		endpoint.saveConfiguration(new ConfigurationXml(configuration));
		Mockito.verify(dao).save(configuration);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void testGetConfigurationNames() {
		List<String> names = new ArrayList<String>();
		PowerMockito.when(dao.getConfigurationNames()).thenReturn(names);
		assertSame(names, endpoint.getConfigurationNames());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void testGetConfiguration() {
		PowerMockito.when(dao.getConfiguration("42")).thenReturn(configuration);
		assertDeepEquals(configuration, endpoint.getConfiguration("42").convert());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void testRemoveConfiguration() {
		endpoint.removeConfiguration("42");
		Mockito.verify(dao).removeConfiguration("42");
	}
}