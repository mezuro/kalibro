package org.kalibro.desktop.settings;

import static org.junit.Assert.*;

import java.util.regex.Pattern;

import org.fest.swing.exception.ComponentLookupException;
import org.fest.swing.fixture.FrameFixture;
import org.junit.Test;
import org.kalibro.KalibroDesktopTestCase;
import org.kalibro.core.concurrent.Task;
import org.kalibro.core.settings.ClientSettings;
import org.kalibro.core.settings.DatabaseSettings;
import org.kalibro.core.settings.KalibroSettings;
import org.kalibro.core.settings.ServerSettings;
import org.kalibro.desktop.swingextension.field.LongField;

/**
 * When opening for the first time, the dialog for editing the settings should open showing the default settings.<br/>
 * If cancels of tries to confirm invalid settings, no file should be written.<br/>
 * If the user cancels, the application should close.
 * 
 * @author Carlos Morais
 */
public class CancelFirstEditSettings extends KalibroDesktopTestCase {

	@Test(timeout = ACCEPTANCE_TIMEOUT)
	public void cancelFirstEditSettings() throws Exception {
		startFromMain();

		KalibroSettings settings = new KalibroSettings();
		assertFalse(settings.isClient());
		fixture.checkBox("client").requireNotSelected();
		checkServerSettings(settings.getServerSettings());

		fixture.checkBox("client").click();
		fixture.checkBox("client").requireSelected();
		checkClientSettings(settings.getClientSettings());

		fixture.button("ok").click();
		verifyErrorMessageForConnectionRefused();

		fixture.button("cancel").click();
		assertFalse(KalibroSettings.settingsFileExists());
		verifyFrameNotOpen();
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

	private void verifyErrorMessageForConnectionRefused() {
		fixture.optionPane().requireErrorMessage();
		fixture.optionPane().requireTitle("Error");
		fixture.optionPane().requireMessage(Pattern.compile("^Failed to access the WSDL at.*", Pattern.DOTALL));
		fixture.optionPane().button().click();
	}

	private void verifyFrameNotOpen() {
		checkException(new Task() {

			@Override
			public void perform() throws Exception {
				fixture = new FrameFixture(fixture.robot, "kalibroFrame");
			}
		}, ComponentLookupException.class);
	}
}