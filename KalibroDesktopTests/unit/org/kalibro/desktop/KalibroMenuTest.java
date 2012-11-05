package org.kalibro.desktop;

import static org.junit.Assert.*;

import javax.swing.JMenuItem;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.desktop.settings.SettingsController;
import org.kalibro.tests.UnitTest;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareOnlyThisForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PowerMockIgnore("javax.*")
@PrepareOnlyThisForTest({KalibroMenu.class, SettingsController.class})
public class KalibroMenuTest extends UnitTest {

	private KalibroMenu menu;

	@Before
	public void seUp() {
		mockStatic(System.class);
		mockStatic(SettingsController.class);
		menu = new KalibroMenu();
	}

	@Test
	public void checkMenuAttributes() {
		assertEquals("kalibro", menu.getName());
		assertEquals("Kalibro", menu.getText());
		assertEquals('K', menu.getMnemonic());
	}

	@Test
	public void checkItemAttributes() {
		assertEquals("settings", settings().getName());
		assertEquals("Settings", settings().getText());
		assertEquals('S', settings().getMnemonic());

		assertNull(menu.getItem(1));

		assertEquals("exit", exit().getName());
		assertEquals("Exit", exit().getText());
		assertEquals('x', exit().getMnemonic());
	}

	@Test
	public void shouldEditSettings() {
		settings().doClick();

		verifyStatic();
		SettingsController.editSettings();
	}

	@Test
	public void shouldCloseSystem() {
		exit().doClick();

		verifyStatic();
		System.exit(0);
	}

	private JMenuItem settings() {
		return menu.getItem(0);
	}

	private JMenuItem exit() {
		return menu.getItem(2);
	}
}