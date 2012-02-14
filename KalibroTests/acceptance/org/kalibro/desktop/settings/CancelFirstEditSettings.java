package org.kalibro.desktop.settings;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Test;
import org.kalibro.KalibroDesktopTestCase;
import org.kalibro.core.settings.ClientSettings;
import org.kalibro.core.settings.DatabaseSettings;
import org.kalibro.core.settings.KalibroSettings;
import org.kalibro.core.settings.ServerSettings;
import org.kalibro.desktop.KalibroFrame;
import org.kalibro.desktop.swingextension.field.LongField;
import org.powermock.api.mockito.PowerMockito;

/**
 * When opening for the first time, the dialog for editing the settings should open showing the default settings. If the
 * user cancels, the application should close and no settings file should be written.
 * 
 * @author Carlos Morais
 */
public class CancelFirstEditSettings extends KalibroDesktopTestCase {

	@Test(timeout = ACCEPTANCE_TIMEOUT)
	public void cancelFirstEditSettings() {
		startKalibroDesktop();
		captureSettingsDialog();
		KalibroSettings settings = new KalibroSettings();

		assertFalse(settings.isClient());
		fixture.checkBox("client").requireNotSelected();
		checkServerSettings(settings.getServerSettings());

		fixture.checkBox("client").click();
		fixture.checkBox("client").requireSelected();
		checkClientSettings(settings.getClientSettings());

		fixture.button("cancel").click();
		assertFalse(KalibroSettings.settingsFileExists());
		PowerMockito.verifyNew(KalibroFrame.class, never());
	}

	private void checkServerSettings(ServerSettings settings) {
		assertTrue(settings.shouldRemoveSources());
		fixture.textBox("path").requireText(settings.getLoadDirectory().getAbsolutePath());
		fixture.checkBox("removeSources").requireSelected();
		checkDatabaseSettings(settings.getDatabaseSettings());
	}

	private void checkDatabaseSettings(DatabaseSettings settings) {
		fixture.comboBox("databaseType").requireSelection(settings.getDatabaseType().toString());
		fixture.textBox("jdbcUrl").requireText(settings.getJdbcUrl());
		fixture.textBox("username").requireText(settings.getUsername());
		fixture.textBox("password").requireText(settings.getPassword());
	}

	private void checkClientSettings(ClientSettings settings) {
		fixture.textBox("serviceAddress").requireText(settings.getServiceAddress());
		String pollingInterval = new LongField("").getDecimalFormat().format(settings.getPollingInterval());
		fixture.textBox("pollingInterval").requireText(pollingInterval);
	}
}