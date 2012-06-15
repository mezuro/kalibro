package org.kalibro.desktop.configuration;

import static org.junit.Assert.*;
import static org.powermock.api.mockito.PowerMockito.*;

import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.KalibroTestCase;
import org.kalibro.desktop.project.ProjectFrame;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareOnlyThisForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PowerMockIgnore("javax.*")
@PrepareOnlyThisForTest(ConfigurationMenu.class)
public class ConfigurationMenuTest extends KalibroTestCase {

	private JDesktopPane desktopPane;
	private ConfigurationController controller;

	private ConfigurationMenu menu;

	@Before
	public void setUp() throws Exception {
		desktopPane = mock(JDesktopPane.class);
		controller = mock(ConfigurationController.class);
		whenNew(ConfigurationController.class).withArguments(desktopPane).thenReturn(controller);
		menu = new ConfigurationMenu(desktopPane);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void checkControllerInitialization() {
		menu.getItem(0).doClick();
		Mockito.verify(controller).newEntity();
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldValidateEntityFrame() {
		assertFalse(menu.isEntityFrame(new JInternalFrame()));
		assertFalse(menu.isEntityFrame(mock(ProjectFrame.class)));
		assertTrue(menu.isEntityFrame(mock(ConfigurationFrame.class)));
	}
}