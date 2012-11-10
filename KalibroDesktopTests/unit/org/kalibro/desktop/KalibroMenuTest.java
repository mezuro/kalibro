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
import org.powermock.reflect.Whitebox;

@RunWith(PowerMockRunner.class)
@PowerMockIgnore("javax.*")
@PrepareOnlyThisForTest(KalibroMenu.class)
public class KalibroMenuTest extends UnitTest {

	private KalibroMenu menu;

	@Before
	public void seUp() {
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
		assertAction(settings(), SettingsController.class, "editSettings");
	}

	@Test
	public void shouldCloseSystem() {
		assertAction(exit(), System.class, "exit", 0);
	}

	private void assertAction(JMenuItem menuItem, Object target, String methodName, Object... arguments) {
		assertEquals(target, Whitebox.getInternalState(menuItem, "target"));
		assertEquals(methodName, Whitebox.getInternalState(menuItem, "methodName"));
		assertArrayEquals(arguments, Whitebox.getInternalState(menuItem, Object[].class));
	}

	private JMenuItem settings() {
		return menu.getItem(0);
	}

	private JMenuItem exit() {
		return menu.getItem(2);
	}
}