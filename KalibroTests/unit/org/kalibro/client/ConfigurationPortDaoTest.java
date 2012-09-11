package org.kalibro.client;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.TestCase;
import org.kalibro.core.concurrent.Task;
import org.kalibro.core.model.Configuration;
import org.kalibro.service.ConfigurationEndpoint;
import org.kalibro.service.entities.ConfigurationXml;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ConfigurationClientDao.class, EndpointClient.class})
public class ConfigurationPortDaoTest extends TestCase {

	private Configuration configuration;
	private ConfigurationXml configurationXml;

	private ConfigurationClientDao dao;
	private ConfigurationEndpoint port;

	@Before
	public void setUp() throws Exception {
		mockConfiguration();
		createSupressedDao();
	}

	private void mockConfiguration() throws Exception {
		configuration = mock(Configuration.class);
		configurationXml = mock(ConfigurationXml.class);
		whenNew(ConfigurationXml.class).withArguments(configuration).thenReturn(configurationXml);
		when(configurationXml.convert()).thenReturn(configuration);
	}

	private void createSupressedDao() {
		suppress(constructor(EndpointClient.class, String.class, Class.class));
		dao = new ConfigurationClientDao("");

		port = mock(ConfigurationEndpoint.class);
		Whitebox.setInternalState(dao, "port", port);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void testSave() {
		dao.save(configuration);
		verify(port).saveConfiguration(configurationXml);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void testGetConfigurationNames() {
		List<String> names = mock(List.class);
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
		when(port.getConfiguration("")).thenReturn(configurationXml);
		assertSame(configuration, dao.getConfiguration(""));
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
		verify(port).removeConfiguration("");
	}
}