package org.kalibro.desktop.settings;

import static org.junit.Assert.*;

import org.junit.Test;
import org.kalibro.KalibroDesktopTestCase;
import org.kalibro.core.settings.KalibroSettings;

/**
 * On the first execution, if the user edit the settings and confirms, the settings file should be written and the
 * application frame opened. The settings should still be available via menu.
 * 
 * @author Carlos Morais
 */
public class FirstEditSettings extends KalibroDesktopTestCase {

	@Test(timeout = ACCEPTANCE_TIMEOUT)
	public void firstEditSettings() {
		startKalibroDesktop();
		captureSettingsDialog();

		KalibroSettings settings = new KalibroSettings();
		settings.getDatabaseSettings().setJdbcUrl("First");
		settings.getDatabaseSettings().setUsername("Edit");
		settings.getDatabaseSettings().setPassword("Settings");

		fixture.textBox("jdbcUrl").setText(settings.getDatabaseSettings().getJdbcUrl());
		fixture.textBox("username").setText(settings.getDatabaseSettings().getUsername());
		fixture.textBox("password").setText(settings.getDatabaseSettings().getPassword());
		fixture.button("ok").click();

		assertTrue(KalibroSettings.settingsFileExists());
		assertDeepEquals(settings, KalibroSettings.load());

		captureKalibroFrame();
		fixture.menuItem("settings").click();
		fixture.dialog().textBox("jdbcUrl").requireText(settings.getDatabaseSettings().getJdbcUrl());
		fixture.dialog().textBox("username").requireText(settings.getDatabaseSettings().getUsername());
		fixture.dialog().textBox("password").requireText(settings.getDatabaseSettings().getPassword());
		fixture.dialog().button("cancel").click();
		fixture.close();
	}
}