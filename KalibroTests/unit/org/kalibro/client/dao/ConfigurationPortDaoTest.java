package org.kalibro.client.dao;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.KalibroTestCase;
import org.kalibro.client.EndpointPortFactory;
import org.kalibro.core.model.Configuration;
import org.kalibro.core.model.ConfigurationFixtures;
import org.kalibro.service.ConfigurationEndpoint;
import org.kalibro.service.entities.ConfigurationXml;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(EndpointPortFactory.class)
public class ConfigurationPortDaoTest extends KalibroTestCase {

	private Configuration configuration;

	private ConfigurationPortDao dao;
	private ConfigurationEndpoint port;

	@Before
	public void setUp() {
		mockPort();
		dao = new ConfigurationPortDao();
		configuration = ConfigurationFixtures.simpleConfiguration();
	}

	private void mockPort() {
		port = PowerMockito.mock(ConfigurationEndpoint.class);
		PowerMockito.mockStatic(EndpointPortFactory.class);
		PowerMockito.when(EndpointPortFactory.getEndpointPort(ConfigurationEndpoint.class)).thenReturn(port);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void testSave() {
		dao.save(configuration);

		ArgumentCaptor<ConfigurationXml> captor = ArgumentCaptor.forClass(ConfigurationXml.class);
		Mockito.verify(port).saveConfiguration(captor.capture());
		assertDeepEquals(configuration, captor.getValue().convert());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void testGetConfigurationNames() {
		List<String> names = new ArrayList<String>();
		PowerMockito.when(port.getConfigurationNames()).thenReturn(names);
		assertSame(names, dao.getConfigurationNames());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void testGetConfiguration() {
		PowerMockito.when(port.getConfiguration("")).thenReturn(new ConfigurationXml(configuration));
		assertDeepEquals(configuration, dao.getConfiguration(""));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void testGetConfigurationFor() {
		checkKalibroException(new Runnable() {

			@Override
			public void run() {
				dao.getConfigurationFor("42");
			}
		}, "Not available remotely");
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void testRemoveConfiguration() {
		dao.removeConfiguration("");
		Mockito.verify(port).removeConfiguration("");
	}
}