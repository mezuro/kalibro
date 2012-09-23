package org.kalibro.client;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.Configuration;
import org.kalibro.core.concurrent.VoidTask;
import org.kalibro.service.ConfigurationEndpoint;
import org.kalibro.service.xml.ConfigurationXml;
import org.kalibro.tests.UnitTest;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ConfigurationClientDao.class, EndpointClient.class})
public class ConfigurationClientDaoTest extends UnitTest {

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

	@Test
	public void testSave() {
		dao.save(configuration);
		verify(port).saveConfiguration(configurationXml);
	}

	@Test
	public void testGetConfigurationNames() {
		List<String> names = mock(List.class);
		when(port.getConfigurationNames()).thenReturn(names);
		assertSame(names, dao.getConfigurationNames());
	}

	@Test
	public void testConfirmConfiguration() {
		when(port.hasConfiguration("42")).thenReturn(true);
		assertTrue(dao.hasConfiguration("42"));

		when(port.hasConfiguration("42")).thenReturn(false);
		assertFalse(dao.hasConfiguration("42"));
	}

	@Test
	public void testGetConfiguration() {
		when(port.getConfiguration("")).thenReturn(configurationXml);
		assertSame(configuration, dao.getConfiguration(""));
	}

	@Test
	public void testGetConfigurationFor() {
		assertThat(new VoidTask() {

			@Override
			protected void perform() {
				dao.getConfigurationFor("42");
			}
		}).throwsException().withMessage("Not available remotely");
	}

	@Test
	public void testDeleteConfiguration() {
		dao.delete(42L);
		verify(port).deleteConfiguration(42L);
	}
}
