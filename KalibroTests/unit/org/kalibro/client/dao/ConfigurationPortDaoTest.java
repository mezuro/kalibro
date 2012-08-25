package org.kalibro.client.dao;

import static org.junit.Assert.*;
import static org.powermock.api.mockito.PowerMockito.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.TestCase;
import org.kalibro.client.EndpointPortFactory;
import org.kalibro.core.concurrent.Task;
import org.kalibro.core.model.Configuration;
import org.kalibro.core.model.ConfigurationFixtures;
import org.kalibro.service.ConfigurationEndpoint;
import org.kalibro.service.entities.ConfigurationXml;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(EndpointPortFactory.class)
public class ConfigurationPortDaoTest extends TestCase {

	private Configuration configuration;

	private ConfigurationPortDao dao;
	private ConfigurationEndpoint port;

	@Before
	public void setUp() {
		mockPort();
		dao = new ConfigurationPortDao();
		configuration = ConfigurationFixtures.newConfiguration();
	}

	private void mockPort() {
		port = mock(ConfigurationEndpoint.class);
		mockStatic(EndpointPortFactory.class);
		when(EndpointPortFactory.getEndpointPort(ConfigurationEndpoint.class)).thenReturn(port);
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
		when(port.getConfigurationNames()).thenReturn(names);
		assertSame(names, dao.getConfigurationNames());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void testConfirmConfiguration() {
		when(port.hasConfiguration("42")).thenReturn(true);
		assertTrue(dao.hasConfiguration("42"));

		when(port.hasConfiguration("42")).thenReturn(false);
		assertFalse(dao.hasConfiguration("42"));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void testGetConfiguration() {
		when(port.getConfiguration("")).thenReturn(new ConfigurationXml(configuration));
		assertDeepEquals(configuration, dao.getConfiguration(""));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void testGetConfigurationFor() {
		checkKalibroException(new Task() {

			@Override
			public void perform() {
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