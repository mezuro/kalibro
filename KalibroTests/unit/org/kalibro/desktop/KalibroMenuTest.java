package org.kalibro.desktop;

import javax.swing.JMenuItem;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.KalibroTestCase;
import org.kalibro.desktop.settings.SettingsController;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareOnlyThisForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PowerMockIgnore("javax.*")
@PrepareOnlyThisForTest({KalibroMenu.class, SettingsController.class})
public class KalibroMenuTest extends KalibroTestCase {

	private KalibroMenu menu;

	@Before
	public void seUp() {
		PowerMockito.mockStatic(System.class);
		PowerMockito.mockStatic(SettingsController.class);
		menu = new KalibroMenu();
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldEditSettings() {
		JMenuItem settingsItem = menu.getItem(0);
		settingsItem.doClick();
		PowerMockito.verifyStatic();
		SettingsController.editSettings();
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldCloseSystem() {
		JMenuItem exitItem = menu.getItem(2);
		exitItem.doClick();
		PowerMockito.verifyStatic();
		System.exit(0);
	}
}