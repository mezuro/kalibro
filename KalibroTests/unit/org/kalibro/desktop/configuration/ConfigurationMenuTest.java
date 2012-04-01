package org.kalibro.desktop.configuration;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import javax.swing.JDesktopPane;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.KalibroTestCase;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareOnlyThisForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PowerMockIgnore("javax.*")
@PrepareOnlyThisForTest(ConfigurationMenu.class)
public class ConfigurationMenuTest extends KalibroTestCase {

	private static final int NEW = 0;
	private static final int OPEN = 1;
	private static final int DELETE = 2;
	private static final int SAVE = 4;
	private static final int SAVE_AS = 5;
	private static final int CLOSE = 6;

	private ConfigurationController controller;
	private JDesktopPane desktopPane;
	private ConfigurationMenu menu;

	@Before
	public void setUp() throws Exception {
		desktopPane = PowerMockito.mock(JDesktopPane.class);
		controller = PowerMockito.mock(ConfigurationController.class);
		PowerMockito.whenNew(ConfigurationController.class).withArguments(desktopPane).thenReturn(controller);
		menu = new ConfigurationMenu(desktopPane);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldCreateNewConfiguration() {
		menu.getItem(NEW).doClick();
		verify(controller).newConfiguration();
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldOpenConfiguration() {
		menu.getItem(OPEN).doClick();
		verify(controller).open();
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldDeleteConfiguration() {
		menu.getItem(DELETE).doClick();
		verify(controller).delete();
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldSaveConfiguration() {
		menu.getItem(SAVE).doClick();
		verify(controller).save();
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldSaveConfigurationWithOtherName() {
		menu.getItem(SAVE_AS).doClick();
		verify(controller).saveAs();
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldCloseConfiguration() {
		menu.getItem(CLOSE).doClick();
		verify(controller).close();
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void itemsForCurrentConfigurationShouldBeDisabledWhenThereIsNoConfigurationFrameSelected() {
		menu.menuSelected(null);
		assertFalse(menu.getItem(SAVE).isEnabled());
		assertFalse(menu.getItem(SAVE_AS).isEnabled());
		assertFalse(menu.getItem(CLOSE).isEnabled());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void itemsForCurrentConfigurationShouldBeEnabledWhenConfigurationFrameIsSelected() {
		ConfigurationFrame configurationFrame = PowerMockito.mock(ConfigurationFrame.class);
		PowerMockito.when(desktopPane.getSelectedFrame()).thenReturn(configurationFrame);

		menu.menuSelected(null);
		assertTrue(menu.getItem(SAVE).isEnabled());
		assertTrue(menu.getItem(SAVE_AS).isEnabled());
		assertTrue(menu.getItem(CLOSE).isEnabled());
	}
}