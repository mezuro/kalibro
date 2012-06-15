package org.kalibro.desktop.project;

import static org.junit.Assert.*;
import static org.powermock.api.mockito.PowerMockito.*;

import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.KalibroTestCase;
import org.kalibro.desktop.configuration.ConfigurationFrame;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareOnlyThisForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PowerMockIgnore("javax.*")
@PrepareOnlyThisForTest(ProjectMenu.class)
public class ProjectMenuTest extends KalibroTestCase {

	private JDesktopPane desktopPane;
	private ProjectController controller;

	private ProjectMenu menu;

	@Before
	public void setUp() throws Exception {
		desktopPane = mock(JDesktopPane.class);
		controller = mock(ProjectController.class);
		whenNew(ProjectController.class).withArguments(desktopPane).thenReturn(controller);
		menu = new ProjectMenu(desktopPane);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void checkControllerInitialization() {
		menu.getItem(0).doClick();
		Mockito.verify(controller).newEntity();
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldValidateEntityFrame() {
		assertFalse(menu.isEntityFrame(new JInternalFrame()));
		assertFalse(menu.isEntityFrame(mock(ConfigurationFrame.class)));
		assertTrue(menu.isEntityFrame(mock(ProjectFrame.class)));
	}
}