package org.kalibro.desktop.configuration;

import static org.junit.Assert.*;
import static org.powermock.api.mockito.PowerMockito.*;

import java.util.Arrays;
import java.util.List;

import javax.swing.JDesktopPane;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.TestCase;
import org.kalibro.core.dao.ConfigurationDao;
import org.kalibro.core.dao.DaoFactory;
import org.kalibro.core.model.Configuration;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ConfigurationController.class, DaoFactory.class})
public class ConfigurationControllerTest extends TestCase {

	private static final String NAME = "ConfigurationControllerTest name";
	private static final List<String> NAMES = Arrays.asList(NAME);

	private JDesktopPane desktopPane;
	private Configuration configuration;
	private ConfigurationDao configurationDao;

	private ConfigurationController controller;

	@Before
	public void setUp() {
		desktopPane = mock(JDesktopPane.class);
		configuration = mock(Configuration.class);
		mockConfigurationDao();
		controller = new ConfigurationController(desktopPane);
	}

	private void mockConfigurationDao() {
		configurationDao = mock(ConfigurationDao.class);
		mockStatic(DaoFactory.class);
		when(DaoFactory.getConfigurationDao()).thenReturn(configurationDao);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void checkDesktopPane() {
		assertSame(desktopPane, Whitebox.getInternalState(controller, JDesktopPane.class));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void checkEntityName() {
		assertEquals("Configuration", Whitebox.getInternalState(controller, String.class));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldCreateConfiguration() {
		configuration = controller.createEntity(NAME);
		assertEquals(NAME, configuration.getName());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldGetConfigurationNamesFromDao() {
		when(configurationDao.getConfigurationNames()).thenReturn(NAMES);
		assertSame(NAMES, controller.getEntityNames());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldGetConfigurationFromDao() {
		when(configurationDao.getConfiguration(NAME)).thenReturn(configuration);
		assertSame(configuration, controller.getEntity(NAME));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldCreateFrame() throws Exception {
		ConfigurationFrame configurationFrame = mock(ConfigurationFrame.class);
		whenNew(ConfigurationFrame.class).withArguments(configuration).thenReturn(configurationFrame);
		assertSame(configurationFrame, controller.createFrameFor(configuration));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldRemoveConfiguration() {
		controller.removeEntity(NAME);
		Mockito.verify(configurationDao).removeConfiguration(NAME);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldSaveConfiguration() {
		controller.save(configuration);
		Mockito.verify(configurationDao).save(configuration);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldSetConfigurationName() {
		controller.setEntityName(configuration, NAME);
		Mockito.verify(configuration).setName(NAME);
	}
}